package com.orioninnovation.temenos.assignmentclient.service;

import com.orioninnovation.temenos.assignmentclient.dto.StartRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Map;

@Service
public class GatewayService {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8080/factorial";

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
        return restTemplate.postForObject(BASE_URL + "/start", request, String.class);
    }
    public boolean stopCalculation(String id) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL + "/stop/" + id, null, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Server error: " + e.getMessage());
        }
    }
    public String getCalculationStatus(String id) {
        Map<String, String> response = restTemplate.getForObject(BASE_URL + "/status/" + id, Map.class);
        if (response == null || !response.containsKey("status")) {
            return null;
        }
        return response.get("status");
    }
    public BigInteger getCalculationResult(String id) {
        Map<String, String> response = restTemplate.getForObject(BASE_URL + "/result/" + id, Map.class);
        if (response == null || !response.containsKey("result")) {
            return null;
        }
        return new BigInteger(response.get("result"));
    }
}
