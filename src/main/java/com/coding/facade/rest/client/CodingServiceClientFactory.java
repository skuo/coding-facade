package com.coding.facade.rest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodingServiceClientFactory {
    @Autowired
    CodingDiscoveryClient codingDiscoveryClient;

    @Autowired
    CodingRestTemplateClient codingRestTemplateClient;

    public CodingServiceClient getClient(String clientType) {
        CodingServiceClient client = null;
        switch(clientType) {
        case "discoveryClient":
            client = codingDiscoveryClient;
            break;
        case "restTemplate":
            client = codingRestTemplateClient;
            break;
        default:
            break;
        }
        return client;
    }
    
}
