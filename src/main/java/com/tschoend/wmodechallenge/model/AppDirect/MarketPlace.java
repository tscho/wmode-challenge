package com.tschoend.wmodechallenge.model.appdirect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

/**
 * Created by tom on 2015-09-20.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketPlace {
    private URL baseUrl;
    private String partner;
}
