package org.java.training.streams.collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class ToMapCase {

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>(Arrays.asList("Astring", "Bstring", "Cstring"));

        Map<Character, String> collect = strings.stream()
                .collect(toMap(s -> s.charAt(0), Function.identity()));

        strings.add("Astringggh");

        Map<Character, String> collect1 = strings.stream()
                .collect(toMap(s -> s.charAt(0), Function.identity(), (s1, s2) -> String.format("%s|%s", s1, s2)));

        System.out.println(collect);
        System.out.println(collect1);
    }
}
