package com.tschoend.wmodechallenge.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

/**
 * Created by tom on 2015-09-20.
 */
@Data
@JacksonXmlRootElement(localName = "result")
public class AppDirectResult {
    boolean success;
    String message;
    UUID accountIdentifier;
    AppDirectErrorCode errorCode;
}
