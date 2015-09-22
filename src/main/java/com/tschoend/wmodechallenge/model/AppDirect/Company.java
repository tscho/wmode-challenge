package com.tschoend.wmodechallenge.model.appdirect;

import lombok.Data;

import java.net.URL;
import java.util.UUID;

/**
 * Created by tom on 2015-09-21.
 */
@Data
public class Company {
    private String country;
    private String email;
    private String name;
    private String phoneNumber;
    private UUID uuid;
    private URL website;
}
