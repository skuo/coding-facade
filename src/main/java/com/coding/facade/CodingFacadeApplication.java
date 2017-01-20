package com.coding.facade;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class CodingFacadeApplication { 

    public static void main(String[] args) {
        SpringApplication.run(CodingFacadeApplication.class, args);
    }

    @LoadBalanced
    @Bean
    // define a RestTemplate bean to be user in CodingRestTemplateClient
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
    
    @Bean
    // Group all REST endpoint with '/hola' prefix in the same docket
    public Docket holaApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("greeting")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/greeting.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot REST API with Swagger")
                .description("Spring Boot REST API with Swagger")
                .termsOfServiceUrl("http://com.coding")
                .contact(new Contact("Steve Kuo","http://com.coding/skuo","skuo@coding.com"))
                .license("Apache License Version 2.0")
                .licenseUrl("https://githuh.com")
                .version("2.0")
                .build();
    }
                
}
