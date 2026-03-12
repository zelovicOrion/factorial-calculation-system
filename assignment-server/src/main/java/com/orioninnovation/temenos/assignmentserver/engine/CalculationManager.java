package com.orioninnovation.temenos.assignmentserver.engine;

import com.orioninnovation.temenos.assignmentserver.model.Calculation;
import com.orioninnovation.temenos.assignmentserver.model.CalculationStatus;
import com.orioninnovation.temenos.assignmentserver.persistence.StorageService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Semaphore;

@Service
public class CalculationManager {

    private final Map<String, Calculation> calculations = new ConcurrentHashMap<>();
    private final Semaphore semaphore = new Semaphore(1);
    private final StorageService storageService;

    public CalculationManager(StorageService storageService) {
        this.storageService = storageService;
    }

    public String register(Calculation calculation) throws InterruptedException {
        semaphore.acquire();
        try {
            String id = UUID.randomUUID().toString();
            calculation.setId(id);
            calculation.setStatus(CalculationStatus.PENDING);
            calculations.put(id, calculation);
            storageService.save(calculation);
            return id;
        } finally {
            semaphore.release();
        }
    }

    public void startCalculation(Calculation calculation) {
        calculation.setStatus(CalculationStatus.RUNNING);
        storageService.update(calculation);
        
        ForkJoinPool pool = new ForkJoinPool(calculation.getThreadCount());

        CompletableFuture.runAsync(() -> {
            try {
                FactorialTask task = new FactorialTask(1, calculation.getNumber(), calculation);
                BigInteger result = pool.invoke(task);
                calculation.setResult(result);
                calculation.setStatus(CalculationStatus.COMPLETED);
                storageService.update(calculation);
            } catch (RuntimeException e) {
                if ("Calculation stopped by user".equals(e.getCause() != null ? e.getCause().getMessage() : e.getMessage())) {
                    calculation.setStatus(CalculationStatus.STOPPED);
                    storageService.update(calculation);
                } else {
                    calculation.setStatus(CalculationStatus.FAILED);
                    storageService.update(calculation);
                }
            }
        });
        calculations.put(calculation.getId(), calculation);
    }
    public boolean stopCalculation(String id) {
        Calculation calculation = calculations.get(id);
        if(calculation == null) {
            return false;
        }
        if(calculation.getStatus() == CalculationStatus.RUNNING) {
            calculation.setStatus(CalculationStatus.STOPPED);
            storageService.update(calculation);
            return true;
        }
        return false;
    }
    public Calculation getCalculationById(String id) {
        return calculations.get(id);
    }
}
