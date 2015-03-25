package com.bluefirereader.ssouser.ballard.parameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Timothy Jones on 3/11/2015.
 *
 * By tagging an wildcard endpoint function with ExtraPath, that parameter will be injected with the current
 * any extra path that was greedily consumed at runtime.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)
public @interface BallardParameterRawBody {
}
