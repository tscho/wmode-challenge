package com.tschoend.wmodechallenge;

import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.tschoend.wmodechallenge.resources.appdirect.SubscriptionEventResource;
import com.yunspace.dropwizard.xml.XmlBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by tom on 2015-09-20.
 */
public class ChallengeWebApp extends Application<ChallengeWebAppConfiguration> {
    public static void main(String... args) throws Exception {
        new ChallengeWebApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<ChallengeWebAppConfiguration> bootstrap) {
        XmlBundle xmlBundle = new XmlBundle();
        xmlBundle.getXmlMapper().configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);

        bootstrap.addBundle(xmlBundle);
    }

    @Override
    public void run(ChallengeWebAppConfiguration challengeWebAppConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new SubscriptionEventResource());
        environment.jersey().disable();
    }
}
