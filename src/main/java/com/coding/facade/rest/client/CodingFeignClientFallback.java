package com.coding.facade.rest.client;

import org.springframework.stereotype.Component;

@Component
public class CodingFeignClientFallback implements CodingFeignClient {

    @Override
    public String hola() {
        return "{"
                + "\"suggestion\": \"feign fallback suggestion from CodingFeignClientFallback --timed out, please retry again\"" 
             + "}";
    }

}
