package com.coding.facade.rest.client;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CodingDiscoveryClient {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DiscoveryClient discoveryClient;

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
    
    public String getHello() {
        RestTemplate restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("coding");

        if (instances.size()==0) return null;
        String serviceUri = String.format("%s/coding/hola",instances.get(0).getUri().toString());
        log.info("!!!! SERVICE URI:  " + serviceUri);


        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        new HttpEntity<String>(createHeaders("user","CodingBreak")), 
                        String.class);
        
        return restExchange.getBody();
    }
}