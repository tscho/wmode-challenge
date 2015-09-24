package com.tschoend.wmodechallenge.filters;

import lombok.extern.slf4j.Slf4j;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.HmacSha1MessageSigner;
import oauth.signpost.signature.OAuthMessageSigner;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Request;

/**
 * Created by tom on 2015-09-21.
 */

@Slf4j
public class OAuthVerifier {
    private final OAuthMessageSigner signer;

    public OAuthVerifier(String oauthKey, String oauthSecret) {
        signer = new HmacSha1MessageSigner();
        signer.setConsumerSecret(oauthSecret);
    }

    public boolean verify(ContainerRequestContext request) {
        String authHeader = request.getHeaderString("authorization");
        log.info(authHeader);

        return true;
    }
}
