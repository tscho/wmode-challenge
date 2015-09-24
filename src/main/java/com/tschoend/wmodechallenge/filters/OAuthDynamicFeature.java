package com.tschoend.wmodechallenge.filters;

import org.glassfish.jersey.server.model.AnnotatedMethod;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;

/**
 * Created by tom on 2015-09-22.
 */
public class OAuthDynamicFeature implements DynamicFeature {
    public final OAuthFilter oAuthFilter;

    public OAuthDynamicFeature(OAuthFilter oAuthFilter) {
        this.oAuthFilter = oAuthFilter;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        final AnnotatedMethod am = new AnnotatedMethod(resourceInfo.getResourceMethod());
        if(am.isAnnotationPresent(OAuthAuthorized.class)) {
            featureContext.register(oAuthFilter);
        }
    }
}
