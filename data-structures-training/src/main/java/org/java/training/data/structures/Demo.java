package org.java.training.data.structures;

import org.java.training.data.structures.impl.ArrayFormatter;
import org.java.training.data.structures.impl.BinaryTree;
import org.java.training.data.structures.impl.HashTable;

import java.util.Scanner;

public class Demo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        switch (input) {
            case 1 -> ArrayFormatter.demo();
            case 2 -> HashTable.Demo.execute();
            case 3 -> BinaryTree.Demo.execute();
            default -> throw new IllegalArgumentException();
        }

    }
}
