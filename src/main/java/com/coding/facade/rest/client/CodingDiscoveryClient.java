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

import com.coding.facade.util.RandomUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Component
public class CodingDiscoveryClient implements CodingServiceClient {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final String HYSTRIX_PROPERTY_TIMEOUTINMS_STR = "5000";
    private final Integer HYSTRIX_PROPERTY_TIMEOUTINMS = Integer.valueOf(HYSTRIX_PROPERTY_TIMEOUTINMS_STR);

    @Autowired
    private DiscoveryClient discoveryClient;

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
    public String getHello(String username, String password) {
        log.info("HystrixProperty execution.isolation.thread.timeoutInMilliseconds=" + HYSTRIX_PROPERTY_TIMEOUTINMS);
        // sleep 11000 milliseconds 1 out of 3 times
        // comment out since coding.halo implements randomUtil.randomRunLong()
        //randomUtil.randomRunLong(11000, 3); 
        
        List<ServiceInstance> instances = discoveryClient.getInstances("coding");
        if (instances.size()==0) 
            return null;
        String serviceUri = String.format("%s/coding/hola",instances.get(0).getUri().toString());
        log.info("!!!! SERVICE URI:  " + serviceUri);

        RestTemplate restTemplate = new RestTemplate();
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
                   + "\"suggestion\": \"fallback suggestion --retry again\"" 
                + "}";
    }
}