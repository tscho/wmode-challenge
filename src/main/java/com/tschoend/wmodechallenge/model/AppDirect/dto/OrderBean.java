package com.tschoend.wmodechallenge.model.appdirect.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.tschoend.wmodechallenge.model.appdirect.constants.EditionCode;
import com.tschoend.wmodechallenge.model.appdirect.constants.PricingDuration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tom on 2015-09-21.
 */
@Getter
@Setter
@NoArgsConstructor
public class OrderBean implements Serializable {
    private EditionCode editionCode;
    private PricingDuration pricingDuration;

    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<OrderItemBean> items;
}
