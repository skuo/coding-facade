package com.coding.facade.rest.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.auth.BasicAuthRequestInterceptor;

@Configuration
public class CodingFeignClientConfig{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${coding.service.username}")
    String codingServiceUsername;
    
    @Value("${coding.service.password}")
    String codingServicePassword;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        log.info(String.format("<<<< In CodingFeignClientConfig codingServiceUsername=%s, codingServicePassword=%s",
                codingServiceUsername, codingServicePassword));
        return new BasicAuthRequestInterceptor(codingServiceUsername, codingServicePassword);
    }
}