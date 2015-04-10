#Overview

This is a library to automate the interaction between Java servlets and Bluefire Single Sign On tokens.
It provides tooling to enable you to automatically authenticate a server by HTTPRequest or by
JWT string.

Additionally this library provides a JAX-RS style endpoints library that acts as a thin layer of middleware
between servlets and Endpoint functions


#Usage

Now that you have included the library, the next step is validating a servlet's request:

       SsoUserUtils userUtils;
       
       try {
           userUtils = SsoUserUtils.getInstance();
       } catch (Exception e) {
           log.severe("Error instantiating userUtils: " + e.getLocalizedMessage());
           e.printStackTrace();
           return;

       }

       SsoUser ssoUser = userUtils.GetUser(req);
       
At this point, your ssoUser should contain the user's id and email among other useful attributes and 
be fully validated.  If validation fails at any point, SsoUserUtils::getUser will return null or 
throw an explanatory exception.