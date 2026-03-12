package com.orioninnovation.temenos.assignmentserver.model;

import lombok.Getter;

import java.math.BigInteger;

public class Calculation {
    @Getter
    private String id;
    private long number;
    private int threadCount;
    private CalculationStatus status;
    private BigInteger result;

    public Calculation(long number, int threadCount) {
        this.number = number;
        this.threadCount = threadCount;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(CalculationStatus status) {
        this.status = status;
    }

    public void setResult(BigInteger result) {
        this.result = result;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public CalculationStatus getStatus() {
        return status;
    }

    public BigInteger getResult() {
        return result;
    }
}
