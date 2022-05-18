package org.java.training.streams;

import org.java.training.streams.impl.advanced.CartesianProduct;
import org.java.training.streams.impl.collectors.CollectorsUtils;

import java.util.Scanner;

public class Demo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();

        switch (input) {
            case 1 -> CartesianProduct.execute();
            case 2 -> CollectorsUtils.grouping();
            case 3 -> CollectorsUtils.partitioning();
            default -> throw new IllegalArgumentException();
        }
    }
}
