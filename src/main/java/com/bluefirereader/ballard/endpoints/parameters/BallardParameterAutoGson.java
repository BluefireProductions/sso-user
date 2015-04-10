package com.bluefirereader.ballard.endpoints.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Timothy Jones on 3/11/2015.
 *
 * By tagging an endpoint function with SsoUser, that parameter will be injected with the current SsoUser at
 * runtime.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)
public @interface BallardParameterAutoGson {
}
