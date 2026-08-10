package algorithms.graph;

import algorithms.structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class DFS {

    public static List<Integer> traverse(
            Graph graph,
            int start) {

        if (graph == null) {
            throw new IllegalArgumentException(
                    "Graph cannot be null"
            );
        }

        if (start < 0 ||
                start >= graph.getVertexCount()) {

            throw new IllegalArgumentException(
                    "Invalid starting vertex"
            );
        }

        boolean[] visited =
                new boolean[graph.getVertexCount()];

        List<Integer> order =
                new ArrayList<>();

        dfsRecursive(
                graph,
                start,
                visited,
                order
        );

        return order;
    }

    private static void dfsRecursive(
            Graph graph,
            int current,
            boolean[] visited,
            List<Integer> order) {

        visited[current] = true;

        order.add(current);

        for (Graph.Edge edge :
                graph.getNeighbors(current)) {

            int next =
                    edge.getTo();

            if (!visited[next]) {

                dfsRecursive(
                        graph,
                        next,
                        visited,
                        order
                );
            }
        }
    }
}