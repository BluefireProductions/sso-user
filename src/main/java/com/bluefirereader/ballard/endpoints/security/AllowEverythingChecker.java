package com.bluefirereader.ballard.endpoints.security;

import com.bluefirereader.ballard.SsoUser;

/**
 * Created by Timothy Jones on 3/13/2015.
 */
public class AllowEverythingChecker implements SecurityChecker {

    @Override
    public boolean isAllowed(SsoUser user, String path) {
        return true;
    }
}
