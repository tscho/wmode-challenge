package com.tschoend.wmodechallenge.dao;

import com.tschoend.wmodechallenge.model.appdirect.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by tom on 2015-09-24.
 */
public class UserDao extends AbstractDAO<User> {
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public User save(User user) {
        return persist(user);
    }
}
