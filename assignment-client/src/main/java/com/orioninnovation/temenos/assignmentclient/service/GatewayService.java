package com.orioninnovation.temenos.assignmentclient.service;

import com.orioninnovation.temenos.assignmentclient.dto.StartRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
    public boolean stopCalculation(String id) {
        String serverUrl = "http://localhost:8080/factorial/stop/" + id;

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, null, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false; // not found or already completed
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Server error: " + e.getMessage());
        }
    }
}
