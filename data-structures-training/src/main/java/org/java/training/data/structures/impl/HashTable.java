package org.java.training.data.structures.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class HashTable<K, V> {

    Node<K, V>[] table;
    int size;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class Node<K, V> {

        final K key;
        V value;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }

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
            current.next = new Node<>(key, value);
        }
        return value;
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
                    System.out.print("->" + node.value);
                }
                System.out.println();
            }
        }
    }

    @UtilityClass
    public static class Demo {

        public void execute() {
            var table = new HashTable<String, Integer>(5) {{
                add("AaAaAa", 1);
                add("AaAaBB", 2);
                add("AaAaAa", 3);
                add("AaBBAa", 4);
                add("AaBBBB", 5);
                add("AAa", 6);
                add("bbb", 8);
                add("BBb", 9);
                add("bBB", 7);
                add("aaAA", 10);
                add("cCCc", 11);
            }};

            table.content();
        }
    }
}
