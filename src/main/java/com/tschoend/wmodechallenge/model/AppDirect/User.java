package com.tschoend.wmodechallenge.model.appdirect;

import lombok.Data;

import java.net.URL;
import java.util.UUID;

/**
 * Created by tom on 2015-09-21.
 */
@Data
public class User {
    private String email;
    private String firstName;
    private String language;
    private String lastName;
    private URL openId;
    private UUID uuid;
}
