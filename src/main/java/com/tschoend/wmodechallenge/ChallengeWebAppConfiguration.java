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
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @Valid
    @NotNull
    private String appDirectOauthKey;

    @JsonProperty("appdirectOauthKey")
    public String getAppDirectOauthKey() {
        return appDirectOauthKey;
    }

    @Valid
    @NotNull
    private String appDirectOauthSecret;

    @JsonProperty("appDirectOauthSecret")
    public String getAppDirectOauthSecret() {
        return appDirectOauthSecret;
    }
}
