package org.java.training.structures.graph;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.format;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SimpleGraph<V extends Comparable<? super V>> implements Graph<V> {

    Map<V, Vertex<V>> verticesMap;
    AtomicInteger size;

    @Data
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    private static class Vertex<E> {

        E value;
        List<Vertex<E>> adjacentVertices;

        public Vertex(E value) {
            this.value = value;
            this.adjacentVertices = new CopyOnWriteArrayList<>();
        }

    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> SimpleGraph<T> of(T... values) {
        SimpleGraph<T> graph = new SimpleGraph<>();
        Arrays.stream(values)
                .forEach(graph::add);

        return graph;
    }

    public SimpleGraph() {
        this.verticesMap = new ConcurrentHashMap<>();
        this.size = new AtomicInteger(0);
    }

    @Override
    public void add(V value) {
        if (contains(value)) {
            throw new IllegalArgumentException(format("Graph already contains vertex %s", value));
        }

        Vertex<V> vertex = new Vertex<>(value);
        verticesMap.put(value, vertex);
        size.incrementAndGet();
    }

    @Override
    public void makeEdge(V from, V to) {
        @NonNull Vertex<V> fromVertex = verticesMap.get(from);
        @NonNull Vertex<V> toVertex = verticesMap.get(to);

        fromVertex.getAdjacentVertices().add(toVertex);
        toVertex.getAdjacentVertices().add(fromVertex);
    }

    @Override
    public int findDistanceBetween(V from, V to) {
        // TODO: impl
        return 0;
    }

    @Override
    public V remove(V value) {
        if (!contains(value)) {
            throw new IllegalArgumentException(format("Graph has no such vertex %s", value));
        }
        Vertex<V> removed = verticesMap.remove(value);
        size.decrementAndGet();
        return removed.getValue();
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public boolean contains(V value) {
        return verticesMap.containsKey(value);
    }
}
