package com.tschoend.wmodechallenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URL;

/**
 * Created by tom on 2015-09-20.
 */

@Data
@AllArgsConstructor
public class MarketPlace {
    private URL baseUrl;
    private String partner;
}
