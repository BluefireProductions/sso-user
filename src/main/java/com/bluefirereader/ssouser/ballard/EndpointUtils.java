package com.bluefirereader.ssouser.ballard;

import com.bluefirereader.ssouser.SsoUser;
import com.bluefirereader.ssouser.SsoUserUtils;
import com.bluefirereader.ssouser.ballard.endpoints.BallardEndpoint;
import com.bluefirereader.ssouser.ballard.endpoints.EndpointPermissionsException;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Timothy Jones on 12/2/2014.
 */
public class EndpointUtils {

    private static HashMap<String,Method> distributorEndpoints = new HashMap<>();
    private static HashMap<String,Method> wildcardEndpoints = new HashMap<>();

    public static void initializeEndpoints(String endpointsRootPackage){
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setUrls(
                        ClasspathHelper.forPackage(endpointsRootPackage) ).setScanners(
                        new MethodAnnotationsScanner() ) );

        Set<Method> ssoMethodList = reflections.getMethodsAnnotatedWith(BallardEndpoint.class);

        for (Method method : ssoMethodList){
            BallardEndpoint endpointDecorator = method.getAnnotation(BallardEndpoint.class);
            if (endpointDecorator.path().endsWith("*")){
                wildcardEndpoints.put(endpointDecorator.path().substring(0,endpointDecorator.path().length()-1),method);
            }
            else{
                distributorEndpoints.put(endpointDecorator.path(),method);
            }

        }
    }

    public static MethodAndPath getFunctionForPath(String path) throws EndpointPermissionsException {
            if (distributorEndpoints.get(path) != null){
                return new MethodAndPath(distributorEndpoints.get(path),null);
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
