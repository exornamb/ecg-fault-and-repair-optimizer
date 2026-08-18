package com.g15.dsa.algorithms.sorting;

public class MergeSort {

    public static <T extends Comparable<T>> void sort(T[] array) {

        if (array == null) {
            throw new IllegalArgumentException(
                    "Array cannot be null"
            );
        }

        if (array.length < 2) {
            return;
        }

        T[] temporary =
                array.clone();

        mergeSort(
                array,
                temporary,
                0,
                array.length - 1
        );
    }

    private static <T extends Comparable<T>> void mergeSort(
            T[] array,
            T[] temporary,
            int left,
            int right) {

        if (left >= right) {
            return;
        }

        int middle =
                left + (right - left) / 2;

        mergeSort(
                array,
                temporary,
                left,
                middle
        );

        mergeSort(
                array,
                temporary,
                middle + 1,
                right
        );

        merge(
                array,
                temporary,
                left,
                middle,
                right
        );
    }

    private static <T extends Comparable<T>> void merge(
            T[] array,
            T[] temporary,
            int left,
            int middle,
            int right) {

        for (int i = left; i <= right; i++) {
            temporary[i] = array[i];
        }

        int i = left;
        int j = middle + 1;
        int k = left;

        while (i <= middle && j <= right) {

            if (temporary[i].compareTo(
                    temporary[j]
            ) <= 0) {

                array[k++] =
                        temporary[i++];

            } else {

                array[k++] =
                        temporary[j++];
            }
        }

        while (i <= middle) {
            array[k++] =
                    temporary[i++];
        }

        while (j <= right) {
            array[k++] =
                    temporary[j++];
        }
    }
}