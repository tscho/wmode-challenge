package com.tschoend.wmodechallenge.resources.appdirect;

import com.tschoend.wmodechallenge.client.AppDirectAuthorizedClient;
import com.tschoend.wmodechallenge.dao.AccountDao;
import com.tschoend.wmodechallenge.dao.UserDao;
import com.tschoend.wmodechallenge.model.appdirect.Account;
import com.tschoend.wmodechallenge.model.appdirect.User;
import com.tschoend.wmodechallenge.model.appdirect.constants.AppDirectErrorCode;
import com.tschoend.wmodechallenge.model.appdirect.constants.EventFlag;
import com.tschoend.wmodechallenge.model.appdirect.constants.Role;
import com.tschoend.wmodechallenge.model.appdirect.dto.AppDirectResultBean;
import com.tschoend.wmodechallenge.model.appdirect.dto.EventBean;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
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
    private final AccountDao accountDao;
    private final UserDao userDao;

    public SubscriptionEventResource(AppDirectAuthorizedClient client, AccountDao accountDao, UserDao userDao) {
        this.client = client;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GET
    @Path("/create/")
    @Produces(MediaType.APPLICATION_XML)
    @RolesAllowed({Role.OAUTH_VERIFICATION})
    @UnitOfWork
    public AppDirectResultBean createEvent(@Auth User user, @QueryParam("url") String eventUrl) {
        if (user == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        EventBean event = client.getEvent(eventUrl);

        if(event == null) {
            return new AppDirectResultBean(false, "Failed to fetch event details", null, AppDirectErrorCode.UNKNOWN_ERROR);
        }

        log.info("Event type: " + event.getType().name());

        if(event.getFlag() == EventFlag.STATELESS) {
            return new AppDirectResultBean(true, eventUrl, 1l, null);
        }

        User admin = User.fromUserBean(event.getCreator(), Role.ADMIN);

        admin = userDao.save(admin);

        Account account = Account.fromOrderBean(event.getPayload().getOrder());
        account.addUser(admin);

        Account persistedAccount = accountDao.save(account);

        return new AppDirectResultBean(true, "Created subscription", persistedAccount.getAccountIdentifier(), null);
    }

    @GET
    @Path("/cancel/")
    @Produces(MediaType.APPLICATION_XML)
    @RolesAllowed({Role.OAUTH_VERIFICATION})
    @UnitOfWork
    public AppDirectResultBean deleteEvent(@Auth User user, @QueryParam("url") String eventUrl) {
        if(user == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        EventBean event = client.getEvent(eventUrl);

        if(event == null) {
            return new AppDirectResultBean(false, "Failed to fetch event details", null, AppDirectErrorCode.UNKNOWN_ERROR);
        }

        log.info("Event type: " + event.getType().name());


        if(event.getFlag() == EventFlag.STATELESS) {
            return new AppDirectResultBean(true, eventUrl, 1l, null);
        }

        long identifier = event.getPayload().getAccount().getAccountIdentifier();
        log.info("Identifier: " + identifier);

        accountDao.delete(identifier);

        return new AppDirectResultBean(true, "Cancelled subscription", identifier, null);
    }
}
