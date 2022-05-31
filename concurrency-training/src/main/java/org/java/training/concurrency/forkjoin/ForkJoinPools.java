package org.java.training.concurrency.forkjoin;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

@UtilityClass
public class ForkJoinPools {

    private final ForkJoinPool POOL = ForkJoinPool.commonPool();

    public <T extends Comparable<? super T>> void mergeSort(T[] arr) {
        System.out.println("Input: " + Arrays.toString(arr));
        POOL.invoke(new MergeAction<>(arr, arr.length));
        System.out.println("Sorted: " + Arrays.toString(arr));
    }

    public <T extends Number> void accumulateNumbers(T[] numbers) {

        var accumulator =
                switch (numbers[0]) {
                    case Byte ignoredByte ->
                            new NumberAccumulator<>((Byte[]) numbers, (byte) 0, (b1, b2) -> (byte) (b1 + b2));
                    case Short ignoredShort ->
                            new NumberAccumulator<>((Short[]) numbers, (short) 0, (s1, s2) -> (short) (s1 + s2));
                    case Integer ignoredInt -> new NumberAccumulator<>((Integer[]) numbers, 0, Integer::sum);
                    case Long ignoredLong -> new NumberAccumulator<>((Long[]) numbers, 0L, Long::sum);
                    case Double ignoredDouble -> new NumberAccumulator<>((Double[]) numbers, 0D, Double::sum);
                    case null, default -> throw new IllegalArgumentException();
                };

        System.out.println("Input type: " + numbers.getClass().getSimpleName());
        System.out.println("Input array: " + Arrays.toString(numbers));
        Number result = POOL.invoke(accumulator);
        System.out.println("Accumulation result: " + result);
    }
}
