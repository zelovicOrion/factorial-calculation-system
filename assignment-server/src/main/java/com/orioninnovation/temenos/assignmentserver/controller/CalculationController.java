package com.orioninnovation.temenos.assignmentserver.controller;

import com.orioninnovation.temenos.assignmentserver.dto.StartRequest;
import com.orioninnovation.temenos.assignmentserver.service.CalculationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
