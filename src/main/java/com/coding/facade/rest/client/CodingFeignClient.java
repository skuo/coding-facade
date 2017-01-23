package com.coding.facade.rest.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.coding.CodingFeignClientConfig;

// FeignClient does not play well with DiscoveryClient or RestTemplate.  For example specifying
// fallback messes up fallback for DiscoveryClient and RestTemplate.
//@FeignClient(name="coding", configuration=CodingFeignClientConfig.class, fallback = CodingFeignClientFallback.class)
@FeignClient(name="coding", configuration=CodingFeignClientConfig.class)
public interface CodingFeignClient {
        @RequestMapping(
                method= RequestMethod.GET,
                value="/coding/hola",
                consumes="application/json")
        String hola();

}
