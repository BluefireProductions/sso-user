package com.bluefirereader.ssouser.ballard.security;

import com.bluefirereader.ssouser.SsoUser;

/**
 * Created by Timothy Jones on 3/13/2015.
 */
public class AllowEverythingChecker implements SecurityChecker {

    @Override
    public boolean isAllowed(SsoUser user, String path) {
        return true;
    }
}
