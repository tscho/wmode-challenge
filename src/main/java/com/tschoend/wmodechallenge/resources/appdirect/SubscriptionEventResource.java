package com.tschoend.wmodechallenge.resources.appdirect;

import com.sun.jersey.oauth.signature.OAuthRequest;
import com.tschoend.wmodechallenge.filters.OAuthProvider;
import com.tschoend.wmodechallenge.model.appdirect.dto.AppDirectResultBean;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tom on 2015-09-20.
 */
@Path("/appdirect/subscription")
public class SubscriptionEventResource {
    private final OAuthProvider provider;

    public SubscriptionEventResource(OAuthProvider provider) {
        this.provider = provider;
    }

    @Path("/create/")
    @Produces(MediaType.APPLICATION_XML)
    public AppDirectResultBean createEvent(
            @Context OAuthRequest request,
            @QueryParam("eventUrl") String eventUrl)
            throws Exception{
        if(!provider.verify(request)) {
            throw new WebApplicationException("Request not verified", Response.Status.UNAUTHORIZED);
        }

        return new AppDirectResultBean();
    }
}
