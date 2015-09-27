package com.tschoend.wmodechallenge.security.openid;

import com.google.common.base.Optional;
import com.tschoend.wmodechallenge.dao.UserSessionDao;
import com.tschoend.wmodechallenge.model.appdirect.entity.UserSession;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

/**
 * Created by tom on 2015-09-27.
 */
@Slf4j
public class OpenIdTokenAuthenticator implements Authenticator<OpenIdTokenCredentials, UserSession> {
    private final UserSessionDao sessionDao;
    private final int expiryPeridodMinutes;

    public OpenIdTokenAuthenticator(UserSessionDao sessionDao, int expiryPeridodMinutes) {
        this.sessionDao = sessionDao;
        this.expiryPeridodMinutes = expiryPeridodMinutes;
    }

    @Override
    public Optional<UserSession> authenticate(OpenIdTokenCredentials openIdTokenCredentials) throws AuthenticationException {
        UserSession session = sessionDao.getByToken(openIdTokenCredentials.getToken());

        if(session != null) {
            //Invalidate old session
            if (session.getAccessedAt().plusMinutes(expiryPeridodMinutes).isBefore(DateTime.now())) {
                log.info("Expiring token {}", session.getToken());
                sessionDao.delete(session);
            } else {
                log.debug("Updating token access time {}", session.getToken());
                session.setAccessedAt(DateTime.now());
                sessionDao.save(session);
            }
        }

        return Optional.fromNullable(session);
    }
}
