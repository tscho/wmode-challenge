package com.tschoend.wmodechallenge.dao;

import com.tschoend.wmodechallenge.model.appdirect.entity.Account;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

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

    public Account getAccount(long id) {
        return get(id);
    }

    public List<Account> findAll() {
        return this.currentSession().createCriteria(Account.class).list();
    }

    public void delete(Account account) {
        this.currentSession().delete(account.getClass().getName(), account);
    }
}
