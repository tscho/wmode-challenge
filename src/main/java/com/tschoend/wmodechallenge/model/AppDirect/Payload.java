package com.tschoend.wmodechallenge.model.appdirect;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tom on 2015-09-21.
 */
@Data
public class Payload {
    private Company company;

    @JacksonXmlElementWrapper(localName = "configuration")
    @JacksonXmlProperty(localName = "item")
    private List<ConfigurationItem> configurationItemList;

    private Order order;
}
