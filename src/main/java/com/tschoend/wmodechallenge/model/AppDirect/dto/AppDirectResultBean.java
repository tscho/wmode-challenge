package com.tschoend.wmodechallenge.model.appdirect.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tschoend.wmodechallenge.model.appdirect.constants.AppDirectErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by tom on 2015-09-20.
 */
@Getter
@Setter
@NoArgsConstructor
@JacksonXmlRootElement(localName = "result")
public class AppDirectResultBean implements Serializable {
    boolean success;
    String message;
    UUID accountIdentifier;
    AppDirectErrorCode errorCode;
}
