package org.java.training.concurrency.forkjoin;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.concurrent.RecursiveTask;
import java.util.function.BinaryOperator;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NumberAccumulator<T extends Number> extends RecursiveTask<T> {

    T[] numbers;

    T identity;

    BinaryOperator<T> accumulator;

    @Override
    protected T compute() {
        if (numbers.length <= 10) {
            return Arrays.stream(numbers).reduce(identity, accumulator);
        }

        NumberAccumulator<T> firstTask = new NumberAccumulator<>(
                Arrays.copyOfRange(numbers, 0, numbers.length / 10), identity, accumulator);
        NumberAccumulator<T> secondTask = new NumberAccumulator<>(
                Arrays.copyOfRange(numbers, numbers.length / 10, numbers.length), identity, accumulator);

        secondTask.fork();

        return accumulator.apply(firstTask.compute(), secondTask.join());
    }
}
