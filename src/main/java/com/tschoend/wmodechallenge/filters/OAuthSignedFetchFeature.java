package com.tschoend.wmodechallenge.filters;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by tom on 2015-09-26.
 */
public class OAuthSignedFetchFeature implements DynamicFeature {
    private final OAuthSignedFetchFilter filter;

    public OAuthSignedFetchFeature(OAuthSignedFetchFilter filter) {
        this.filter = filter;
    }

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        if(resourceInfo.getResourceMethod().getAnnotation(OAuthSigned.class) != null) {
            featureContext.register(filter);
        }
    }
}
