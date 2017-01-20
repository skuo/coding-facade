package com.coding.facade.rest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.coding.facade.rest.client.CodingServiceClient;
import com.coding.facade.rest.client.CodingServiceClientFactory;

@RestController
@RefreshScope
public class GreetingController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${example.property}")
    private String exampleProperty;
    
    @Value("${coding.service.username}")
    String codingServiceUsername;
    
    @Value("${coding.service.password}")
    String codingServicePassword;
    
    @Autowired
    CodingServiceClientFactory codingSerivceClientFactory;
    
    @RequestMapping(method = RequestMethod.GET, value = "/greeting/version", headers = "accept=application/json")
    @ResponseBody
    public String getVersion(HttpServletRequest request, HttpServletResponse response) {
        // log.info("getVersion");
        StringBuilder sb = new StringBuilder();
        sb.append("{\"java.version\":\"" + System.getProperty("java.version") + "\"}");
        log.info(sb.toString());
        return sb.toString();
    }
    
    /**
     * A simple endpoint to print out host IP address.  It is useful in testing Kubernetes
     * @return
     */
    @RequestMapping(value = "/greeting/hello/{clientType}", method = RequestMethod.GET, produces = "application/json")
    public String hola(@PathVariable String clientType) {
        log.info("calling coding service with client = " + clientType);
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException uhe) {
            hostname = "unknown";
        }
        
        // call coding.hola using different client.  it's a json string
        CodingServiceClient codingServiceClient = codingSerivceClientFactory.getClient(clientType);
        String greetingFromCodingService = codingServiceClient.getHello(codingServiceUsername, codingServicePassword); 
        String jsonStr = 
                "{"
                + "\"hostname\":\"" +  hostname + "\"" + ","
                + "\"exampleProperty\":\"" + exampleProperty + "\"" + ","
                + "\"clientType\":\"" + clientType + "\"" + ","
                + "\"greeting from coding service\":" + greetingFromCodingService 
                + "}";
        log.info(jsonStr);
        return jsonStr;
    }

}
