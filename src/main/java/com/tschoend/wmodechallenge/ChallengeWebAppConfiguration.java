package com.tschoend.wmodechallenge;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
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

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @Valid
    @NotNull
    @JsonProperty
    private String appDirectOauthKey;

    public String getAppDirectOauthKey() {
        return appDirectOauthKey;
    }

    @Valid
    @NotNull
    @JsonProperty
    private String appDirectOauthSecret;

    public String getAppDirectOauthSecret() {
        return appDirectOauthSecret;
    }
}
