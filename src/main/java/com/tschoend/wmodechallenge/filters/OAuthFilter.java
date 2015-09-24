package com.tschoend.wmodechallenge.filters;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by tom on 2015-09-22.
 */
@Slf4j
@Priority(Priorities.AUTHORIZATION)
public class OAuthFilter implements ContainerRequestFilter {
    private final OAuthVerifier provider;

    public OAuthFilter(OAuthVerifier provider) {
        this.provider = provider;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        Request request = containerRequestContext.getRequest();
            if (provider.verify(containerRequestContext)) {
                return;
            }

        throw new WebApplicationException("OAuth verify failed", Response.Status.UNAUTHORIZED);
    }
}
