package org.java.training.data.structures.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class HashTable<T> {

    Node<T>[] table;
    int size;

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class Node<T> {

        final T value;
        Node<T> next;

        public Node(T value) {
            this.value = value;
        }

    }

    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        this.size = 0;
        this.table = new Node[capacity];
    }

    public boolean add(T element) {
        Node<T> newNode = new Node<>(Objects.requireNonNull(element));
        if (table.length - size < 5) {
            resize(size * 2);
        }

        int index = calculateIndex(hash(element));
        if (table[index] == null) {
            size++;
            table[index] = newNode;
        } else {
            Node<T> current = table[index];
            if (current.value.equals(element)) {
                return false;
            }
            while (current.next != null) {
                current = current.next;
                if (current.value.equals(element)) {
                    return false;
                }
            }
            current.next = newNode;
        }
        return true;
    }

    private int calculateIndex(int hash) {
        int currentLength = table.length;
        return Math.abs(((currentLength - 1) & hash) % currentLength);
    }

    @SuppressWarnings("unchecked")
    private void resize(int newSize) {
        Node<T>[] newTable = new Node[newSize];
        for (int i = 0; i < table.length; i++) {
            Node<T> current = table[i];
            if (current != null) {
                newTable[i] = table[i];
            }
        }
        this.table = newTable;
    }

    private int hash(T element) {
        int h;
        return (element == null) ? 0 : (h = element.hashCode()) ^ (h >>> 16);
    }

    public void content() {
        for (int i = 0; i < table.length; i++) {
            Node<T> current = table[i];
            if (current != null) {
                System.out.print(i + ": " + current.value);
                while (current.next != null) {
                    System.out.print("->");
                    current = current.next;
                    System.out.print(current.value);
                }
                System.out.println();
            }
        }
    }

}
