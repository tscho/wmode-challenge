package com.tschoend.wmodechallenge.dao;

import com.tschoend.wmodechallenge.model.appdirect.entity.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;

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

    public void delete(User user) {
        currentSession().delete(user);
    }

    public User getByUUID(UUID uuid, long accountIdentifier) {
        Query query = currentSession().createQuery("from User where uuid = :uuid and account.accountIdentifier = :identifier");
        query.setParameter("uuid", uuid);
        query.setParameter("identifier", accountIdentifier);

        List<User> matchedUsers = list(query);

        if(matchedUsers.size() > 0) {
            return list(query).get(0);
        } else {
            return null;
        }
    }
}
