package com.orioninnovation.temenos.assignmentserver.service;

import com.orioninnovation.temenos.assignmentserver.dto.StartRequest;
import com.orioninnovation.temenos.assignmentserver.engine.CalculationManager;
import com.orioninnovation.temenos.assignmentserver.engine.FactorialTask;
import com.orioninnovation.temenos.assignmentserver.model.Calculation;
import com.orioninnovation.temenos.assignmentserver.model.CalculationStatus;
import com.orioninnovation.temenos.assignmentserver.persistence.StorageService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;

@Service
public class CalculationService {
    private final CalculationManager calculationManager;

    public CalculationService(CalculationManager calculationManager) {
        this.calculationManager = calculationManager;
    }

    public String calculate(StartRequest request) throws InterruptedException {
        Calculation calculation = new Calculation(request.getNumber(), request.getThreadCount());
        calculation.setNumber(request.getNumber());
        calculation.setThreadCount(request.getThreadCount());
        String id = calculationManager.register(calculation);

        calculationManager.startCalculation(calculation);

        return id;
    }
}
