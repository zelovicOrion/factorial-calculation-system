package com.orioninnovation.temenos.assignmentserver.dto;

public class StartRequest {
    private long number;
    private int threadCount;

    public StartRequest(long number, int threadCount) {
        this.number = number;
        this.threadCount = threadCount;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}
