package org.java.training.model;

public record ComplexKey<T, R>(T firstKey, R secondKey) {

    public static <T, R> ComplexKey<T, R> of(T firstKey, R secondKey) {
        return new ComplexKey<>(firstKey, secondKey);
    }
}
