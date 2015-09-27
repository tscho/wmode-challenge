package com.tschoend.wmodechallenge.resources.appdirect;

import com.tschoend.wmodechallenge.client.AppDirectAuthorizedClient;
import com.tschoend.wmodechallenge.dao.AccountDao;
import com.tschoend.wmodechallenge.dao.UserDao;
import com.tschoend.wmodechallenge.filters.OAuthSigned;
import com.tschoend.wmodechallenge.model.appdirect.entity.Account;
import com.tschoend.wmodechallenge.model.appdirect.entity.User;
import com.tschoend.wmodechallenge.model.appdirect.constants.AppDirectErrorCode;
import com.tschoend.wmodechallenge.model.appdirect.constants.EventFlag;
import com.tschoend.wmodechallenge.model.appdirect.constants.Role;
import com.tschoend.wmodechallenge.model.appdirect.dto.AppDirectResultBean;
import com.tschoend.wmodechallenge.model.appdirect.dto.EventBean;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

/**
 * Created by tom on 2015-09-20.
 */
@Slf4j
@Path("/appdirect/event")
public class EventResource {
    private final AppDirectAuthorizedClient client;
    private final AccountDao accountDao;
    private final UserDao userDao;

    public EventResource(AppDirectAuthorizedClient client, AccountDao accountDao, UserDao userDao) {
        this.client = client;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @OAuthSigned
    @UnitOfWork
    public AppDirectResultBean createEvent(@QueryParam("url") String eventUrl) {
        EventBean event;
        try {
            event = client.getEvent(eventUrl);
        } catch (Exception e) {
            return new AppDirectResultBean(false, e.getMessage(), null, AppDirectErrorCode.UNKNOWN_ERROR);
        }

        if(event == null) {
            return new AppDirectResultBean(false, "Failed to fetch event details", null, AppDirectErrorCode.UNKNOWN_ERROR);
        }

        log.info("Event type: " + event.getType().name());

        if(event.getFlag() == EventFlag.STATELESS) {
            return new AppDirectResultBean(true, "Ping response", "0", null);
        }

        switch (event.getType()) {
            case SUBSCRIPTION_ORDER:
                return handleSubscriptionOrder(event);
            case SUBSCRIPTION_CANCEL:
                return handleSubscriptionCancel(event);
            case SUBSCRIPTION_CHANGE:
                return handleSubscriptionChange(event);
            case USER_ASSIGNMENT:
                return handleAssignUser(event);
            case USER_UNASSIGNMENT:
                return handleUnassignUser(event);
            default:
                return new AppDirectResultBean(false, "Failed to handle event", null, AppDirectErrorCode.UNKNOWN_ERROR);
        }
    }

    private AppDirectResultBean handleSubscriptionOrder(EventBean event) {
        User admin = User.fromUserBean(event.getCreator(), Role.ADMIN);

        admin = userDao.save(admin);

        Account account = Account.fromEventBean(
                event.getPayload().getOrder(),
                event.getMarketplace(),
                event.getPayload().getCompany());

        account.addUser(admin);

        account = accountDao.save(account);

        return new AppDirectResultBean(true, "Created subscription", Long.toString(account.getAccountIdentifier()), null);
    }

    private AppDirectResultBean handleSubscriptionChange(EventBean event) {
        if(event.getPayload() == null
                || event.getPayload().getAccount() == null
                || event.getPayload().getAccount().getAccountIdentifier() == null)
            return new AppDirectResultBean(false, "Missing account parameters", null, AppDirectErrorCode.INVALID_RESPONSE);

        if(event.getPayload().getOrder() == null)
            return new AppDirectResultBean(false, "Missing account parameters", null, AppDirectErrorCode.INVALID_RESPONSE);

        long identifier = Long.parseLong(event.getPayload().getAccount().getAccountIdentifier());

        Account account = accountDao.getAccount(identifier);

        if(account == null) {
            return new AppDirectResultBean(false, "Account not found", event.getPayload().getAccount().getAccountIdentifier(), AppDirectErrorCode.ACCOUNT_NOT_FOUND);
        }

        account.setEditionCode(event.getPayload().getOrder().getEditionCode());

        accountDao.save(account);

        return new AppDirectResultBean(true, "Updated subscription", Long.toString(identifier), null);
    }

    public AppDirectResultBean handleSubscriptionCancel(EventBean event) {
        if(event.getPayload() == null
                || event.getPayload().getAccount() == null
                || event.getPayload().getAccount().getAccountIdentifier() == null)
            return new AppDirectResultBean(false, "Missing account parameters", null, AppDirectErrorCode.INVALID_RESPONSE);

        long identifier = Long.parseLong(event.getPayload().getAccount().getAccountIdentifier());
        log.info("Identifier: " + identifier);

        Account account = accountDao.getAccount(identifier);

        if(account == null) {
            return new AppDirectResultBean(false, "Account not found", event.getPayload().getAccount().getAccountIdentifier(), AppDirectErrorCode.ACCOUNT_NOT_FOUND);
        }

        accountDao.delete(account);

        return new AppDirectResultBean(true, "Cancelled subscription", Long.toString(identifier), null);
    }

    public AppDirectResultBean handleAssignUser(EventBean event) {
        if(event.getPayload() == null
                || event.getPayload().getAccount() == null
                || event.getPayload().getAccount().getAccountIdentifier() == null)
            return new AppDirectResultBean(false, "Missing account parameters", null, AppDirectErrorCode.INVALID_RESPONSE);

        if(event.getPayload().getUser() == null)
            return new AppDirectResultBean(false, "Missing user parameter", null, AppDirectErrorCode.INVALID_RESPONSE);

        Long accountIdentifier = Long.parseLong(event.getPayload().getAccount().getAccountIdentifier());

        User newUser = userDao.getByUUID(event.getPayload().getUser().getUuid(), accountIdentifier);

        if(newUser != null) {
            return new AppDirectResultBean(false, "User is already assigned", event.getPayload().getAccount().getAccountIdentifier(), AppDirectErrorCode.USER_ALREADY_EXISTS);
        }

        Account account = accountDao.getAccount(accountIdentifier);

        if(account == null) {
            return new AppDirectResultBean(false, "Account not found", event.getPayload().getAccount().getAccountIdentifier(), AppDirectErrorCode.ACCOUNT_NOT_FOUND);
        }

        newUser = User.fromUserBean(event.getPayload().getUser(), Role.USER);

        userDao.save(newUser);
        account.addUser(newUser);

        return new AppDirectResultBean(true, "Assigned user " + newUser.getName(), event.getPayload().getAccount().getAccountIdentifier(), null);
    }

    @UnitOfWork
    public AppDirectResultBean handleUnassignUser(EventBean event) {
        if(event.getPayload() == null
                || event.getPayload().getAccount() == null
                || event.getPayload().getAccount().getAccountIdentifier() == null)
            return new AppDirectResultBean(false, "Missing account parameters", null, AppDirectErrorCode.INVALID_RESPONSE);

        if(event.getPayload().getUser() == null)
            return new AppDirectResultBean(false, "Missing user parameter", null, AppDirectErrorCode.INVALID_RESPONSE);

        long accountIdentifier = Long.parseLong(event.getPayload().getAccount().getAccountIdentifier());
        UUID uuid = event.getPayload().getUser().getUuid();

        Account account = accountDao.getAccount(accountIdentifier);

        if(account == null) {
            return new AppDirectResultBean(false, "Account not found", event.getPayload().getAccount().getAccountIdentifier(), AppDirectErrorCode.ACCOUNT_NOT_FOUND);
        }

        User user = userDao.getByUUID(uuid, accountIdentifier);

        if(user == null) {
            return new AppDirectResultBean(false, "User is not assigned", event.getPayload().getAccount().getAccountIdentifier(), AppDirectErrorCode.USER_NOT_FOUND);
        }
        userDao.delete(user);

        return new AppDirectResultBean(true, "Unassigned user " + user.getName(), event.getPayload().getAccount().getAccountIdentifier(), null);
    }
}
