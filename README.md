#Overview

This is a library to automate the interaction be Java servlets and Bluefire Single Sign On tokens.
It provides tooling to enable you to automatically authenticate a server by HTTPRequest or by
JWT string.

#Getting Started

The recommended way to install this library is as a maven dependency. This project is published to 
Box/Developers/Maven/sso-user/.  So, in order to use this location as a maven repository, you
will need to sync your Box/Developers to a local folder and then add that folder as a local
repository.  For example: 

        <repositories>  
                <repository>            
                        <id>Box</id>
                        <url>file:////Users/bluefire/Box Sync/Developers/Maven/sso-user/</url>                        
                </repository>
        </repositories>
        
Note that for portability, this step is done in the user's local maven settings.xml (typically 
~/.m2/settings.xml) instead of the project.

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