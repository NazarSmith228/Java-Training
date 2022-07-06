package org.java.training.structures.util;

import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.util.Stack;

@UtilityClass
public class LinkedListUtils {

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @ToString(exclude = "next", includeFieldNames = false)
    private static class Node<T> {

        final T value;
        Node<T> next;

        private Node(T value) {
            this.value = value;
        }

        public static <T> Node<T> of(T value) {
            return new Node<>(value);
        }
    }

    @SafeVarargs
    public <T> Node<T> createLinkedList(T... elements) {
        Node<T> head = Node.of(elements[0]);
        Node<T> current = head;
        for (int i = 1; i < elements.length; i++) {
            Node<T> next = Node.of(elements[i]);
            current.next = next;
            current = next;
        }
        return head;
    }

    public <T> Node<T> reverseRecursively(Node<T> head) {
        if (head.next != null) {
            Node<T> newHead = reverseRecursively(head.next);
            if (newHead.next != null) {
                Node<T> last = traverse(newHead.next);
                last.next = head;
            } else {
                newHead.next = head;
            }
            head.next = null;
            return newHead;
        } else {
            return head;
        }
    }

    private <T> Node<T> traverse(Node<T> head) {
        if (head.next == null) {
            return head;
        } else {
            return traverse(head.next);
        }
    }

    public <T> Node<T> reverseUsingStack(Node<T> head) {
        Stack<Node<T>> nodeStack = new Stack<>();
        while (head != null) {
            nodeStack.push(head);
            head = head.next;
        }
        Node<T> newHead = nodeStack.pop();
        Node<T> current = newHead;
        while (!nodeStack.empty()) {
            Node<T> retrieved = nodeStack.pop();
            retrieved.next = null;
            current.next = retrieved;
            current = retrieved;
        }
        return newHead;
    }

    public <T> Node<T> reverseIterative(Node<T> head) {
        Node<T> newHead = null;
        Node<T> prevHead = head;
        while (head != null) {
            head = head.next;
            prevHead.next = newHead;
            newHead = prevHead;
            prevHead = head;
        }
        return newHead;
    }

    public void printLinkedList(Node<?> head) {
        System.out.print(head.value + " -> ");
        while ((head = head.next) != null) {
            System.out.print(head.value);
            if (head.next != null) {
                System.out.print(" -> ");
            }
        }
    }
}
