package com.tschoend.wmodechallenge.resources.appdirect;

import com.tschoend.wmodechallenge.model.appdirect.User;
import com.tschoend.wmodechallenge.model.appdirect.constants.Role;
import com.tschoend.wmodechallenge.model.appdirect.dto.AppDirectResultBean;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tom on 2015-09-20.
 */
@Path("/appdirect/subscription")
public class SubscriptionEventResource {
    @GET
    @Path("/create/")
    @Produces(MediaType.APPLICATION_XML)
    @RolesAllowed({Role.OAUTH_VERIFICATION})
    public AppDirectResultBean createEvent(@Auth User user, @QueryParam("url") String eventUrl) {
        if (user == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        return new AppDirectResultBean(true, eventUrl, 1l, null);
    }
}
