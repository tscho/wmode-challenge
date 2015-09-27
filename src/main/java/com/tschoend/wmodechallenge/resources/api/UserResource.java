package com.tschoend.wmodechallenge.resources.api;

import com.tschoend.wmodechallenge.model.appdirect.entity.User;
import com.tschoend.wmodechallenge.model.appdirect.entity.UserSession;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by tom on 2015-09-27.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    @GET
    @UnitOfWork
    public User getUser(@Auth UserSession session) {
        return session.getUser();
    }
}
