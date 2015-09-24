package com.tschoend.wmodechallenge.filters;

import oauth.signpost.http.HttpRequest;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by tom on 2015-09-23.
 */
public class JerseyRequestAdapter implements HttpRequest {
    HttpServletRequest request;

    public JerseyRequestAdapter(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getMethod() {
        return request.getMethod();
    }

    @Override
    public String getRequestUrl() {
        return request.getRequestURL().toString();
    }

    @Override
    public void setRequestUrl(String s) {
        throw new NotImplementedException();
    }

    @Override
    public void setHeader(String s, String s1) {
//        request.getHeaders().putSingle(s, s1);
        throw new NotImplementedException();
    }

    @Override
    public String getHeader(String s) {
        return request.getHeader(s);
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
        return request.getContentType();
    }

    @Override
    public Object unwrap() {
        return request;
    }
}
