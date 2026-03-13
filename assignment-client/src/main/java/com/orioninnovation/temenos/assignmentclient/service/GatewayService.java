package com.orioninnovation.temenos.assignmentclient.service;

import com.orioninnovation.temenos.assignmentclient.dto.ResultResponse;
import com.orioninnovation.temenos.assignmentclient.dto.StartRequest;
import com.orioninnovation.temenos.assignmentclient.dto.StartResponse;
import com.orioninnovation.temenos.assignmentclient.dto.StatusResponse;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${server.factorial.url}")
    private String baseUrl;
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
        StartResponse response = restTemplate.postForObject(baseUrl + "/start", request, StartResponse.class);
        if(response == null) {
            return null;
        }
        return response.getId();
    }
    public boolean stopCalculation(String id) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/stop/" + id, null, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Server error: " + e.getMessage());
        }
    }
    public String getCalculationStatus(String id) {
        StatusResponse response = restTemplate.getForObject(baseUrl + "/status/" + id, StatusResponse.class);
        if (response == null || response.getStatus() == null) {
            return null;
        }
        return response.getStatus();
    }
    public BigInteger getCalculationResult(String id) {
        ResultResponse response = restTemplate.getForObject(baseUrl + "/result/" + id, ResultResponse.class);
        if (response == null || response.getResult() == null) {
            return null;
        }
        return new BigInteger(response.getResult());
    }
}
