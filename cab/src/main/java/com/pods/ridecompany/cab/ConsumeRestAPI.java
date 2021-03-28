package com.pods.ridecompany.cab;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Component
public class ConsumeRestAPI {
    RestTemplate restTemplate = new RestTemplate();

    public boolean consumeRideEnded(Long cabId, Long rideId){
        final String URL = "http://localhost:8082/rideEnded?cabId={cabId}&rideId={rideId}";

        Map<String,Long> param = new HashMap<>();
        param.put("cabId",cabId);
        param.put("rideId",rideId);

        return restTemplate.getForObject(URL, Boolean.class, param);
    }
}
