package com.bluefirereader.ssouser;

import java.util.Date;

/**
 * Created by Timothy Jones on 6/10/14.
 */
public class SsoUser {
    private String id;
    private String email;
    private String name;
    private String tokenType;
    private String socialId;
    private Date createdAt;
    private Date expires;

    public String getId() {
        return id;
    }

    public SsoUser setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SsoUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public SsoUser setName(String name) {
        this.name = name;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public SsoUser setTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getSocialId() {
        return socialId;
    }

    public SsoUser setSocialId(String socialId) {
        this.socialId = socialId;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public SsoUser setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getExpires() {
        return expires;
    }

    public SsoUser setExpires(Date expires) {
        this.expires = expires;
        return this;
    }
}
