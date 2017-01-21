package com.coding.facade.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig {
    @Value("${example.property}")
    private String exampleProperty;
    
    @Value("${coding.service.username}")
    String codingServiceUsername;
    
    @Value("${coding.service.password}")
    String codingServicePassword;

    public String getExampleProperty() {
        return exampleProperty;
    }

    public String getCodingServiceUsername() {
        return codingServiceUsername;
    }

    public String getCodingServicePassword() {
        return codingServicePassword;
    }
    
}
