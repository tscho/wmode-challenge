package com.tschoend.wmodechallenge.security.oauthsignedfetch;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 * Created by tom on 2015-09-23.
 * <p/>
 * Based on the OAuth2 example at https://github.com/dropwizard/dropwizard/blob/release/0.8.x/dropwizard-auth/src/main/java/io/dropwizard/auth/oauth/OAuthFactory.java
 */
@Slf4j
public class OAuth1Factory<T> extends AuthFactory {
    private final boolean required;
    private final Class<T> generatedClass;
    private final String realm;
    @Context
    private HttpServletRequest request;

    public OAuth1Factory(Authenticator<OauthCredentials, T> authenticator,
                         Class<T> generatedClass,
                         String realm,
                         boolean required) {
        super(authenticator);
        this.generatedClass = generatedClass;
        this.realm = realm;
        this.required = required;
    }

    public OAuth1Factory(Authenticator<OauthCredentials, T> authenticator,
                         Class<T> generatedClass,
                         String realm) {
        super(authenticator);
        this.generatedClass = generatedClass;
        this.realm = realm;
        this.required = false;
    }

    @Override
    public void setRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    @Override
    public AuthFactory clone(boolean required) {
        return new OAuth1Factory<T>(authenticator(), this.generatedClass, this.realm, required);
    }

    @Override
    public Class getGeneratedClass() {
        return generatedClass;
    }

    @Override
    public T provide() {
        try {
            if (request != null) {
                final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

                if (header != null) {
                    OauthCredentials credentials = new OauthCredentials(
                            request.getMethod(),
                            request.getRequestURL().toString(),
                            request.getQueryString(),
                            header);

                    final Optional<T> result = authenticator()
                            .authenticate(credentials);

                    if (result.isPresent()) {
                        return result.get();
                    }
                }
            }
        } catch (AuthenticationException e) {
            log.warn("Error authenticating credentials", e);
            throw new InternalServerErrorException();
        }

        if (required) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        return null;
    }
}
