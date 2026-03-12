package com.orioninnovation.temenos.assignmentclient.service;

import com.orioninnovation.temenos.assignmentclient.dto.StartRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GatewayService {

    private final RestTemplate restTemplate;

    public GatewayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String startCalculation(StartRequest request) {
        if(request.getNumber() <= 0) {
            throw new IllegalArgumentException("Number must be greater than zero");
        }
        if(request.getThreadCount() <= 0) {
            throw new IllegalArgumentException("Thread count must be greater than zero");
        }
        String serverUrl = "http://localhost:8080/factorial/start";

        return restTemplate.postForObject(serverUrl, request, String.class);
    }
}
