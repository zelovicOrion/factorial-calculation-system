package com.orioninnovation.temenos.assignmentserver.engine;

import com.orioninnovation.temenos.assignmentserver.model.CalculationStatus;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;
import com.orioninnovation.temenos.assignmentserver.model.Calculation;

public class FactorialTask extends RecursiveTask<BigInteger> {
    private final long start;
    private final long end;
    private final Calculation calculation;

    public FactorialTask(long start, long end, Calculation calculation) {
        this.start = start;
        this.end = end;
        this.calculation = calculation;

    }

    @Override
    protected BigInteger compute() {
        if (end - start <= 20) {
            BigInteger result = BigInteger.ONE;

            for(long i = start; i <=end; i++){
                if (calculation.getStatus() == CalculationStatus.STOPPED) {
                    throw new RuntimeException("Calculation stopped by user");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Calculation interrupted");
                }
                result = result.multiply(BigInteger.valueOf(i));
            }
            return result;
        }
        long mid = (start + end) / 2;

        FactorialTask left = new FactorialTask(start, mid, calculation);
        FactorialTask right = new FactorialTask(mid + 1, end, calculation);

        left.fork();
        BigInteger rightResult = right.compute();

        return left.join().multiply(rightResult);
    }
}
