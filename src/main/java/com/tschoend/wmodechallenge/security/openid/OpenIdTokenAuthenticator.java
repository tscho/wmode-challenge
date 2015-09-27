package com.tschoend.wmodechallenge.security.openid;

import com.google.common.base.Optional;
import com.tschoend.wmodechallenge.dao.UserSessionDao;
import com.tschoend.wmodechallenge.model.appdirect.entity.User;
import com.tschoend.wmodechallenge.model.appdirect.entity.UserSession;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

/**
 * Created by tom on 2015-09-27.
 */
public class OpenIdTokenAuthenticator implements Authenticator<OpenIdTokenCredentials, UserSession> {
    private final UserSessionDao sessionDao;

    public OpenIdTokenAuthenticator(UserSessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public Optional<UserSession> authenticate(OpenIdTokenCredentials openIdTokenCredentials) throws AuthenticationException {
        UserSession session = sessionDao.getByToken(openIdTokenCredentials.getToken());

        return Optional.fromNullable(session);
    }
}
