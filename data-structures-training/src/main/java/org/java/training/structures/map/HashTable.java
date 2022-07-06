package org.java.training.structures.map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class HashTable<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    Node<K, V>[] table;

    int size;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Node<K, V> {

        @Getter
        final K key;

        @Getter
        V value;

        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

    }

    public HashTable() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        this.size = 0;
        this.table = new Node[capacity];
    }

    public V add(K key, V value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        Node<K, V> newNode = new Node<>(key, value);
        if (table.length - size < 5) {
            resize(size * 2);
        }

        int index = calculateIndex(hash(key));
        if (table[index] == null) {
            size++;
            table[index] = newNode;
        } else {
            Node<K, V> current = table[index];
            Node<K, V> p = current;
            while (p != null) {
                if (p.key.equals(key)) {
                    V oldValue = p.value;
                    p.value = value;
                    return oldValue;
                }
                p = p.next;
                if (current.next != null) {
                    current = current.next;
                }
            }
            Objects.requireNonNull(current).next = new Node<>(key, value);
        }
        return value;
    }

    public List<Node<K, V>> nodes() {
        return Arrays.stream(table).toList();
    }

    private int calculateIndex(int hash) {
        int currentLength = table.length;
        return Math.abs(((currentLength - 1) & hash) % currentLength);
    }

    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        Node<K, V>[] newTable = new Node[newSize];
        for (int i = 0; i < table.length; i++) {
            Node<K, V> current = table[i];
            if (current != null) {
                newTable[i] = table[i];
            }
        }
        this.table = newTable;
    }

    private int hash(K key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    public void content() {
        for (var node : table) {
            if (node != null) {
                System.out.print(node.key + ": " + node.value);
                while ((node = node.next) != null) {
                    System.out.print(" -> ");
                    System.out.print("{" + node.key + ": " + node.value + "}");
                }
                System.out.println();
            }
        }
    }
}
