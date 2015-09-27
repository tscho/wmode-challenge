package com.tschoend.wmodechallenge.dao;

import com.tschoend.wmodechallenge.model.appdirect.entity.UserSession;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

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
        Criteria criteria = currentSession()
                .createCriteria(UserSession.class)
                .add(Restrictions.eq("token", token));

        List<UserSession> matchedSessions = list(criteria);

        if (matchedSessions.size() > 0) {
            return matchedSessions.get(0);
        } else {
            return null;
        }
    }

    public void delete(UserSession session) {
        currentSession().delete(session);
    }
}
