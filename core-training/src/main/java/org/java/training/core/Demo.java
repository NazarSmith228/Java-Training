package org.java.training.core;

import org.java.training.core.impl.HeterogeneousMaxHolder;
import org.java.training.core.impl.RandomFieldComparator;

import java.util.Scanner;

public class Demo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        switch (input) {
            case 1 -> HeterogeneousMaxHolder.Demo.execute();
            case 2 -> RandomFieldComparator.Demo.execute();
            default -> throw new IllegalArgumentException();
        }
    }
}
