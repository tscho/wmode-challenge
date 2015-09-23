package com.tschoend.wmodechallenge.model.appdirect.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.net.URL;
import java.util.UUID;

/**
 * Created by tom on 2015-09-21.
 */
@Getter
@Setter
@NoArgsConstructor
public class CompanyBean implements Serializable {
    private String country;
    private String email;
    private String name;
    private String phoneNumber;
    private UUID uuid;
    private URL website;
}
