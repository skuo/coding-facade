package com.coding.facade.rest.client;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.coding.facade.util.RandomUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class CodingRestTemplateClient implements CodingServiceClient {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RandomUtil randomUtil;
    
    @SuppressWarnings("serial")
    private HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

    @HystrixCommand
    public String getHello(String username, String password){
        // sleep 11000 milliseconds 1 out of 3 times
        randomUtil.randomRunLong(11000, 3); 
        
        //serviceUri is of this form. http://{applicationid}/v1/organizations/{organizationId}
        String serviceUri = String.format("http://coding/coding/hola");
        log.info(">>>> SERVICE URI for applicationId=coding:  " + serviceUri);

        // resetTemplate make sure of Ribbon and is client-side load balanced
        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        new HttpEntity<String>(createHeaders(username,password)), 
                        String.class);

        return restExchange.getBody();
    }
}