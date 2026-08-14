package algorithms.dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Knapsack {

    public static class Item {

        private final String name;
        private final int weight;
        private final int value;

        public Item(
                String name,
                int weight,
                int value) {

            if (weight < 0) {
                throw new IllegalArgumentException(
                        "Weight cannot be negative"
                );
            }

            if (value < 0) {
                throw new IllegalArgumentException(
                        "Value cannot be negative"
                );
            }

            this.name = name;
            this.weight = weight;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getWeight() {
            return weight;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name +
                    " (weight=" +
                    weight +
                    ", value=" +
                    value +
                    ")";
        }
    }

    public static class Result {

        private final int maximumValue;
        private final int totalWeight;
        private final List<Item> selectedItems;
        private final int[][] table;

        private Result(
                int maximumValue,
                int totalWeight,
                List<Item> selectedItems,
                int[][] table) {

            this.maximumValue = maximumValue;
            this.totalWeight = totalWeight;

            this.selectedItems =
                    new ArrayList<>(selectedItems);

            this.table = table;
        }

        public int getMaximumValue() {
            return maximumValue;
        }

        public int getTotalWeight() {
            return totalWeight;
        }

        public List<Item> getSelectedItems() {
            return new ArrayList<>(
                    selectedItems
            );
        }

        public int[][] getTable() {

            int[][] copy =
                    new int[table.length][];

            for (int i = 0;
                 i < table.length;
                 i++) {

                copy[i] =
                        table[i].clone();
            }

            return copy;
        }
    }

    public static Result solve(
            List<Item> items,
            int capacity) {

        if (items == null) {
            throw new IllegalArgumentException(
                    "Items cannot be null"
            );
        }

        if (capacity < 0) {
            throw new IllegalArgumentException(
                    "Capacity cannot be negative"
            );
        }

        int n = items.size();

        int[][] dp =
                new int[n + 1][capacity + 1];

        // =====================================================
        // BUILD DP TABLE
        // =====================================================

        for (int i = 1; i <= n; i++) {

            Item item =
                    items.get(i - 1);

            for (int w = 0;
                 w <= capacity;
                 w++) {

                // Do not take item
                dp[i][w] =
                        dp[i - 1][w];

                // Take item if it fits
                if (item.getWeight() <= w) {

                    int include =
                            item.getValue()
                                    +
                                    dp[
                                            i - 1
                                            ][
                                            w -
                                                    item.getWeight()
                                            ];

                    dp[i][w] =
                            Math.max(
                                    dp[i][w],
                                    include
                            );
                }
            }
        }

        // =====================================================
        // RECONSTRUCT SELECTED ITEMS
        // =====================================================

        List<Item> selected =
                new ArrayList<>();

        int remainingCapacity =
                capacity;

        for (int i = n;
             i >= 1;
             i--) {

            if (
                    dp[i][remainingCapacity]
                            !=
                            dp[i - 1][remainingCapacity]
            ) {

                Item item =
                        items.get(i - 1);

                selected.add(item);

                remainingCapacity -=
                        item.getWeight();
            }
        }

        Collections.reverse(selected);

        int totalWeight = 0;

        for (Item item : selected) {

            totalWeight +=
                    item.getWeight();
        }

        return new Result(
                dp[n][capacity],
                totalWeight,
                selected,
                dp
        );
    }
}
