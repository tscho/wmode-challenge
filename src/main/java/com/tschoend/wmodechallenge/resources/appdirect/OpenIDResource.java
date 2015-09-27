package com.tschoend.wmodechallenge.resources.appdirect;

import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * Created by tom on 2015-09-26.
 */
@Slf4j
@Path("/appdirect")
public class OpenIDResource {
    //http://appdirect.tschoend.com/api/appdirect/login?openid={openid}&account={accountIdentifier}

    private final Cache<UUID, ConsumerManager> openIDCache;

    public OpenIDResource(Cache<UUID, ConsumerManager> openIDCache) {
        this.openIDCache = openIDCache;
    }


    @GET
    @Path("/login")
    public Response authenticationRequest(@Context HttpServletRequest request,
                                          @QueryParam("openid") String openid,
                                          @QueryParam("accountIdentifier") String accountIdentifier) {

        UUID sessionToken = UUID.randomUUID();

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

        log.debug("Return to URL '{}'", returnToUrl);

        try {
            ConsumerManager consumerManager = new ConsumerManager();
            openIDCache.put(sessionToken, consumerManager);

            List discoveries = consumerManager.discover(openid);

            DiscoveryInformation discovered = consumerManager.associate(discoveries);

            log.info(discovered.getOPEndpoint().toString());

            return Response.ok().build();
        } catch (DiscoveryException e) {
            throw new ServerErrorException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/verify")
    public Response authenticationVerify() {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
