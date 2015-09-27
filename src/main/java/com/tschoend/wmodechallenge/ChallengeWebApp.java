package com.tschoend.wmodechallenge;

import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tschoend.wmodechallenge.client.AppDirectAuthorizedClient;
import com.tschoend.wmodechallenge.dao.AccountDao;
import com.tschoend.wmodechallenge.dao.UserDao;
import com.tschoend.wmodechallenge.dao.UserSessionDao;
import com.tschoend.wmodechallenge.filters.OAuthSignedFetchFeature;
import com.tschoend.wmodechallenge.filters.OAuthSignedFetchFilter;
import com.tschoend.wmodechallenge.model.appdirect.entity.Account;
import com.tschoend.wmodechallenge.model.appdirect.entity.User;
import com.tschoend.wmodechallenge.model.appdirect.entity.UserSession;
import com.tschoend.wmodechallenge.resources.api.AccountResource;
import com.tschoend.wmodechallenge.resources.api.UserResource;
import com.tschoend.wmodechallenge.resources.appdirect.EventResource;
import com.tschoend.wmodechallenge.resources.appdirect.OpenIDResource;
import com.tschoend.wmodechallenge.security.openid.OpenIdState;
import com.tschoend.wmodechallenge.security.openid.OpenIdTokenAuthFactory;
import com.tschoend.wmodechallenge.security.openid.OpenIdTokenAuthenticator;
import com.yunspace.dropwizard.xml.XmlBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.openid4java.consumer.ConsumerManager;

import javax.ws.rs.client.Client;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by tom on 2015-09-20.
 */
public class ChallengeWebApp extends Application<ChallengeWebAppConfiguration> {
    private final HibernateBundle<ChallengeWebAppConfiguration> hibernate = new HibernateBundle<ChallengeWebAppConfiguration>(
            Account.class,
            User.class,
            UserSession.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(ChallengeWebAppConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };
    private final MigrationsBundle<ChallengeWebAppConfiguration> migrations = new MigrationsBundle<ChallengeWebAppConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(ChallengeWebAppConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(String... args) throws Exception {
        new ChallengeWebApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<ChallengeWebAppConfiguration> bootstrap) {
        // For XML request/result serialization
        XmlBundle xmlBundle = new XmlBundle();
        xmlBundle.getXmlMapper().configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        bootstrap.addBundle(xmlBundle);

        bootstrap.addBundle(migrations);
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(new AssetsBundle("/assets", "/"));
    }

    @Override
    public void run(ChallengeWebAppConfiguration challengeWebAppConfiguration, Environment environment) throws Exception {
        Client client = new JerseyClientBuilder(environment)
                .using(challengeWebAppConfiguration.getJerseyClientConfiguration())
                .build(getName());

        AppDirectAuthorizedClient appDirectClient = new AppDirectAuthorizedClient(
                client,
                challengeWebAppConfiguration.getAppDirectOauthKey(),
                challengeWebAppConfiguration.getAppDirectOauthSecret());

        OAuthSignedFetchFilter oAuthSignedFetchFilter = new OAuthSignedFetchFilter(
                challengeWebAppConfiguration.getAppDirectOauthKey(),
                challengeWebAppConfiguration.getAppDirectOauthSecret());

        OAuthSignedFetchFeature oAuthSignedFetchFeature = new OAuthSignedFetchFeature(oAuthSignedFetchFilter);

        AccountDao accountDao = new AccountDao(hibernate.getSessionFactory());
        UserDao userDao = new UserDao(hibernate.getSessionFactory());
        UserSessionDao sessionDao = new UserSessionDao(hibernate.getSessionFactory());

        Cache<UUID, OpenIdState> openIdCache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(2, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        ConsumerManager consumerManager = new ConsumerManager();
        consumerManager.setMaxNonceAge(challengeWebAppConfiguration.getOpenIdMaxNonceAge());

        environment.jersey().register(
                AuthFactory.binder(
                        new OpenIdTokenAuthFactory<>(
                                new OpenIdTokenAuthenticator(sessionDao, challengeWebAppConfiguration.getSessionExpiryPeriodMinutes()),
                                null,
                                UserSession.class)));

        environment.jersey().register(oAuthSignedFetchFeature);
        environment.jersey().register(new EventResource(appDirectClient, accountDao, userDao));
        environment.jersey().register(new AccountResource(accountDao));
        environment.jersey().register(new OpenIDResource(openIdCache, consumerManager, userDao, sessionDao));
        environment.jersey().register(new UserResource());
    }
}
