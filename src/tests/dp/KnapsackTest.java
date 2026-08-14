package algorithms.dp;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnapsackTest {

    @Test
    void knapsackFindsOptimalValue() {

        List<Knapsack.Item> items =
                Arrays.asList(
                        new Knapsack.Item("A", 2, 12),
                        new Knapsack.Item("B", 1, 10),
                        new Knapsack.Item("C", 3, 20),
                        new Knapsack.Item("D", 2, 15)
                );

        Knapsack.Result result =
                Knapsack.solve(items, 5);

        assertEquals(
                37,
                result.getMaximumValue()
        );
    }

    @Test
    void knapsackReconstructsSelectedItems() {

        List<Knapsack.Item> items =
                Arrays.asList(
                        new Knapsack.Item("A", 2, 12),
                        new Knapsack.Item("B", 1, 10),
                        new Knapsack.Item("C", 3, 20),
                        new Knapsack.Item("D", 2, 15)
                );

        Knapsack.Result result =
                Knapsack.solve(items, 5);

        List<Knapsack.Item> selected =
                result.getSelectedItems();

        assertEquals(3, selected.size());

        assertEquals(
                Arrays.asList("A", "B", "D"),
                selected.stream()
                        .map(Knapsack.Item::getName)
                        .toList()
        );
    }

    @Test
    void knapsackCalculatesCorrectTotalWeight() {

        List<Knapsack.Item> items =
                Arrays.asList(
                        new Knapsack.Item("A", 2, 12),
                        new Knapsack.Item("B", 1, 10),
                        new Knapsack.Item("C", 3, 20),
                        new Knapsack.Item("D", 2, 15)
                );

        Knapsack.Result result =
                Knapsack.solve(items, 5);

        assertEquals(
                5,
                result.getTotalWeight()
        );
    }

    @Test
    void knapsackHandlesEmptyItems() {

        Knapsack.Result result =
                Knapsack.solve(
                        Collections.emptyList(),
                        10
                );

        assertEquals(
                0,
                result.getMaximumValue()
        );

        assertTrue(
                result.getSelectedItems().isEmpty()
        );
    }

    @Test
    void knapsackHandlesZeroCapacity() {

        List<Knapsack.Item> items =
                Arrays.asList(
                        new Knapsack.Item("A", 2, 12),
                        new Knapsack.Item("B", 1, 10)
                );

        Knapsack.Result result =
                Knapsack.solve(items, 0);

        assertEquals(
                0,
                result.getMaximumValue()
        );

        assertTrue(
                result.getSelectedItems().isEmpty()
        );
    }

    @Test
    void knapsackRejectsNegativeCapacity() {

        List<Knapsack.Item> items =
                Collections.singletonList(
                        new Knapsack.Item("A", 1, 10)
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> Knapsack.solve(items, -1)
        );
    }

    @Test
    void knapsackRejectsNegativeWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Knapsack.Item(
                        "Invalid",
                        -1,
                        10
                )
        );
    }

    @Test
    void knapsackRejectsNegativeValue() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Knapsack.Item(
                        "Invalid",
                        1,
                        -10
                )
        );
    }
}
