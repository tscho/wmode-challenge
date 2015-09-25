package com.tschoend.wmodechallenge.model.appdirect.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tschoend.wmodechallenge.model.appdirect.constants.AppDirectErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by tom on 2015-09-20.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "result")
public class AppDirectResultBean implements Serializable {
    boolean success;
    String message;
    String accountIdentifier;
    AppDirectErrorCode errorCode;
}
