package com.tschoend.wmodechallenge.model.appdirect.dto;

import com.tschoend.wmodechallenge.model.appdirect.constants.Unit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by tom on 2015-09-21.
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderItemBean implements Serializable {
    private int quantity;
    private Unit unit;
}
