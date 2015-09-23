package com.tschoend.wmodechallenge;

import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.tschoend.wmodechallenge.filters.OAuthProvider;
import com.tschoend.wmodechallenge.model.appdirect.Account;
import com.tschoend.wmodechallenge.model.appdirect.User;
import com.tschoend.wmodechallenge.resources.appdirect.SubscriptionEventResource;
import com.yunspace.dropwizard.xml.XmlBundle;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by tom on 2015-09-20.
 */
public class ChallengeWebApp extends Application<ChallengeWebAppConfiguration> {
    public static void main(String... args) throws Exception {
        new ChallengeWebApp().run(args);
    }

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

    @Override
    public void initialize(Bootstrap<ChallengeWebAppConfiguration> bootstrap) {
        // For XML request/result serialization
        XmlBundle xmlBundle = new XmlBundle();
        xmlBundle.getXmlMapper().configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        bootstrap.addBundle(xmlBundle);

        bootstrap.addBundle(migrations);
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(ChallengeWebAppConfiguration challengeWebAppConfiguration, Environment environment) throws Exception {
        OAuthProvider provider = new OAuthProvider(
                challengeWebAppConfiguration.getAppDirectOauthKey(),
                challengeWebAppConfiguration.getAppDirectOauthSecret());


        environment.jersey().register(new SubscriptionEventResource(provider));
//        environment.jersey().disable();
    }
}
