package org.java.training.generics.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HeterogeneousMaxHolder {
    Map<Class<?>, Object> holder = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public <V extends Comparable<? super V>> V getMax(Class<V> clazz) {
        return (V) Optional.ofNullable(holder.get(clazz))
                .orElseThrow(NoSuchElementException::new);
    }

    @SuppressWarnings("unchecked")
    public <V extends Comparable<? super V>> V put(Class<V> clazz, V value) {
        Objects.requireNonNull(clazz);
        return (V) holder.merge(clazz, value, (oldVal, newVal) -> computeMax((V) oldVal, (V) newVal));
    }

    private <V extends Comparable<? super V>> V computeMax(V oldVal, V newVal) {
        return oldVal.compareTo(newVal) > 0 ? oldVal : newVal;
    }
}
