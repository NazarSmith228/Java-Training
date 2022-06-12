package org.java.training.concurrency.forkjoin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

@RequiredArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FibonacciCounter extends RecursiveTask<BigInteger> {

    int targetIndex;

    @Override
    protected BigInteger compute() {
        if (targetIndex == 0 || targetIndex == 1) {
            return BigInteger.valueOf(targetIndex);
        }

        FibonacciCounter prevCounter = FibonacciCounter.of(targetIndex - 1);
        FibonacciCounter prevPrevCounter = FibonacciCounter.of(targetIndex - 2);

        prevPrevCounter.fork();

        return prevCounter.compute().add(prevPrevCounter.join());

    }
}
