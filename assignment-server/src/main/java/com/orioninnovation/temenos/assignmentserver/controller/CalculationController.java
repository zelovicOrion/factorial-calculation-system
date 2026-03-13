package com.orioninnovation.temenos.assignmentserver.controller;

import com.orioninnovation.temenos.assignmentserver.dto.StartRequest;
import com.orioninnovation.temenos.assignmentserver.model.Calculation;
import com.orioninnovation.temenos.assignmentserver.service.CalculationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/factorial")
public class CalculationController {
    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startCalculation(@RequestBody StartRequest request) throws InterruptedException {

        String id = calculationService.startCalculation(request).toString();
        return ResponseEntity.ok(Map.of("id", id));
    }
    @PostMapping("/stop/{id}")
    public ResponseEntity<String> stopCalculation(@PathVariable String id) throws InterruptedException {
        boolean stopped = calculationService.stopCalculation(id);
        if(stopped) {
            return ResponseEntity.ok("Calculation stopped successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calculation not found or already completed.");
        }
    }
    @GetMapping("/status/{id}")
    public ResponseEntity<Map<String, String>> getCalculationStatus(@PathVariable String id) throws InterruptedException {
        Calculation calculation = calculationService.getCalculationById(id);

        if (calculation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Calculation not found"));
        }

        return ResponseEntity.ok(Map.of(
                "id", calculation.getId(),
                "status", calculation.getStatus().name()
        ));
    }
    @GetMapping("/result/{id}")
    public ResponseEntity<Map<String, String>> getCalculationResult(@PathVariable String id) throws InterruptedException {
        Calculation calculation = calculationService.getCalculationById(id);
        if (calculation == null || calculation.getResult() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Calculation not found or not completed"));
        }
        return ResponseEntity.ok(Map.of(
                "id", calculation.getId(),
                "result", calculation.getResult().toString()
        ));
    }


}
