package algorithms.sorting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SortingAlgorithmsTest {

    // =========================================================
    // SELECTION SORT
    // =========================================================

    @Test
    void selectionSortSortsUnsortedArray() {
        Integer[] values = {64, 25, 12, 22, 11};

        SelectionSort.sort(values);

        assertArrayEquals(
                new Integer[]{11, 12, 22, 25, 64},
                values
        );
    }

    @Test
    void selectionSortHandlesAlreadySortedArray() {
        Integer[] values = {1, 2, 3, 4, 5};

        SelectionSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 3, 4, 5},
                values
        );
    }

    @Test
    void selectionSortHandlesReverseOrder() {
        Integer[] values = {5, 4, 3, 2, 1};

        SelectionSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 3, 4, 5},
                values
        );
    }

    @Test
    void selectionSortHandlesDuplicates() {
        Integer[] values = {4, 2, 4, 1, 2};

        SelectionSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 2, 4, 4},
                values
        );
    }


    // =========================================================
    // INSERTION SORT
    // =========================================================

    @Test
    void insertionSortSortsUnsortedArray() {
        Integer[] values = {12, 11, 13, 5, 6};

        InsertionSort.sort(values);

        assertArrayEquals(
                new Integer[]{5, 6, 11, 12, 13},
                values
        );
    }

    @Test
    void insertionSortHandlesAlreadySortedArray() {
        Integer[] values = {1, 2, 3, 4, 5};

        InsertionSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 3, 4, 5},
                values
        );
    }

    @Test
    void insertionSortHandlesReverseOrder() {
        Integer[] values = {5, 4, 3, 2, 1};

        InsertionSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 3, 4, 5},
                values
        );
    }

    @Test
    void insertionSortHandlesDuplicates() {
        Integer[] values = {3, 1, 3, 2, 1};

        InsertionSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 1, 2, 3, 3},
                values
        );
    }


    // =========================================================
    // MERGE SORT
    // =========================================================

    @Test
    void mergeSortSortsUnsortedArray() {
        Integer[] values = {
                38, 27, 43, 3, 9, 82, 10
        };

        MergeSort.sort(values);

        assertArrayEquals(
                new Integer[]{3, 9, 10, 27, 38, 43, 82},
                values
        );
    }

    @Test
    void mergeSortHandlesAlreadySortedArray() {
        Integer[] values = {1, 2, 3, 4, 5};

        MergeSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 3, 4, 5},
                values
        );
    }

    @Test
    void mergeSortHandlesReverseOrder() {
        Integer[] values = {5, 4, 3, 2, 1};

        MergeSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 3, 4, 5},
                values
        );
    }

    @Test
    void mergeSortHandlesDuplicates() {
        Integer[] values = {4, 1, 4, 2, 1};

        MergeSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 1, 2, 4, 4},
                values
        );
    }


    // =========================================================
    // QUICK SORT
    // =========================================================

    @Test
    void quickSortSortsUnsortedArray() {
        Integer[] values = {
                10, 7, 8, 9, 1, 5
        };

        QuickSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 5, 7, 8, 9, 10},
                values
        );
    }

    @Test
    void quickSortHandlesAlreadySortedArray() {
        Integer[] values = {1, 2, 3, 4, 5};

        QuickSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 3, 4, 5},
                values
        );
    }

    @Test
    void quickSortHandlesReverseOrder() {
        Integer[] values = {5, 4, 3, 2, 1};

        QuickSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 2, 3, 4, 5},
                values
        );
    }

    @Test
    void quickSortHandlesDuplicates() {
        Integer[] values = {3, 5, 3, 1, 5};

        QuickSort.sort(values);

        assertArrayEquals(
                new Integer[]{1, 3, 3, 5, 5},
                values
        );
    }


    // =========================================================
    // EDGE CASES
    // =========================================================

    @Test
    void selectionSortHandlesEmptyArray() {
        Integer[] values = {};

        SelectionSort.sort(values);

        assertArrayEquals(
                new Integer[]{},
                values
        );
    }

    @Test
    void insertionSortHandlesSingleElement() {
        Integer[] values = {42};

        InsertionSort.sort(values);

        assertArrayEquals(
                new Integer[]{42},
                values
        );
    }

    @Test
    void mergeSortHandlesEmptyArray() {
        Integer[] values = {};

        MergeSort.sort(values);

        assertArrayEquals(
                new Integer[]{},
                values
        );
    }

    @Test
    void quickSortHandlesSingleElement() {
        Integer[] values = {99};

        QuickSort.sort(values);

        assertArrayEquals(
                new Integer[]{99},
                values
        );
    }
}