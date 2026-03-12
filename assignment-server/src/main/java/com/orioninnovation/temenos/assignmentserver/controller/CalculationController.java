package com.orioninnovation.temenos.assignmentserver.controller;

import com.orioninnovation.temenos.assignmentserver.dto.StartRequest;
import com.orioninnovation.temenos.assignmentserver.service.CalculationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/factorial")
public class CalculationController {
    private final CalculationService calculationService;

    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }
    @PostMapping("/start")
    public String start(@RequestBody StartRequest request) throws InterruptedException {

        return calculationService.calculate(request).toString();
    }
    @PostMapping("/stop/{id}")
    public ResponseEntity<String> stop(@PathVariable String id) {
        boolean stopped = calculationService.stopCalculation(id);
        if(stopped) {
            return ResponseEntity.ok("Calculation stopped successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Calculation not found or already completed.");
        }
    }

}
