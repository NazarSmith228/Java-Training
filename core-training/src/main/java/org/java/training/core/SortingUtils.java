package org.java.training.core;

import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@UtilityClass
public class SortingUtils {

    public <T extends Comparable<? super T>> void mergeSort(List<T> input) {
        verifyModifiable(input);
        mergeSort(input, 0, input.size() - 1);
    }

    public <T extends Comparable<? super T>> void insertionSort(List<T> input) {
        verifyModifiable(input);
        for (int i = 1; i < input.size(); i++) {
            T current = input.get(i);
            int j = i - 1;
            while (j >= 0 && current.compareTo(input.get(j)) < 0) {
                input.set(j + 1, input.get(j));
                j--;
            }
            input.set(j + 1, current);
        }
    }

    public <T, R extends Comparable<? super R>> Comparator<T> comparing(
            Function<? super T, ? extends R> extractionFunction) {
        Objects.requireNonNull(extractionFunction);
        return (t1, t2) -> extractionFunction.apply(t1).compareTo(extractionFunction.apply(t2));
    }

    public <T, R extends Comparable<? super R>> Comparator<T> comparingAndThen(
            Comparator<? super T> other, Function<? super T, ? extends R> extractionFunction) {
        Objects.requireNonNull(other);
        Objects.requireNonNull(extractionFunction);
        return (t1, t2) -> other.compare(t1, t2) == 0
                ? extractionFunction.apply(t1).compareTo(extractionFunction.apply(t2))
                : other.compare(t1, t2);
    }

    private <T extends Comparable<? super T>> void mergeSort(List<T> input, int left, int right) {
        if (right > left) {
            int mid = left + (right - left) / 2;
            mergeSort(input, left, mid);
            mergeSort(input, mid + 1, right);
            merge(input, left, mid, right);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<? super T>> void merge(List<T> input, int left, int mid, int right) {
        var firstHalf = new Object[mid - left + 1];
        var secondHalf = new Object[right - mid];

        for (var i = 0; i < firstHalf.length; i++) {
            firstHalf[i] = input.get(left + i);
        }
        for (var j = 0; j < secondHalf.length; j++) {
            secondHalf[j] = input.get(mid + j + 1);
        }

        int i = 0, j = 0, k = left;
        while (i < firstHalf.length && j < secondHalf.length) {
            T el1 = (T) firstHalf[i];
            T el2 = (T) secondHalf[j];
            if (el1.compareTo(el2) <= 0) {
                input.set(k++, el1);
                i++;
            } else {
                input.set(k++, el2);
                j++;
            }
        }
        while (i < firstHalf.length) {
            input.set(k++, (T) firstHalf[i++]);
        }
        while (j < secondHalf.length) {
            input.set(k++, (T) secondHalf[j++]);
        }
    }

    private <T> void verifyModifiable(List<T> collection) {
        try { //ugly workaround
            collection.add(null);
            collection.remove(collection.size() - 1);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Collection is unmodifiable");
        }
    }
}
