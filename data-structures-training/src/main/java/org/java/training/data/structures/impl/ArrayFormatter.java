package org.java.training.data.structures.impl;

import lombok.experimental.UtilityClass;

import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Utility that prints formatted an array of strings of unspecified length.
 * <p>>
 * Content is divided into {@code n} columns with minimum distance between columns of 4 spaces.
 */
@UtilityClass
public class ArrayFormatter {

    public void printFormatted(String[] elements, int columns) {
        int numberOfElements = elements.length;
        int minDist = 4;
        int[] maxIndexes = new int[columns];

        for (int i = 0; i < columns; i++) {
            int max = 0;
            for (int j = i; j < numberOfElements; j += columns) {
                int currentLength = elements[j].length();
                if (max < currentLength) {
                    max = currentLength;
                    maxIndexes[i] = j;
                }
            }
        }
        for (int i = 0; i < numberOfElements; ) {
            int j = 0;
            while (j < columns && i < numberOfElements) {
                int repeatNumber = elements[maxIndexes[j]].length() - elements[i].length() + minDist;
                if (elements[i].length() == elements[maxIndexes[j]].length()) {
                    repeatNumber = minDist;
                }
                System.out.print(elements[i] + SPACE.repeat(repeatNumber));
                j++;
                i++;
            }
            System.out.println();
        }
    }

    public static void demo() {
        String[] input = {"1", "2", "3", "x", "5", "6", "a", "porosiatko", "c",
                "fdsdfsdfsdfs", "sdfsdq", "12", "13", "14", "15", "16"};
        printFormatted(input, 4);
    }
}

