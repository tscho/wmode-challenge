package com.tschoend.wmodechallenge.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by tom on 2015-09-20.
 */
@Data
public abstract class Event implements Serializable {
    private MarketPlace marketPlace;
}
