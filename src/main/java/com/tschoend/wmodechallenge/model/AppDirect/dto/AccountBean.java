package com.tschoend.wmodechallenge.model.appdirect.dto;

import com.tschoend.wmodechallenge.model.appdirect.constants.SubscriptionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by tom on 2015-09-22.
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountBean implements Serializable {
    private String accountIdentifier;
    private SubscriptionStatus status;
}
