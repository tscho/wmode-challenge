package com.tschoend.wmodechallenge.model.appdirect;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by tom on 2015-09-21.
 */
@Data
public class Order {
    private EditionCode editionCode;
    private PricingDuration pricingDuration;

    @JacksonXmlProperty(localName = "item")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<OrderItem> items;
}
