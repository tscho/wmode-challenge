package com.tschoend.wmodechallenge.model.appdirect.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;

/**
 * Created by tom on 2015-09-20.
 */

@Getter
@Setter
@NoArgsConstructor
public class MarketPlaceBean implements Serializable {
    private URL baseUrl;
    private String partner;
}
