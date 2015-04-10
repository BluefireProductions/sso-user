package com.bluefirereader.ballard.endpoints;

import java.lang.reflect.Method;

/**
 * Created by Timothy Jones on 1/14/15.
 */
public class MethodAndPath {

    public MethodAndPath(Method method, String path){
        this.method = method;
        this.path = path;
    }

    public Method method;
    public String path;
}
