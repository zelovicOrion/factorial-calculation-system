package com.orioninnovation.temenos.assignmentclient.controller;

import com.orioninnovation.temenos.assignmentclient.dto.*;
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
    public ResponseEntity<ApiResponse<StartResponse>> start(@RequestBody StartRequest request) {
        String id = gatewayService.startCalculation(request);
        return ResponseEntity.ok(new ApiResponse<>(true, new StartResponse(id), null));
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<ApiResponse<String>> stop(@PathVariable String id) {
        boolean stopped = gatewayService.stopCalculation(id);
        if (stopped) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Calculation stopped successfully.", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, null, "Calculation not found or already completed."));
        }
    }

    @GetMapping("/getStatus/{id}")
    public ResponseEntity<ApiResponse<StatusResponse>> getStatus(@PathVariable String id) {
        try {
            String status = gatewayService.getCalculationStatus(id);
            return ResponseEntity.ok(new ApiResponse<>(true, new StatusResponse(id, status), null));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, null, "Calculation not found"));
        }
    }
    @GetMapping("/getResult/{id}")
    public ResponseEntity<ApiResponse<ResultResponse>> getResult(@PathVariable String id) {
        try {
            BigInteger result = gatewayService.getCalculationResult(id);
            return ResponseEntity.ok(new ApiResponse<>(true, new ResultResponse(id, result.toString()), null));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, null, "Calculation not found or not completed"));
        }
    }

}
