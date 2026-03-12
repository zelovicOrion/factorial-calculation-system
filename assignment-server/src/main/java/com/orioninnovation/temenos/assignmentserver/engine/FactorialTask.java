package com.orioninnovation.temenos.assignmentserver.engine;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

public class FactorialTask extends RecursiveTask<BigInteger> {
    private final long start;
    private final long end;

    public FactorialTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected BigInteger compute() {
        if (end - start <= 20) {
            BigInteger result = BigInteger.ONE;

            for(long i = start; i <=end; i++){
                if(Thread.currentThread().isInterrupted()) {
                    throw new RuntimeException("Calculation interrupted");
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Calculation interrupted");
                }
                result = result.multiply(BigInteger.valueOf(i));
            }
            return result;
        }
        long mid = (start + end) / 2;

        FactorialTask left = new FactorialTask(start, mid);
        FactorialTask right = new FactorialTask(mid + 1, end);

        left.fork();
        BigInteger rightResult = right.compute();

        return left.join().multiply(rightResult);
    }
}
