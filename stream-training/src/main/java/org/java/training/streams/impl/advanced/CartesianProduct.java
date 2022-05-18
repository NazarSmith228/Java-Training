package org.java.training.streams.impl.advanced;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@UtilityClass
public class CartesianProduct {

    public List<String> cartesianProduct(List<List<String>> lists) {
        Supplier<Stream<String>> streamSupplier = lists.stream()
                .<Supplier<Stream<String>>>map(list -> list::stream)
                .reduce((supplier1, supplier2) ->
                        () -> supplier1.get().flatMap(el1 -> supplier2.get()
                                .map(el1::concat)))
                .orElse(() -> Stream.of(StringUtils.EMPTY));

        return streamSupplier.get().collect(toList());
    }

    public void execute() {
        List<List<String>> lists = List.of(
                List.of("a", "b", "c"),
                List.of("1", "2", "3"),
                List.of("x", "y", "z")
        );

        String cartesianProduct = cartesianProduct(lists).stream()
                .collect(joining("|", "[", "]"));
        System.out.println(cartesianProduct);
    }

}
