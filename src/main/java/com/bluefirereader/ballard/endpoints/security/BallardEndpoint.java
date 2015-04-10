package com.bluefirereader.ballard.endpoints.security;


import com.bluefirereader.ballard.endpoints.security.AllowEverythingChecker;
import com.bluefirereader.ballard.endpoints.security.SecurityChecker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Timothy Jones on 12/2/2014.
 *
 * By tagging a function with @AdminEndpoint and putting it within the endpoints/ subfolder, you make it
 * available for an authenticated http POST endpoint at /endpoints/admin/<path()>
 *
 * Note that the endpoints system will verify that the user is an admin before forwarding the request to
 * the endpoint so no further checking is necessary unless supra-admin permissions are needed.
 *
 * @method path - the path (Relative to the Authenticated Servlet) for this endpoint to consume.
 *
 * @method securityChecker - (Optional) - A class implementing @link com.bluefirereader.ballard.ballard.security.SecurityChecker
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface BallardEndpoint {
    String path();
    Class<? extends SecurityChecker> securityChecker() default AllowEverythingChecker.class;
}
