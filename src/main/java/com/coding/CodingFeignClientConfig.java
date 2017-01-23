package com.coding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.auth.BasicAuthRequestInterceptor;

/**
 * CodingFeignClient should not be in ComponentScan path so that the value is not applied to all Feign clients.
 * @author terrancekuo
 *
 */
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

    // best page on Feign client timeout: https://github.com/spring-cloud/spring-cloud-netflix/issues/696
/*
    @Bean
    public Request.Options options() {
        log.info(String.format("<<<< IN CodingFeignClientConfig timeout=%d", FIVE_SECONDS_IN_MS));
        return new Request.Options(FIVE_SECONDS_IN_MS, FIVE_SECONDS_IN_MS);
    }
    @Bean
    Request.Options requestOptions(ConfigurableEnvironment env){
        int ribbonReadTimeout = env.getProperty("ribbon.ReadTimeout", int.class, 6000);
        int ribbonConnectionTimeout = env.getProperty("ribbon.ConnectTimeout", int.class, 3000);
        log.info(String.format("<<<< IN CodingFeignClientConfig ribbonReadTimeout=%d, robbonConnectionTimeout=%d",
                ribbonReadTimeout, ribbonConnectionTimeout));

        return new Request.Options(ribbonConnectionTimeout, ribbonReadTimeout);
    }    
*/
}