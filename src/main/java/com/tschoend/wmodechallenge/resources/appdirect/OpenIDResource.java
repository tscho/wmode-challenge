package com.tschoend.wmodechallenge.resources.appdirect;

import com.google.common.cache.Cache;
import com.tschoend.wmodechallenge.dao.UserDao;
import com.tschoend.wmodechallenge.dao.UserSessionDao;
import com.tschoend.wmodechallenge.model.appdirect.entity.User;
import com.tschoend.wmodechallenge.model.appdirect.entity.UserSession;
import com.tschoend.wmodechallenge.security.openid.OpenIdState;
import com.tschoend.wmodechallenge.security.openid.SessionUtil;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Created by tom on 2015-09-26.
 *
 * Adapted from the examples at:
 *      http://gary-rowe.com/agilestack/2012/12/12/dropwizard-with-openid/
 *      https://github.com/jbufu/openid4java/blob/master/samples/consumer-servlet/src/main/java/org/openid4java/samples/consumerservlet/ConsumerServlet.java
 */
@Slf4j
@Path("/appdirect")
public class OpenIDResource {
    private final Cache<UUID, OpenIdState> openIdStateCache;
    private final ConsumerManager consumerManager;
    private final UserDao userDao;
    private final UserSessionDao sessionDao;

    public OpenIDResource(Cache<UUID, OpenIdState> openIdStateCache, ConsumerManager consumerManager, UserDao userDao, UserSessionDao sessionDao) {
        this.openIdStateCache = openIdStateCache;
        this.consumerManager = consumerManager;
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }


    @GET
    @Path("/login")
    public Response authenticationRequest(@Context HttpServletRequest request,
                                          @QueryParam("openid") String openid,
                                          @QueryParam("account") String accountIdentifier) {

        UUID sessionToken = UUID.randomUUID();

        OpenIdState openIdState = new OpenIdState();

        openIdState.setAccountIdentifier(Long.decode(accountIdentifier));

        final String returnToUrl;
        if (request.getServerPort() == 80) {
            returnToUrl = String.format(
                    "http://%s/api/appdirect/verify?token=%s",
                    request.getServerName(),
                    sessionToken);
        } else {
            returnToUrl = String.format(
                    "http://%s:%d/api/appdirect/verify?token=%s",
                    request.getServerName(),
                    request.getServerPort(),
                    sessionToken);
        }

        log.info("Return to URL '{}'", returnToUrl);

        try {
            List discoveries = consumerManager.discover(openid);

            DiscoveryInformation discovered = consumerManager.associate(discoveries);
            openIdState.setDiscoveryInformation(discovered);

            AuthRequest authRequest = consumerManager.authenticate(discovered, returnToUrl);

            // We'll try to associate the OpenID login with UUID first, then fallback to email
            FetchRequest fetchRequest = FetchRequest.createFetchRequest();
            fetchRequest.addAttribute("uuid", "https://www.appdirect.com/schema/user/uuid", true);
            fetchRequest.addAttribute("email", "http://axschema.org/contact/email", true);

            authRequest.addExtension(fetchRequest);

            openIdStateCache.put(sessionToken, openIdState);
            return Response
                    .seeOther(URI.create(authRequest.getDestinationUrl(true)))
                    .build();

        } catch (Exception e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/verify")
    @UnitOfWork
    public Response authenticationVerify(
            @Context HttpServletRequest request,
            @QueryParam("token") String token) {

        if(token == null) {
            log.warn("Authentication failed - no token");
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        UUID sessionToken = UUID.fromString(token);

        OpenIdState state = openIdStateCache.getIfPresent(sessionToken);

        if(state == null) {
            log.debug("Authentication failed due to no state matching session token {}", token);
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

        StringBuffer receivingUrl = request.getRequestURL();
        String queryString = request.getQueryString();

        if(queryString != null && queryString.length() > 0) {
            receivingUrl.append("?").append(queryString);
        }

        log.info("Receiveing URL: {}", receivingUrl.toString());

        ParameterList parameterList = new ParameterList(request.getParameterMap());

        try {
            VerificationResult verificationResult = consumerManager.verify(
                    receivingUrl.toString(),
                    parameterList,
                    state.getDiscoveryInformation());

            Identifier identifier = verificationResult.getVerifiedId();

            if(identifier != null) {
                AuthSuccess authSuccess = (AuthSuccess)verificationResult.getAuthResponse();

                if(!authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
                    log.warn("OpenID response did not contain user information");
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }

                User user = null;

                UUID userUuid = extractUuid(authSuccess);
                String userEmail = extractEmail(authSuccess);

                if(userUuid != null) {
                   user = userDao.getByUUID(userUuid, state.getAccountIdentifier());
                } else if (userEmail != null){
                    log.info("Falling back to email");
                    user = userDao.getByEmail(userEmail, state.getAccountIdentifier());
                }

                if(user == null) {
                    log.warn("Could not find user for OpenID response");
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }

                UserSession session = UserSession.create(user);
                sessionDao.save(session);

                // We're done logging in so evict the OpenID process state
                openIdStateCache.invalidate(sessionToken);

                // Redirect to homepage
                return Response
                        .seeOther(URI.create("/"))
                        .cookie(SessionUtil.swapCookie(user, session))
                        .build();

            }
        } catch (OpenIDException e) {
            log.warn("OpenIDException", e);
        }

        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    @GET
    @Path("/logout")
    public Response logout(@Auth UserSession session, @Context HttpServletRequest request) {
        sessionDao.delete(session);

        try {
            URI baseUri = session.getUser().getAccountEntity().getMarketplaceBaseUrl();
            URI redirectUri = new URI(
                    baseUri.getScheme(),
                    null,
                    baseUri.getHost(),
                    baseUri.getPort(),
                    "/applogout",
                    "openid=" + session.getUser().getOpenId(),
                    null);

            return Response
                    .seeOther(redirectUri)
                    .cookie(SessionUtil.swapCookie(null, null))
                    .build();
        } catch (URISyntaxException e) {
            log.warn("Error constructing logout redirect", e);
        }

        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
    }

    private UUID extractUuid(AuthSuccess authSuccess) throws MessageException {
        FetchResponse response = (FetchResponse)authSuccess.getExtension(AxMessage.OPENID_NS_AX);
        return UUID.fromString(getAttributeValue(
                response,
                "uuid",
                null,
                String.class));
    }

    private String extractEmail(AuthSuccess authSuccess) throws MessageException {
        FetchResponse response = (FetchResponse)authSuccess.getExtension(AxMessage.OPENID_NS_AX);
        return getAttributeValue(
                response,
                "email",
                null,
                String.class);
    }

    private <T> T getAttributeValue(
            FetchResponse response,
            String attribute,
            T defaultVal,
            Class<T> klass) {
        List attrList = response.getAttributeValues(attribute);
        if(attrList != null && attrList.size() > 0) {
            return (T) attrList.get(0);
        }

        return defaultVal;
    }
}
