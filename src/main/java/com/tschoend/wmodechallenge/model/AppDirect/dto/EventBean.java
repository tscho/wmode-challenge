package com.tschoend.wmodechallenge.model.appdirect.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tschoend.wmodechallenge.model.appdirect.constants.EventFlag;
import com.tschoend.wmodechallenge.model.appdirect.constants.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by tom on 2015-09-20.
 */
@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "event")
public class EventBean implements Serializable {
    private EventType type;
    private MarketPlaceBean marketplace;
    private EventFlag flag;
    private UserBean creator;
    private PayloadBean payload;
    private URL returnUrl;
}
