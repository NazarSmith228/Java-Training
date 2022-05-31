package org.java.training.concurrency.forkjoin;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MergeAction<T extends Comparable<? super T>> extends RecursiveAction {

    @NonNull T[] arr;
    int size;

    @Override
    protected void compute() {
        if (size > 1) {
            T[] leftSubArray = Arrays.copyOfRange(arr, 0, arr.length / 2);
            T[] rightSubArray = Arrays.copyOfRange(arr, arr.length / 2, arr.length);

            MergeAction<T> leftAction = new MergeAction<>(leftSubArray, leftSubArray.length);
            MergeAction<T> rightAction = new MergeAction<>(rightSubArray, rightSubArray.length);

            leftAction.fork();
            rightAction.compute();
            leftAction.join();

            merge(arr, leftSubArray, rightSubArray);
        }
    }

    private void merge(T[] target, T[] leftSubArray, T[] rightSubArray) {
        var i = 0;
        var j = 0;
        while (i < leftSubArray.length && j < rightSubArray.length) {
            target[i + j] = leftSubArray[i].compareTo(rightSubArray[j]) <= 0
                    ? leftSubArray[i++]
                    : rightSubArray[j++];
        }
        System.arraycopy(leftSubArray, i, target, i + j, leftSubArray.length - i);
        System.arraycopy(rightSubArray, j, target, i + j, rightSubArray.length - j);
    }
}
