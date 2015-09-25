package com.tschoend.wmodechallenge.dao;

import com.tschoend.wmodechallenge.model.appdirect.Account;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by tom on 2015-09-24.
 */
public class AccountDao extends AbstractDAO<Account> {
    public AccountDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Account save(Account account) {
        return persist(account);
    }

    public void delete(long id) {
        Account account = get(id);
        this.currentSession().delete(account.getClass().getName(), account);
    }
}
