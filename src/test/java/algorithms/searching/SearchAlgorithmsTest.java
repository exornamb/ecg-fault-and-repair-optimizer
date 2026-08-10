package algorithms.searching;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchAlgorithmsTest {

    // =========================
    // LINEAR SEARCH
    // =========================

    @Test
    void linearSearchFindsExistingElement() {

        Integer[] values = {
                10, 20, 30, 40, 50
        };

        Assertions.assertEquals(
                2,
                LinearSearch.search(values, 30)
        );
    }

    @Test
    void linearSearchReturnsMinusOneWhenNotFound() {

        Integer[] values = {
                10, 20, 30, 40, 50
        };

        assertEquals(
                -1,
                LinearSearch.search(values, 99)
        );
    }

    @Test
    void linearSearchWorksWithFirstElement() {

        Integer[] values = {
                10, 20, 30
        };

        assertEquals(
                0,
                LinearSearch.search(values, 10)
        );
    }

    @Test
    void linearSearchWorksWithLastElement() {

        Integer[] values = {
                10, 20, 30
        };

        assertEquals(
                2,
                LinearSearch.search(values, 30)
        );
    }

    @Test
    void linearSearchWorksWithDuplicates() {

        Integer[] values = {
                10, 20, 20, 30
        };

        assertEquals(
                1,
                LinearSearch.search(values, 20)
        );
    }

    @Test
    void linearSearchWorksWithEmptyArray() {

        Integer[] values = {};

        assertEquals(
                -1,
                LinearSearch.search(values, 10)
        );
    }


    // =========================
    // BINARY SEARCH
    // =========================

    @Test
    void binarySearchFindsExistingElement() {

        Integer[] values = {
                10, 20, 30, 40, 50
        };

        Assertions.assertEquals(
                2,
                BinarySearch.search(values, 30)
        );
    }

    @Test
    void binarySearchReturnsMinusOneWhenNotFound() {

        Integer[] values = {
                10, 20, 30, 40, 50
        };

        assertEquals(
                -1,
                BinarySearch.search(values, 99)
        );
    }

    @Test
    void binarySearchFindsFirstElement() {

        Integer[] values = {
                10, 20, 30, 40, 50
        };

        assertEquals(
                0,
                BinarySearch.search(values, 10)
        );
    }

    @Test
    void binarySearchFindsLastElement() {

        Integer[] values = {
                10, 20, 30, 40, 50
        };

        assertEquals(
                4,
                BinarySearch.search(values, 50)
        );
    }

    @Test
    void binarySearchWorksWithSingleElement() {

        Integer[] values = {
                42
        };

        assertEquals(
                0,
                BinarySearch.search(values, 42)
        );
    }

    @Test
    void binarySearchWorksWithEmptyArray() {

        Integer[] values = {};

        assertEquals(
                -1,
                BinarySearch.search(values, 42)
        );
    }

    @Test
    void binarySearchHandlesMissingValueBetweenElements() {

        Integer[] values = {
                10, 20, 30, 40, 50
        };

        assertEquals(
                -1,
                BinarySearch.search(values, 35)
        );
    }
}