package com.coding.facade.rest.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.governator.annotations.binding.Request;

import feign.auth.BasicAuthRequestInterceptor;

@Configuration
public class CodingFeignClientConfig{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    public static final int FIVE_SECONDS_IN_MS = 5000;
    
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
/*
    @Bean
    public Request.Options options() {
        return new Request.Options(FIVE_SECONDS_IN_MS, FIVE_SECONDS_IN_MS);
    }
*/
}