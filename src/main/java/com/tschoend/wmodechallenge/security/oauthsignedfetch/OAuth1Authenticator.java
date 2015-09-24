package com.tschoend.wmodechallenge.security.oauthsignedfetch;

import com.google.common.base.Optional;
import com.tschoend.wmodechallenge.model.appdirect.User;
import com.tschoend.wmodechallenge.model.appdirect.constants.Role;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.HmacSha1MessageSigner;
import oauth.signpost.signature.SignatureBaseString;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;

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

    @Override
    public Optional<User> authenticate(OauthCredentials oauthCredentials) throws AuthenticationException {
        try {
            HttpParameters parameters = parseParameters(oauthCredentials.getAuthHeader(), oauthCredentials.getQuery());

            MinimalHttpRequest request = new MinimalHttpRequest(oauthCredentials.getMethod(), oauthCredentials.getUrl());
//
//            String base = new SignatureBaseString(request, parameters).generate()
//                    + OAuth.percentEncode(oauthCredentials.getQuery());

//            String base = generateBaseSigningString(
//                    oauthCredentials.getMethod(),
//                    oauthCredentials.getUrl(),
//                    parameters);
//
//            log.info("OAuth base " + base);
//
//            byte[] keyBytes = (OAuth.percentEncode(oauthSecret) + "&").getBytes(OAuth.ENCODING);
//
//            SecretKey key = new SecretKeySpec(keyBytes, MAC_NAME);
//            Mac mac = Mac.getInstance(MAC_NAME);
//            mac.init(key);
//
//            byte[] text = base.getBytes(OAuth.ENCODING);
//
//            String expectedSignature = new String(base64.encode(mac.doFinal(text))).trim();


            HmacSha1MessageSigner signer = new HmacSha1MessageSigner();
            signer.setConsumerSecret(this.oauthSecret);

            String expectedSignature = signer.sign(request, parameters);
            String requestSignature = OAuth.percentDecode(parameters.get(OAuth.OAUTH_SIGNATURE).first());

            log.info("Request signature: " + requestSignature);
            log.info("Computed signature: " + expectedSignature);

            if(expectedSignature.equals(requestSignature)) {
                User user = new User();
                user.setRole(Role.OAUTH_VERIFICATION);

                return Optional.of(user);
            }

            return Optional.absent();
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }




    }

    /**
     * Based on the Signpost normalization code: https://github.com/mttkay/signpost/blob/1.2.1.2/signpost-core/src/main/java/oauth/signpost/signature/SignatureBaseString.java
     */

    public static String generateBaseSigningString(String method, String url, HttpParameters parameters) throws AuthenticationException {

        try {
            String normalizedUrl = normalizeRequestUrl(url);
            String normalizedParams = normalizeRequestParameters(parameters);

            return method + '&' + OAuth.percentEncode(normalizedUrl) + '&'
                    + OAuth.percentEncode(normalizedParams);
        } catch (Exception e) {
            throw new AuthenticationException(e);
        }
    }

    public static String normalizeRequestUrl(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String scheme = uri.getScheme().toLowerCase();
        String authority = uri.getAuthority().toLowerCase();
        boolean dropPort = (scheme.equals("http") && uri.getPort() == 80)
                || (scheme.equals("https") && uri.getPort() == 443);
        if (dropPort) {
            // find the last : in the authority
            int index = authority.lastIndexOf(":");
            if (index >= 0) {
                authority = authority.substring(0, index);
            }
        }
        String path = uri.getRawPath();
        if (path == null || path.length() <= 0) {
            path = "/"; // conforms to RFC 2616 section 3.2.2
        }
        // we know that there is no query and no fragment here.
        return scheme + "://" + authority + path;
    }

    public static String normalizeRequestParameters(HttpParameters requestParameters) {
        StringBuilder sb = new StringBuilder();

        Iterator<String> iter = requestParameters.keySet().iterator();

        for (int i = 0; iter.hasNext(); i++) {
            String param = iter.next();

            if (OAuth.OAUTH_SIGNATURE.equals(param)) {
                continue;
            }

            if (i > 0) {
                sb.append("&");
            }

            // fix contributed by Stjepan Rajko
            // since param should already be encoded, we supply false for percentEncode
            sb.append(requestParameters.getAsQueryString(param, false));
        }
        return sb.toString();
    }

    public static HttpParameters parseParameters(String header, String query) {
        HttpParameters params = new HttpParameters();

        log.info("Original Header: " + header);

        // Strip off "OAuth " authorization identifier
        String[] paramPairs = header.substring(6).split(",");

        for(String pair : paramPairs) {
            String[] keyValPair = pair.trim().split("=");
            log.info("Original pair: " + pair);

            // Strip off quotations around values
            String cleanedValue = keyValPair[1].substring(1, keyValPair[1].length() - 1);

            log.info("Parsed: " + keyValPair[0] + ": " + cleanedValue);
            if(keyValPair[0].startsWith("oauth")) {
                params.put(keyValPair[0], cleanedValue);
            }
        }

        paramPairs = query.split("&");
        for(String pair : paramPairs) {
            String[] keyValPair = pair.trim().split("=");
            params.put(keyValPair[0], keyValPair[1]);
        }

        return params;
    }
}
