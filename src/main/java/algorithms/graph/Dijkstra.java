package algorithms.graph;

import algorithms.structures.Graph;

import java.util.Arrays;

public class Dijkstra {

    public static Result shortestPaths(
            Graph graph,
            int source) {

        if (graph == null) {
            throw new IllegalArgumentException(
                    "Graph cannot be null"
            );
        }

        if (source < 0 ||
                source >= graph.getVertexCount()) {

            throw new IllegalArgumentException(
                    "Invalid source vertex"
            );
        }

        int n =
                graph.getVertexCount();

        double[] distance =
                new double[n];

        int[] predecessor =
                new int[n];

        boolean[] visited =
                new boolean[n];

        Arrays.fill(
                distance,
                Double.POSITIVE_INFINITY
        );

        Arrays.fill(
                predecessor,
                -1
        );

        distance[source] = 0;

        for (int count = 0;
             count < n;
             count++) {

            int current =
                    getClosestUnvisited(
                            distance,
                            visited
                    );

            if (current == -1) {
                break;
            }

            visited[current] = true;

            for (Graph.Edge edge :
                    graph.getNeighbors(current)) {

                int next =
                        edge.getTo();

                if (visited[next]) {
                    continue;
                }

                double newDistance =
                        distance[current]
                                + edge.getWeight();

                if (newDistance
                        < distance[next]) {

                    distance[next] =
                            newDistance;

                    predecessor[next] =
                            current;
                }
            }
        }

        return new Result(
                distance,
                predecessor
        );
    }

    private static int getClosestUnvisited(
            double[] distance,
            boolean[] visited) {

        int closest = -1;

        double smallest =
                Double.POSITIVE_INFINITY;

        for (int i = 0;
             i < distance.length;
             i++) {

            if (!visited[i]
                    && distance[i] < smallest) {

                smallest =
                        distance[i];

                closest = i;
            }
        }

        return closest;
    }

    // =========================================================
    // RESULT
    // =========================================================

    public static class Result {

        private final double[] distances;
        private final int[] predecessors;

        private Result(
                double[] distances,
                int[] predecessors) {

            this.distances =
                    distances.clone();

            this.predecessors =
                    predecessors.clone();
        }

        public double getDistance(int vertex) {
            return distances[vertex];
        }

        public int getPredecessor(int vertex) {
            return predecessors[vertex];
        }

        public double[] getDistances() {
            return distances.clone();
        }

        public int[] getPredecessors() {
            return predecessors.clone();
        }

        // =====================================================
        // SHORTEST PATH RECONSTRUCTION
        // =====================================================

        public int[] getPath(int target) {

            if (target < 0 ||
                    target >= distances.length) {

                throw new IllegalArgumentException(
                        "Invalid target vertex"
                );
            }

            if (Double.isInfinite(
                    distances[target])) {

                return new int[0];
            }

            int count = 0;
            int current = target;

            while (current != -1) {

                count++;

                current =
                        predecessors[current];
            }

            int[] path =
                    new int[count];

            current = target;

            for (int i = count - 1;
                 i >= 0;
                 i--) {

                path[i] = current;

                current =
                        predecessors[current];
            }

            return path;
        }
    }
}