package com.tschoend.wmodechallenge.security.openid;

import lombok.Data;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryInformation;

/**
 * Created by tom on 2015-09-26.
 */
@Data
public class OpenIdState {
    private Long accountIdentifier;
    private DiscoveryInformation discoveryInformation;
}
