package com.tschoend.wmodechallenge.client;

import com.tschoend.wmodechallenge.model.appdirect.dto.EventBean;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.oauth1.AccessToken;
import org.glassfish.jersey.client.oauth1.ConsumerCredentials;
import org.glassfish.jersey.client.oauth1.OAuth1ClientSupport;
import org.glassfish.jersey.filter.LoggingFilter;
import org.slf4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tom on 2015-09-24.
 */
@Slf4j
public class AppDirectAuthorizedClient {
    private final Client client;
    private final Feature oauthFeature;

    public AppDirectAuthorizedClient(Client client, String oauthKey, String oauthSecret) {
        this.client = client;

        ConsumerCredentials credentials = new ConsumerCredentials(oauthKey, oauthSecret);

        oauthFeature = OAuth1ClientSupport.builder(credentials)
                .feature()
                .build();
    }

    public EventBean getEvent(String url) {
        WebTarget target = client.target(url);
        target.register(oauthFeature);
        target.register(new LoggingFilter(java.util.logging.Logger.getLogger(this.getClass().getName()), true));

        log.info("Fetching event for URL: " + url);
        Response response = target
                .request(MediaType.APPLICATION_XML_TYPE)
                .get();

        if(response.getStatus() != 200) {
            log.warn("Received status code " + response.getStatus());
            return null;
        }

        return response.readEntity(EventBean.class);
    }
}
