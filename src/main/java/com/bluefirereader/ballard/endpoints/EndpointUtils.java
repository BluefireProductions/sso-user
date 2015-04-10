package com.bluefirereader.ballard.endpoints;

import com.bluefirereader.ballard.SsoUser;
import com.bluefirereader.ballard.SsoUserUtils;
import com.bluefirereader.ballard.endpoints.security.BallardEndpoint;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Timothy Jones on 12/2/2014.
 */
public class EndpointUtils {

    private static final Logger log = Logger.getLogger(JsonServlet.class.getName());

    private static HashMap<String,Method> exactEndpoints = new HashMap<>();
    private static HashMap<String,Method> wildcardEndpoints = new HashMap<>();

    public static void initializeEndpoints(String endpointsRootPackage){
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setUrls(
                        ClasspathHelper.forPackage(endpointsRootPackage) ).setScanners(
                        new MethodAnnotationsScanner() ) );

        Set<Method> ssoMethodList = reflections.getMethodsAnnotatedWith(BallardEndpoint.class);

        for (Method method : ssoMethodList){
            if (!Modifier.isStatic(method.getModifiers())){
                log.severe("Ignoring invalid endpoint: " + method.getName() + " - BallardEndpoints must be static.");
                continue;
            }
            BallardEndpoint endpointDecorator = method.getAnnotation(BallardEndpoint.class);
            if (endpointDecorator.path().endsWith("*")){
                wildcardEndpoints.put(endpointDecorator.path().substring(0,endpointDecorator.path().length()-1),method);
            }
            else{
                exactEndpoints.put(endpointDecorator.path(), method);
            }

        }
    }

    public static MethodAndPath getFunctionForPath(String path) throws EndpointPermissionsException {
            if (exactEndpoints.get(path) != null){
                return new MethodAndPath(exactEndpoints.get(path),null);
            }
            else{
                for (String wildcardKey : wildcardEndpoints.keySet()){
                    if (path.startsWith(wildcardKey)){
                        String extraPath = path.substring(wildcardKey.length());
                        return new MethodAndPath(wildcardEndpoints.get(wildcardKey),extraPath);
                    }
                }
            }
        return null;

    }

    public static SsoUser getSsoUser(HttpServletRequest req) throws Exception {
        return SsoUserUtils.getInstance().getUser(req);

    }

}
