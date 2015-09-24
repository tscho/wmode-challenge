package com.tschoend.wmodechallenge;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by tom on 2015-09-20.
 */
public class ChallengeWebAppConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();
    @Valid
    @NotNull
    @JsonProperty
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();
    @Valid
    @NotNull
    @JsonProperty
    private String appDirectOauthKey;
    @Valid
    @NotNull
    @JsonProperty
    private String appDirectOauthSecret;

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public JerseyClientConfiguration getJerseyClientConfiguration() { return httpClient; }

    public String getAppDirectOauthKey() {
        return appDirectOauthKey;
    }

    public String getAppDirectOauthSecret() {
        return appDirectOauthSecret;
    }
}
