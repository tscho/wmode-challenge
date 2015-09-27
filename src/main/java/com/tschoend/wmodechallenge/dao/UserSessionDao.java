package com.tschoend.wmodechallenge.dao;

import com.tschoend.wmodechallenge.model.appdirect.entity.UserSession;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.UUID;

/**
 * Created by tom on 2015-09-26.
 */
public class UserSessionDao extends AbstractDAO<UserSession> {
    public UserSessionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public UserSession save(UserSession session) {
        return persist(session);
    }

    public UserSession getByToken(UUID token) {
        Query query = currentSession().createQuery("from UserSession where token = :token");
        query.setParameter("token", token);

        List<UserSession> matchedSessions = list(query);

        if(matchedSessions.size() > 0) {
            return matchedSessions.get(0);
        } else {
            return null;
        }
    }
}
