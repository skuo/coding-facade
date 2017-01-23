package com.coding.facade.rest.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coding.CodingFeignClientConfig;

@FeignClient(name="coding", configuration=CodingFeignClientConfig.class, fallback = CodingFeignClientFallback.class)
public interface CodingFeignClient {
        @RequestMapping(
                method= RequestMethod.GET,
                value="/coding/hola",
                consumes="application/json")
        String hola();

}
