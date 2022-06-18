package org.java.training.streams.impl.advanced;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@UtilityClass
public class AdvancedStreams {

    public <T> Collection<String> cartesianProduct(Collection<? extends Collection<? extends T>> multiCollection,
                                                   Supplier<? extends Collection<String>> collectionSupplier) {
        return multiCollection.stream()
                .map(collection -> collection.stream()
                        .map(Objects::toString)
                        .collect(toList()))
                .<Supplier<Stream<String>>>map(list -> list::stream)
                .reduce((sup1, sup2) ->
                        () -> sup1.get()
                                .flatMap(t1 -> sup2.get()
                                        .map(t2 -> String.join("|", t1, t2))))
                .orElse(Stream::empty)
                .get()
                .collect(toCollection(collectionSupplier));
    }

    public <K, V> Map<K, List<V>> flattenListOfMaps(List<Map<K, V>> listOfMaps) {
        return listOfMaps.stream()
                .flatMap(kvMap -> kvMap.entrySet().stream())
                .collect(groupingBy(Map.Entry::getKey, mapping(Map.Entry::getValue, toList())));
    }

    public int sumPrimesInRange(int fromIndex, int toIndex) {
        return IntStream.iterate(2, i -> ++i)
                .filter(i -> IntStream.range(2, i).allMatch(j -> i % j != 0))
                .limit(toIndex)
                .skip(fromIndex)
                .sum();
    }

    public <T extends Number> void findFirstPositiveSequence(Collection<T> collection) {
        collection.stream()
                .dropWhile(t -> t.intValue() < 0)
                .takeWhile(t -> t.intValue() > 0)
                .forEach(t -> System.out.printf("%s|", t.toString()));
    }

    public <T extends Comparable<? super T>> void mergeSortWithStream(T[] input) {
        int length = input.length;
        T[] temp = Arrays.copyOf(input, length);

        IntStream.range(1, length)
                .filter(n -> (n & (n - 1)) == 0)
                .forEach(n -> IntStream.iterate(0, i -> i + 2 * n)
                        .limit((length - n) / (2L * n) + 1)
                        .parallel()
                        .forEach(i -> merge(input, temp, i, i + n - 1, Math.min(i + 2 * n - 1, length - 1)))
                );
    }

    private <T extends Comparable<? super T>> void merge(T[] input, T[] temp, int left, int mid, int right) {
        if (right + 1 - left >= 0) {
            System.arraycopy(input, left, temp, left, right + 1 - left);
        }
        int i = left, j = mid + 1;
        for (int k = left; k <= right; k++) {
            if (i > mid) {
                input[k] = temp[j++];
            } else if (j > right) {
                input[k] = temp[i++];
            } else if (temp[i].compareTo(temp[j]) <= 0) {
                input[k] = temp[i++];
            } else {
                input[k] = temp[j++];
            }
        }
    }
}
