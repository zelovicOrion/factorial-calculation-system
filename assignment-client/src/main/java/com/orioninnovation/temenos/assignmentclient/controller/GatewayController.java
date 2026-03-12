package com.orioninnovation.temenos.assignmentclient.controller;

import com.orioninnovation.temenos.assignmentclient.dto.StartRequest;
import com.orioninnovation.temenos.assignmentclient.service.GatewayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
