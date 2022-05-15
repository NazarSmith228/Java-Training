package org.java.training.data.structures.impl;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayFormatter {

    private final String WHITESPACE = " ";

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
                System.out.print(elements[i] + WHITESPACE.repeat(repeatNumber));
                j++;
                i++;
            }
            System.out.println();
        }
    }
}

