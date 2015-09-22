package com.tschoend.wmodechallenge.filters;

import com.sun.jersey.oauth.signature.*;

/**
 * Created by tom on 2015-09-21.
 */

public class OAuthProvider {
    private final OAuthParameters parameters;
    private final OAuthSecrets secrets;

    public OAuthProvider(String oauthKey, String oauthSecret) {
        parameters = new OAuthParameters()
                .consumerKey(oauthKey)
                .nonce().timestamp().signatureMethod(HMAC_SHA1.NAME);
        secrets = new OAuthSecrets().
                consumerSecret(oauthSecret);
    }

    public void sign(OAuthRequest request) throws Exception {
        OAuthSignature.sign(request, parameters, secrets);
    }

    public boolean verify(OAuthRequest request) throws Exception {
        return OAuthSignature.verify(request, parameters, secrets);
    }
}
