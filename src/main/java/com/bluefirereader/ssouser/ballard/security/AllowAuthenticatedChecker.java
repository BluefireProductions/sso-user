package com.bluefirereader.ssouser.ballard.security;

import com.bluefirereader.ssouser.SsoUser;

/**
 * Created by Timothy Jones on 3/20/2015.
 * Allows any user that has been authenticated.
 */
public class AllowAuthenticatedChecker implements SecurityChecker {

    @Override
    public boolean isAllowed(SsoUser user, String path) {
        if (user != null){
            return true;
        }
        return false;
    }
}
