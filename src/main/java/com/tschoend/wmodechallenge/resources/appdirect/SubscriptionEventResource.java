package com.tschoend.wmodechallenge.resources.appdirect;

import com.tschoend.wmodechallenge.client.AppDirectAuthorizedClient;
import com.tschoend.wmodechallenge.model.appdirect.User;
import com.tschoend.wmodechallenge.model.appdirect.constants.AppDirectErrorCode;
import com.tschoend.wmodechallenge.model.appdirect.constants.Role;
import com.tschoend.wmodechallenge.model.appdirect.dto.AppDirectResultBean;
import com.tschoend.wmodechallenge.model.appdirect.dto.EventBean;
import io.dropwizard.auth.Auth;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tom on 2015-09-20.
 */
@Slf4j
@Path("/appdirect/subscription")
public class SubscriptionEventResource {
    private final AppDirectAuthorizedClient client;

    public SubscriptionEventResource(AppDirectAuthorizedClient client) {
        this.client = client;
    }

    @GET
    @Path("/create/")
    @Produces(MediaType.APPLICATION_XML)
    @RolesAllowed({Role.OAUTH_VERIFICATION})
    public AppDirectResultBean createEvent(@Auth User user, @QueryParam("url") String eventUrl) {
        if (user == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        EventBean eventInfo = client.getEvent(eventUrl);

        if(eventInfo == null) {
            return new AppDirectResultBean(false, "Failed to fetch event details", null, AppDirectErrorCode.UNAUTHORIZED);
        }

        log.info("Event type: " + eventInfo.getType().name());
        log.info("Admin UUID: " + eventInfo.getCreator().getUuid());

        return new AppDirectResultBean(true, eventUrl, 1l, null);
    }
}
