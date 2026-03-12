package com.orioninnovation.temenos.assignmentclient.controller;

import com.orioninnovation.temenos.assignmentclient.dto.StartRequest;
import com.orioninnovation.temenos.assignmentclient.service.GatewayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/gateway")
public class GatewayController {
    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }
    @PostMapping("/start")
    public String start(@RequestBody StartRequest request) {
        return gatewayService.startCalculation(request);
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<String> stop(@PathVariable String id) {
        boolean stopped = gatewayService.stopCalculation(id);
        if (stopped) {
            return ResponseEntity.ok("Calculation stopped successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calculation not found or already completed.");
        }
    }

    @GetMapping("/getStatus/{id}")
    public ResponseEntity<Map<String, String>> getStatus(@PathVariable String id) {
        try {
            String status = gatewayService.getCalculationStatus(id);
            Map<String, String> response = new HashMap<>();
            response.put("id", id);
            response.put("status", status);
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException.NotFound e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Calculation not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        }
    }
    @GetMapping("/getResult/{id}")
    public ResponseEntity<Map<String, String>> getResult(@PathVariable String id) {
        try {
            BigInteger result = gatewayService.getCalculationResult(id);
            Map<String, String> response = new HashMap<>();
            response.put("id", id);
            response.put("result", result.toString());
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException.NotFound e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Calculation not found or not completed");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);

        }
    }

}
