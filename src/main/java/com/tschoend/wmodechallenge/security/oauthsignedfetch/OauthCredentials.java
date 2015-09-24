package com.tschoend.wmodechallenge.security.oauthsignedfetch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by tom on 2015-09-22.
 */
@Getter
@RequiredArgsConstructor
public class OauthCredentials {
    private final String method;
    private final String url;
    private final String query;
    private final String authHeader;
}
