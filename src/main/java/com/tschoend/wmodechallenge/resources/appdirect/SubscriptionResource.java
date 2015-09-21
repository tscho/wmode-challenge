package com.tschoend.wmodechallenge.resources.appdirect;

import com.tschoend.wmodechallenge.model.AppDirectResult;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by tom on 2015-09-20.
 */
@Path("/appdirect/subscription")
public class SubscriptionResource {
    @Path("/create/")
    @Produces(MediaType.APPLICATION_XML)
    public AppDirectResult createEvent(@QueryParam("eventUrl") String eventUrl) {
        return new AppDirectResult();
    }
}
