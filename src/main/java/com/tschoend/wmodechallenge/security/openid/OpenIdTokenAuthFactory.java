package com.tschoend.wmodechallenge.security.openid;

import com.google.common.base.Optional;
import com.tschoend.wmodechallenge.dao.UserSessionDao;
import io.dropwizard.auth.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.UUID;

/**
 * Created by tom on 2015-09-27.
 */
@Slf4j
public class OpenIdTokenAuthFactory<T> extends AuthFactory<OpenIdTokenCredentials, T> {
    private final boolean required;
    private final Class<T> generatedClass;
    private final String realm;
    private UnauthorizedHandler unauthorizedHandler = new DefaultUnauthorizedHandler();

    @Context
    private HttpServletRequest request;

    public OpenIdTokenAuthFactory(final Authenticator<OpenIdTokenCredentials, T> authenticator,
                                  final String realm,
                                  final Class<T> generatedClass) {
        super(authenticator);
        this.required = false;
        this.generatedClass = generatedClass;
        this.realm = realm;
    }

    public OpenIdTokenAuthFactory(final boolean required,
                                  final Authenticator<OpenIdTokenCredentials, T> authenticator,
                                  final String realm,
                                  final Class<T> generatedClass) {
        super(authenticator);
        this.required = required;
        this.generatedClass = generatedClass;
        this.realm = realm;
    }

    public OpenIdTokenAuthFactory<T> responseBuilder(UnauthorizedHandler unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
        return this;
    }

    @Override
    public void setRequest(HttpServletRequest httpServletRequest) {
        this.request = httpServletRequest;
    }

    @Override
    public AuthFactory<OpenIdTokenCredentials, T> clone(boolean b) {
        return new OpenIdTokenAuthFactory<T>(b, authenticator(), realm, generatedClass);
    }

    @Override
    public Class<T> getGeneratedClass() {
        return generatedClass;
    }

    @Override
    public T provide() {
        UUID token = SessionUtil.getTokenForRequest(this.request);

        if(token != null) {
            try {
                OpenIdTokenCredentials credentials = new OpenIdTokenCredentials(token);

                final Optional<T> result = authenticator().authenticate(credentials);

                if(result.isPresent()) {
                    return result.get();
                }
            } catch (AuthenticationException e) {
                log.warn("Error authenticating OpenID token", e);
            }
        }

        if(required) {
            throw new WebApplicationException("Failed to authorize", Response.Status.UNAUTHORIZED);
        }

        return null;
    }
}
