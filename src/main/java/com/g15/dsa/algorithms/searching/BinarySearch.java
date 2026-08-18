package com.g15.dsa.algorithms.searching;

public class BinarySearch {

    public static <T extends Comparable<T>> int search(
            T[] array,
            T target) {

        if (array == null) {
            throw new IllegalArgumentException(
                    "Array cannot be null"
            );
        }

        if (target == null) {
            return -1;
        }

        int left = 0;
        int right = array.length - 1;

        while (left <= right) {

            int middle =
                    left + (right - left) / 2;

            int comparison =
                    target.compareTo(array[middle]);

            if (comparison == 0) {
                return middle;
            }

            if (comparison < 0) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        return -1;
    }
}