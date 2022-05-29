package org.java.training.streams.impl.advanced;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@UtilityClass
public class AdvancedStreams {

    public <T> Collection<String> cartesianProduct(Collection<? extends Collection<? extends T>> multiCollection,
                                                   Supplier<? extends Collection<String>> collectionSupplier) {
        return multiCollection.stream()
                .map(collection -> collection.stream().map(Objects::toString).collect(toList()))
                .<Supplier<Stream<String>>>map(list -> list::stream)
                .reduce((sup1, sup2) ->
                        () -> sup1.get().flatMap(t1 -> sup2.get().map(t2 -> String.join("|", t1, t2))))
                .orElse(Stream::empty)
                .get()
                .collect(Collectors.toCollection(collectionSupplier));
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
}
