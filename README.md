#Overview

This is a library for automating modern client server interactions where user authentication is based on
Open ID Connect style authentication tokens encoded in JWT tokens and where the primary client server interaction
protocol is JSON.

The Ballard library provides a JAX-RS style endpoints library that acts as a thin layer of middleware
between servlets and Endpoint functions.  This enables you to ignore the repetitive Servlet handshaking and concentrate
on your business logic. Now when exposing REST endpoints you simply write them as if they were standard Java functions
to be called within Java but decorate them to explain how they should be used in a server context:

    @BallardEndpoint(path="user/updateUserDescription", securityChecker = AllowAuthenticatedChecker.class)
    	public static String updateUserDescription(@BallardParameterSsoUser SsoUser user, @BallardParameterRawBody String payload){
    	   Business logic to update the user description...
    	}


Out of the box, the Ballard library provides a validation mechanism for Open ID Connect style authentication tokens
based on a public key server that you provide.  If you wish to add on your own security mechanisms this security
checking is easily extensible by creating a class with implements the SecurityChecker interface assigning that class
as an endpoint checker.

    @BallardEndpoint(path = "admin/deleteUser", securityChecker = AdminChecker.class)

#Getting Started

In order to use Servlet Mapping, you will need to set up a JsonServlet (provided) and map it to a wildcard
server pattern.  You will need to pass in two parameters, one specifying the where to find the @BallardEndpoints
functions and another specifying how to get the public key to validate your JWTs securely.

##First Parameter: endpointsRootPackage

The endpointsRootPackage should be the highest level package name that includes all of your endpoints.  Ballard
will look through every class below that package searching for functions with the @BallardEndpoint annotation so
it is best to separate out all of your endpoints into a unique package for performance reasons.

##Second Parameter: publicKeyLocation OR publicKey

In order to validate your JWT tokens, you need to provide a public key.  This can be done by either providing the
key itself, encoded as Base64 in the publicKey parameter OR by providing a URL for the key to be fetched in the
publickKeyLocation parameter.


Example:

    <servlet>
        <servlet-name>authenticatedServlet</servlet-name>
        <servlet-class>com.bluefirereader.ballard.endpoints.JsonServlet
        </servlet-class>
        <init-param>
            <param-name>endpointsRootPackage</param-name>
            <param-value>com.yourapp.endpoints</param-value>
        </init-param>
        <init-param>
            <param-name>publicKeyLocation</param-name>
            <param-value>http://yourapp.com/cert</param-value>
        </init-param>
    </servlet>

    <servlet-mapping>
        <servlet-name>authenticatedServlet</servlet-name>
        <url-pattern>/dadc/*</url-pattern>
    </servlet-mapping>
