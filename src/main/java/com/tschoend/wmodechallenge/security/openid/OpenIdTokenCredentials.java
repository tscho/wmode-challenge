package com.tschoend.wmodechallenge.security.openid;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * Created by tom on 2015-09-27.
 */
@Data
@AllArgsConstructor
public class OpenIdTokenCredentials {
    private UUID token;
}
