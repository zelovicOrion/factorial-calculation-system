package com.orioninnovation.temenos.assignmentclient.dto;

public class StartRequest {
    private long number;
    private int threadCount;

    public long getNumber() {
        return  number;
    }

    public int getThreadCount() {
        return  threadCount;
    }

    public void setNumber(long number) {
        this.number = number;
    }
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
}
