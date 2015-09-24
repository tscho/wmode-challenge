package com.tschoend.wmodechallenge.security.oauthsignedfetch;

import com.google.common.base.Optional;
import com.tschoend.wmodechallenge.model.appdirect.User;
import com.tschoend.wmodechallenge.model.appdirect.constants.Role;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.HmacSha1MessageSigner;
import org.apache.commons.codec.binary.Base64;


/**
 * Created by tom on 2015-09-23.
 */
@Slf4j
public class OAuth1Authenticator implements Authenticator<OauthCredentials, User> {
    private static final String MAC_NAME = "HmacSHA1";

    private final String oauthKey;
    private final String oauthSecret;

    private final Base64 base64 = new Base64();

    public OAuth1Authenticator(String oauthKey, String oauthSecret) {
        this.oauthKey = oauthKey;
        this.oauthSecret = oauthSecret;
    }

    public static HttpParameters parseParameters(String header, String query) {
        HttpParameters params = new HttpParameters();

        log.info("Original Header: " + header);

        // Strip off "OAuth " authorization identifier
        String[] paramPairs = header.substring(6).split(",");

        for (String pair : paramPairs) {
            String[] keyValPair = pair.trim().split("=");
            log.info("Original pair: " + pair);

            // Strip off quotations around values
            String cleanedValue = keyValPair[1].substring(1, keyValPair[1].length() - 1);

            log.info("Parsed: " + keyValPair[0] + ": " + cleanedValue);
            if (keyValPair[0].startsWith("oauth")) {
                params.put(keyValPair[0], cleanedValue);
            }
        }

        paramPairs = query.split("&");
        for (String pair : paramPairs) {
            String[] keyValPair = pair.trim().split("=");
            params.put(keyValPair[0], keyValPair[1]);
        }

        return params;
    }

    @Override
    public Optional<User> authenticate(OauthCredentials oauthCredentials) throws AuthenticationException {
        try {
            HttpParameters parameters = parseParameters(oauthCredentials.getAuthHeader(), oauthCredentials.getQuery());

            MinimalHttpRequest request = new MinimalHttpRequest(oauthCredentials.getMethod(), oauthCredentials.getUrl());

            HmacSha1MessageSigner signer = new HmacSha1MessageSigner();
            signer.setConsumerSecret(this.oauthSecret);

            String expectedSignature = signer.sign(request, parameters);
            String requestSignature = OAuth.percentDecode(parameters.get(OAuth.OAUTH_SIGNATURE).first());

            log.info("Request signature: " + requestSignature);
            log.info("Computed signature: " + expectedSignature);

            if (expectedSignature.equals(requestSignature)) {
                User user = new User();
                user.setRole(Role.OAUTH_VERIFICATION);

                return Optional.of(user);
            }

            return Optional.absent();
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
    }
}
