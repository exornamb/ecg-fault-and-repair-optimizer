package algorithms.graph;

import algorithms.structures.Graph;

import java.util.Arrays;

public class Prim {

    public static Result minimumSpanningTree(
            Graph graph,
            int start) {

        if (graph == null) {
            throw new IllegalArgumentException(
                    "Graph cannot be null"
            );
        }

        int n = graph.getVertexCount();

        if (start < 0 || start >= n) {
            throw new IllegalArgumentException(
                    "Invalid starting vertex"
            );
        }

        double[] key =
                new double[n];

        int[] parent =
                new int[n];

        boolean[] inTree =
                new boolean[n];

        Arrays.fill(
                key,
                Double.POSITIVE_INFINITY
        );

        Arrays.fill(
                parent,
                -1
        );

        key[start] = 0;

        for (int count = 0;
             count < n;
             count++) {

            int current =
                    getMinimumKeyVertex(
                            key,
                            inTree
                    );

            if (current == -1) {
                break;
            }

            inTree[current] = true;

            for (Graph.Edge edge :
                    graph.getNeighbors(current)) {

                int next =
                        edge.getTo();

                double weight =
                        edge.getWeight();

                if (!inTree[next]
                        && weight < key[next]) {

                    key[next] = weight;

                    parent[next] =
                            current;
                }
            }
        }

        return new Result(
                parent,
                key,
                inTree
        );
    }

    private static int getMinimumKeyVertex(
            double[] key,
            boolean[] inTree) {

        int minimumVertex = -1;

        double minimum =
                Double.POSITIVE_INFINITY;

        for (int i = 0;
             i < key.length;
             i++) {

            if (!inTree[i]
                    && key[i] < minimum) {

                minimum = key[i];

                minimumVertex = i;
            }
        }

        return minimumVertex;
    }

    // =========================================================
    // RESULT
    // =========================================================

    public static class Result {

        private final int[] parent;
        private final double[] key;
        private final boolean[] inTree;

        private Result(
                int[] parent,
                double[] key,
                boolean[] inTree) {

            this.parent =
                    parent.clone();

            this.key =
                    key.clone();

            this.inTree =
                    inTree.clone();
        }

        public int[] getParents() {
            return parent.clone();
        }

        public double[] getKeys() {
            return key.clone();
        }

        public boolean[] getIncludedVertices() {
            return inTree.clone();
        }

        public double getTotalWeight() {

            double total = 0;

            for (int i = 0;
                 i < key.length;
                 i++) {

                if (parent[i] != -1) {
                    total += key[i];
                }
            }

            return total;
        }
    }
}