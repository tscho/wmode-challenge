package com.tschoend.wmodechallenge.model.appdirect.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
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
public class PayloadBean implements Serializable {
    private CompanyBean company;

    @JacksonXmlElementWrapper(localName = "configuration")
    @JacksonXmlProperty(localName = "item")
    private List<ConfigurationItem> configurationItemList;

    private AccountBean account;
    private OrderBean order;
}
