package com.tschoend.wmodechallenge.model.appdirect;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by tom on 2015-09-20.
 */
@Data
@JacksonXmlRootElement(localName = "event")
public class Event implements Serializable {
    @JacksonXmlProperty(localName = "type")
    private EventType type;

    @JacksonXmlProperty(localName = "marketplace")
    private MarketPlace marketPlace;

    @JacksonXmlProperty(localName = "flag")
    private EventFlag flag;

    @JacksonXmlProperty(localName = "creator")
    private User creator;

    private Payload payload;

    private URL returnUrl;
}
