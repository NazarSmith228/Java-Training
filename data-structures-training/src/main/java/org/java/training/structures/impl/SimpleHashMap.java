package org.java.training.structures.impl;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleHashMap<K, V> implements Map<K, V> {

    private static final int INITIAL_CAPACITY = 8;
    private static final double LOAD_FACTOR = 0.5;

    Node<K, V>[] table;

    int size;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static final class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

        @Override
        public String toString() {
            var builder = new StringBuilder();
            Node<K, V> current = this;
            builder.append(key).append("=").append(value);

            while ((current = current.next) != null) {
                builder.append(" -> ").append(current.key).append("=").append(current.value);
            }
            return builder.toString();
        }
    }

    public SimpleHashMap() {
        this(INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public SimpleHashMap(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }
        table = new Node[initialCapacity];
        size = 0;
    }

    private int calculateIndex(Object key, int tableCapacity) {
        return (Objects.hash(key) >>> 16) % tableCapacity;
    }

    @Override
    public V put(K key, V value) {
        ensureCapacity();
        int index = calculateIndex(key, table.length);
        if (table[index] == null) {
            table[index] = new Node<>(key, value);
            size++;
            return null;
        }
        Node<K, V> current = table[index];
        K oldKey = current.key;
        V oldVal = current.value;
        if (oldKey.equals(key)) {
            current.value = value;
            return oldVal;
        } else {
            current.next = new Node<>(key, value);
            size++;
            return null;
        }
    }

    private void ensureCapacity() {
        int currentLen = table.length;
        if (currentLen - size <= 2) {
            resizeTable(currentLen + (int) (currentLen * LOAD_FACTOR));
        }
    }

    @Override
    public V get(Object key) {
        int idx = calculateIndex(key, table.length);
        if (table[idx] == null) {
            return null;
        }
        Node<K, V> current = table[idx];
        while (current.next != null) {
            current = current.next;
        }
        return current.value;
    }

    @Override
    public boolean containsKey(Object key) {
        int idx = calculateIndex(key, table.length);
        return table[idx] != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (var node : table) {
            if (containsValue(node, value)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsValue(Node<K, V> node, Object value) {
        Node<K, V> current = node;
        while (current != null) {
            if (current.value.equals(value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public V remove(Object key) {
        int index = calculateIndex(key, table.length);
        if (table[index] == null) {
            return null;
        }
        Node<K, V> current = table[index];
        Node<K, V> prev = current;
        while (current != null) {
            K currentKey = current.key;
            if (currentKey.equals(key)) {
                V retVal = current.value;
                if (current.next != null) {
                    prev.next = current.next;
                } else {
                    prev.next = null;
                }
                return retVal;
            }
            current = current.next;
            prev = prev.next;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        map.forEach(this::put);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        table = new Node[table.length];
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return Arrays.stream(table)
                .map(Node::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values() {
        return Arrays.stream(table)
                .flatMap(this::linkedNodes)
                .map(Node::getValue)
                .collect(Collectors.toList());
    }

    private Stream<Node<K, V>> linkedNodes(Node<K, V> head) {
        var nodes = new LinkedList<Node<K, V>>();
        nodes.addFirst(head);
        while ((head = head.next) != null) {
            nodes.add(head);
        }
        return nodes.stream();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Arrays.stream(table)
                .map(node -> Map.entry(node.key, node.value))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            builder.append(i).append(": ");
            Node<K, V> current = table[i];
            if (current != null) {
                builder.append(current);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    private void resizeTable(int newCapacity) {
        Node<K, V>[] newTable = new Node[newCapacity];
        System.arraycopy(table, 0, newTable, 0, table.length);
        table = newTable;
    }
}
