package com.bluefirereader.ballard.test.endpoints;

import com.bluefirereader.ballard.endpoints.security.BallardEndpoint;

/**
 * Created by Timothy Jones on 4/9/15.
 */
public class TestEndpoints {

    @BallardEndpoint(path = "/good")
    public static String goodEndpoint(){
        return "good";
    }

    @BallardEndpoint(path = "/bad")
    public String badEndpoint(){
        return "bad";
    }


}
