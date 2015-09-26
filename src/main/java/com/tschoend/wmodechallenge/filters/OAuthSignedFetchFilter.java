package com.tschoend.wmodechallenge.filters;

import lombok.extern.slf4j.Slf4j;
import oauth.signpost.OAuth;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.HmacSha1MessageSigner;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by tom on 2015-09-26.
 */
@Slf4j
public class OAuthSignedFetchFilter implements ContainerRequestFilter {
    private final String oauthKey;
    private final String oauthSecret;

    public OAuthSignedFetchFilter(String oauthKey, String oauthSecret) {
        this.oauthKey = oauthKey;
        this.oauthSecret = oauthSecret;
    }

    public static HttpParameters parseParameters(String header, MultivaluedMap<String, String> query) {
        HttpParameters params = new HttpParameters();

        log.info("Original Header: " + header);

        // Strip off "OAuth " authorization identifier
        String[] paramPairs = header.substring(6).split(",");

        for (String pair : paramPairs) {
            String[] keyValPair = pair.trim().split("=");
            log.debug("Original pair: " + pair);

            // Strip off quotations around values
            String cleanedValue = keyValPair[1].substring(1, keyValPair[1].length() - 1);

            log.debug("Parsed: " + keyValPair[0] + ": " + cleanedValue);
            if (keyValPair[0].startsWith("oauth")) {
                params.put(keyValPair[0], cleanedValue);
            }
        }

        for (Map.Entry<String, List<String>> pair : query.entrySet()) {
            params.put(pair.getKey(), new TreeSet<>(pair.getValue()));
        }

        return params;
    }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        try {
            if (request != null) {
                final String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if (header != null) {
                    UriInfo uriInfo = request.getUriInfo();

                    String method = request.getMethod();
                    String url = uriInfo.getBaseUri() + uriInfo.getPath();
                    MultivaluedMap<String, String> query = request.getUriInfo().getQueryParameters();

                    HttpParameters parameters = parseParameters(header, query);

                    MinimalHttpRequest signedRequest = new MinimalHttpRequest(method, url);

                    HmacSha1MessageSigner signer = new HmacSha1MessageSigner();
                    signer.setConsumerSecret(this.oauthSecret);

                    String expectedSignature = signer.sign(signedRequest, parameters);
                    String requestSignature = OAuth.percentDecode(parameters.get(OAuth.OAUTH_SIGNATURE).first());

                    log.info("Request signature: <" + requestSignature + ">");
                    log.info("Computed signature: <" + expectedSignature + ">");

                    if (expectedSignature.equals(requestSignature)) {
                        log.info("Signature verified");

                        return;
                    }

                    log.warn("Signature failed verification");
                }
            }
        } catch (Exception e) {
            log.warn("Error authenticating credentials", e);
            throw new InternalServerErrorException();
        }

        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
}
