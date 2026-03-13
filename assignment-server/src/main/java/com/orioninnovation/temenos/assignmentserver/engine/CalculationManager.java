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
    private final Semaphore mutex = new Semaphore(1);
    private final Semaphore rwLock = new Semaphore(1);
    private int readCount = 0;
    private final StorageService storageService;

    public CalculationManager(StorageService storageService) {
        this.storageService = storageService;
    }

    public String register(Calculation calculation) throws InterruptedException {
        rwLock.acquire();
        try {
            String id = UUID.randomUUID().toString();
            calculation.setId(id);
            calculation.setStatus(CalculationStatus.PENDING);
            calculations.put(id, calculation);
            storageService.save(calculation);
            return id;
        } finally {
            rwLock.release();
        }
    }

    public void startCalculation(Calculation calculation) throws InterruptedException {
        rwLock.acquire();
        try {
            calculation.setStatus(CalculationStatus.RUNNING);
            storageService.update(calculation);

            ForkJoinPool pool = new ForkJoinPool(calculation.getThreadCount());

            CompletableFuture.runAsync(() -> {
                try {
                    FactorialTask task = new FactorialTask(1, calculation.getNumber(), calculation);
                    BigInteger result = pool.invoke(task);
                    if (calculation.getStatus() == CalculationStatus.RUNNING) {
                        calculation.setResult(result);
                        calculation.setStatus(CalculationStatus.COMPLETED);
                        storageService.update(calculation);
                    }
                } catch (RuntimeException e) {
                    if (calculation.getStatus() != CalculationStatus.STOPPED) {
                        calculation.setStatus(CalculationStatus.FAILED);
                    }
                    storageService.update(calculation);
                } finally {
                    pool.shutdown();
                }
            }, pool);
            calculations.put(calculation.getId(), calculation);
        } finally {
            rwLock.release();
        }
    }
    public boolean stopCalculation(String id) throws InterruptedException {
        rwLock.acquire();
        try {
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
        } finally {
            rwLock.release();
        }
    }
    public Calculation getCalculationById(String id) throws InterruptedException {
        mutex.acquire();
        readCount++;
        if(readCount == 1) {
            rwLock.acquire();
        }
        mutex.release();
        Calculation calculation = calculations.get(id);
        mutex.acquire();
        readCount--;
        if(readCount == 0){
            rwLock.release();
        }
        mutex.release();

        return calculation;
    }
}
