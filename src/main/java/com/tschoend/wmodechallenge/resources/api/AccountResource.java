package com.tschoend.wmodechallenge.resources.api;

import com.tschoend.wmodechallenge.dao.AccountDao;
import com.tschoend.wmodechallenge.model.appdirect.entity.Account;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.Hibernate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by tom on 2015-09-20.
 */
@Path("/accounts/")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    private final AccountDao accountDao;

    public AccountResource(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @GET
    @UnitOfWork
    public List<Account> getAccounts() {
        return accountDao.findAll();
    }

    @GET
    @Path("{id}")
    @UnitOfWork
    public Account getAccount(@PathParam("id") long id) {
        Account account = accountDao.getAccount(id);

        if (account != null) {
            Hibernate.initialize(account.getUsers());
            return account;
        } else {
            throw new WebApplicationException("Account not found", Response.Status.NOT_FOUND);
        }
    }
}
