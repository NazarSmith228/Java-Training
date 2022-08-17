package org.java.training.structures.graph;

public interface Graph<V> {

    void add(V value);

    void makeEdge(V from, V to);

    int findDistanceBetween(V from, V to);

    V remove(V value);

    int size();

    boolean contains(V value);

}
