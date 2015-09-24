package com.tschoend.wmodechallenge.security.oauthsignedfetch;

import oauth.signpost.http.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by tom on 2015-09-23.
 */
public class MinimalHttpRequest implements HttpRequest {
    private String method;
    private String url;

    public MinimalHttpRequest(String method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getRequestUrl() {
        return url;
    }

    @Override
    public void setRequestUrl(String s) {
         this.url = s;
    }

    @Override
    public void setHeader(String s, String s1) {

    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Map<String, String> getAllHeaders() {
        return null;
    }

    @Override
    public InputStream getMessagePayload() throws IOException {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public Object unwrap() {
        return null;
    }
}
