package com.tschoend.wmodechallenge.security.openid;

import com.tschoend.wmodechallenge.model.appdirect.entity.User;
import com.tschoend.wmodechallenge.model.appdirect.entity.UserSession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.NewCookie;
import java.util.UUID;

/**
 * Created by tom on 2015-09-27.
 */
public class SessionUtil {
    public static final String TOKEN_COOKIE = "auth_token";

    public static NewCookie swapCookie(User user, UserSession session) {
        if (user != null) {
            return new NewCookie(
                    TOKEN_COOKIE,
                    session.getToken().toString(),
                    "/",
                    null,
                    null,
                    86400,
                    false);
        } else {
            return new NewCookie(
                    TOKEN_COOKIE,
                    null,
                    null,
                    null,
                    null,
                    0,
                    false);
        }
    }

    public static UUID getTokenForRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(TOKEN_COOKIE)) {
                    return UUID.fromString(cookie.getValue());
                }
            }
        }

        return null;
    }
}
