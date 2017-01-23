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
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Component
public class CodingRestTemplateClient implements CodingServiceClient {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final String HYSTRIX_PROPERTY_TIMEOUTINMS_STR = "12000";
    private final Integer HYSTRIX_PROPERTY_TIMEOUTINMS = Integer.valueOf(HYSTRIX_PROPERTY_TIMEOUTINMS_STR);

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

    @HystrixCommand(
            fallbackMethod = "buildFallbackHello",
            commandProperties = {
              @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value=HYSTRIX_PROPERTY_TIMEOUTINMS_STR)
            },
            threadPoolKey = "codingDiscoveryClientPool",
            threadPoolProperties = {
              @HystrixProperty(name="coreSize", value="30"),
              @HystrixProperty(name="maxQueueSize", value="10")
            }
          )
    public String getHello(String username, String password){
        log.info("HystrixProperty execution.isolation.thread.timeoutInMilliseconds=" + HYSTRIX_PROPERTY_TIMEOUTINMS);
        // sleep 11000 milliseconds 1 out of 3 times
        //randomUtil.randomRunLong(11000, 3); 
        
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

    @SuppressWarnings("unused")
    private String buildFallbackHello(String username, String password) {
        return "{"
                   + "\"suggestion\": \"fallback suggestion --timed out, please retry again\"" 
                + "}";
    }
}