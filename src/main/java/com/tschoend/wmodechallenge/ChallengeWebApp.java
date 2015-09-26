package com.tschoend.wmodechallenge;

import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.tschoend.wmodechallenge.client.AppDirectAuthorizedClient;
import com.tschoend.wmodechallenge.dao.AccountDao;
import com.tschoend.wmodechallenge.dao.UserDao;
import com.tschoend.wmodechallenge.model.appdirect.entity.Account;
import com.tschoend.wmodechallenge.model.appdirect.entity.User;
import com.tschoend.wmodechallenge.resources.api.AccountResource;
import com.tschoend.wmodechallenge.resources.appdirect.EventResource;
import com.tschoend.wmodechallenge.security.oauthsignedfetch.OAuth1Authenticator;
import com.tschoend.wmodechallenge.security.oauthsignedfetch.OAuth1Factory;
import com.yunspace.dropwizard.xml.XmlBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.ChainedAuthFactory;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;

/**
 * Created by tom on 2015-09-20.
 */
public class ChallengeWebApp extends Application<ChallengeWebAppConfiguration> {
    private final HibernateBundle<ChallengeWebAppConfiguration> hibernate = new HibernateBundle<ChallengeWebAppConfiguration>(
            Account.class,
            User.class) {
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
        ChainedAuthFactory<User> chainedAuthFactory = new ChainedAuthFactory<>(
                new OAuth1Factory<>(
                        new OAuth1Authenticator(
                                challengeWebAppConfiguration.getAppDirectOauthKey(),
                                challengeWebAppConfiguration.getAppDirectOauthSecret()),
                        User.class,
                        null,
                        true));

        Client client = new JerseyClientBuilder(environment)
                .using(challengeWebAppConfiguration.getJerseyClientConfiguration())
                .build(getName());

        AppDirectAuthorizedClient appDirectClient = new AppDirectAuthorizedClient(
                client,
                challengeWebAppConfiguration.getAppDirectOauthKey(),
                challengeWebAppConfiguration.getAppDirectOauthSecret());

        AccountDao accountDao = new AccountDao(hibernate.getSessionFactory());
        UserDao userDao = new UserDao(hibernate.getSessionFactory());

        environment.jersey().register(AuthFactory.binder(chainedAuthFactory));
        environment.jersey().register(new EventResource(appDirectClient, accountDao, userDao));
        environment.jersey().register(new AccountResource(accountDao));
    }
}
