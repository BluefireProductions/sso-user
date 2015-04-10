package com.bluefirereader.ballard.endpoints;

import com.bluefirereader.ballard.SsoUser;
import com.bluefirereader.ballard.endpoints.security.BallardEndpoint;
import com.bluefirereader.ballard.endpoints.parameters.BallardParameterAutoGson;
import com.bluefirereader.ballard.endpoints.parameters.BallardParameterExtraPath;
import com.bluefirereader.ballard.endpoints.parameters.BallardParameterRawBody;
import com.bluefirereader.ballard.endpoints.parameters.BallardParameterSsoUser;
import com.bluefirereader.ballard.endpoints.security.SecurityChecker;
import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

/**
 * Created by Timothy Jones on 11/24/14.
 */
public class JsonServlet extends HttpServlet{

    private static final Logger log = Logger.getLogger(JsonServlet.class.getName());

    public void init(ServletConfig servletConfig) throws ServletException{
        String endpointsRootPackage = null;
        endpointsRootPackage = servletConfig.getInitParameter("endpointsRootPackage");
        EndpointUtils.initializeEndpoints(endpointsRootPackage);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        // pre-flight request processing
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "*");
        resp.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setContentType("application/json");

        // '/' path is not allowed.
        if (req.getPathInfo().length() < 2){
            resp.setStatus(404);
            return;
        }

        //this should be paths off of the root servlet, and hence should start with /
        if (req.getPathInfo().toCharArray()[0] != '/'){
            resp.setStatus(404);
            return;
        }

        SsoUser ssoUser = null;

        try{
            ssoUser = EndpointUtils.getSsoUser(req);
        }
        catch (Exception e){
            log.severe("Error validating SSO user: " + e.getMessage());
        }


        try {
            MethodAndPath methodAndPath = EndpointUtils.getFunctionForPath(req.getPathInfo().substring(1));



            if (methodAndPath != null){

                //First do the security check.
                Class<? extends SecurityChecker> securityCheckerClass = methodAndPath.method.getAnnotation(BallardEndpoint.class).securityChecker();
                try {
                    SecurityChecker securityChecker = securityCheckerClass.newInstance();
                    if (!securityChecker.isAllowed(ssoUser,methodAndPath.path)){
                        resp.setStatus(403);
                        return;
                    }
                } catch (Exception e) {
                    log.severe("Error, could not instantiate specified security checker: " + securityCheckerClass.getCanonicalName() +": " + e.getMessage());
                    resp.setStatus(500);
                    return;
                }


                //Now that we've passed, go ahead and fill out the method and call it with the right params
                try {

                    Annotation[][] annotations = methodAndPath.method.getParameterAnnotations();

                    Object[] parameters = new Object[annotations.length];

                    int index = 0;
                    for (Annotation[] ann : annotations) {

                        if (ann.length > 1){
                            log.severe("Annotation error: Ballard does not support more than one annotation on an endpoint parameter.");
                            resp.setStatus(500);
                            return;
                        }
                        else if (ann.length < 1){
                            log.severe("Annotation error for "+methodAndPath.method.getName()+": Must have at least one BallardParameter to inject a parameter");
                        }

                        if (ann[0].annotationType().equals(BallardParameterExtraPath.class)){
                            parameters[index] = methodAndPath.path;
                        }
                        else if (ann[0].annotationType().equals(BallardParameterRawBody.class)){
                            parameters[index] = getBody(req);
                        }
                        else if (ann[0].annotationType().equals(BallardParameterSsoUser.class)){
                            parameters[index] = ssoUser;
                        }
                        else if (ann[0].annotationType().equals(BallardParameterAutoGson.class)){
                            Class parameterType = methodAndPath.method.getParameterTypes()[index];
                            String bodyString = getBody(req);

                            parameters[index] = new Gson().fromJson(bodyString,parameterType);
                        }
                        else{
                            log.severe("Annotation error for "+methodAndPath.method.getName()+": Must have at least one BallardParameter to inject a parameter");
                            resp.setStatus(500);
                            return;
                        }
                        index = index + 1;
                    }

                    Class returnType = methodAndPath.method.getReturnType();

                    String stringToOutput;
                    if (returnType.equals(String.class)){
                        stringToOutput = (String)methodAndPath.method.invoke(null,parameters);
                    }
                    else{
                        Object returnValue = methodAndPath.method.invoke(null,parameters);
                        stringToOutput = new Gson().toJson(returnValue,returnType);
                    }


                    resp.getWriter().print(stringToOutput);
                    resp.setStatus(200);
                    return;
                } catch (IllegalAccessException e) {
                    log.severe("Illegal access exception when trying to access method for " + req.getPathInfo() + ". Is your handler set to private?" + e.getLocalizedMessage());
                    resp.setStatus(500);
                    return;
                } catch (InvocationTargetException e) {
                    StringWriter writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter( writer );
                    e.getTargetException().printStackTrace( printWriter );
                    printWriter.flush();

                    String stackTrace = writer.toString();
                    log.severe("Exception while processing " + req.getPathInfo() +". " +  stackTrace);
                    resp.setStatus(500);
                    return;
                }
            }

        } catch (EndpointPermissionsException e) {
            log.severe("EndpointPermissionsException when trying to access method for " + req.getPathInfo() + ". Is your handler set to private?" + e.getLocalizedMessage());
            resp.setStatus(403);
        }



        resp.setStatus(404);
    }


    public static String getBody(HttpServletRequest request) throws IOException {

        String body;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

}
