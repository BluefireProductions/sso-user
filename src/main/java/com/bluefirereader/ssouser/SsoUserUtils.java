package com.bluefirereader.ssouser;

import com.google.common.io.BaseEncoding;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

public class SsoUserUtils
{

    private static final Logger log = Logger.getLogger(SsoUserUtils.class.getName());

    private static SsoUserUtils instance;

    RSAPublicKey publicKey = null;

    private SsoUserUtils() throws Exception {


        String base64PublicKey = getText("http://bluefire-sso.appspot.com/cert");

        byte[] base64PublicKeyBytes = BaseEncoding.base64().decode(base64PublicKey);

        publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(base64PublicKeyBytes));

    }

    public static SsoUserUtils getInstance(){
        if (instance == null)
        {
            try {
                instance = new SsoUserUtils();
            } catch (Exception e) {
                log.severe("Algorithm Exception: " + e.getLocalizedMessage());
                return null;
            }
        }
        return instance;
    }

    public SsoUser getUser(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }

        String tokenString = authHeader.substring("Bearer ".length());

        return getUserFromJWT(tokenString);
    }

    public SsoUser getUserFromJWT(String tokenString)
    {
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(tokenString);
        } catch (ParseException e) {
            //invalid JWT, return null;
            return null;
        }

        boolean verified = false;
        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        try {
            verified = signedJWT.verify(verifier);

        } catch (JOSEException e) {
            return null;
        }

        if (!verified)
            return null;

        SsoUser returnUser = new SsoUser();

        try {
            returnUser.setId(signedJWT.getJWTClaimsSet().getSubject());
            returnUser.setEmail(signedJWT.getJWTClaimsSet().getStringClaim("email"));
            returnUser.setName(signedJWT.getJWTClaimsSet().getStringClaim("name"));
            returnUser.setSocialId(signedJWT.getJWTClaimsSet().getStringClaim("social-id"));
            returnUser.setTokenType(signedJWT.getJWTClaimsSet().getStringClaim("token-type"));
            returnUser.setCreatedAt(signedJWT.getJWTClaimsSet().getIssueTime());
            returnUser.setExpires(signedJWT.getJWTClaimsSet().getExpirationTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        //Final check is expiration date.
        if (returnUser.getExpires() != null && new Date().getTime() > returnUser.getExpires().getTime()) {
            return returnUser;
        }

        // If necessary in the future, we can also reject on IAT that is too old, but for now
        // that seems like overkill.

        return null;


    }

    public static String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }

}