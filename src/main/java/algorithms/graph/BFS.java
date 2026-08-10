package algorithms.graph;

import algorithms.structures.Graph;

import java.util.ArrayList;
import java.util.List;

public class BFS {

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

        int[] queue =
                new int[graph.getVertexCount()];

        int front = 0;
        int rear = 0;

        queue[rear++] = start;
        visited[start] = true;

        while (front < rear) {

            int current =
                    queue[front++];

            order.add(current);

            for (Graph.Edge edge :
                    graph.getNeighbors(current)) {

                int next =
                        edge.getTo();

                if (!visited[next]) {

                    visited[next] = true;

                    queue[rear++] = next;
                }
            }
        }

        return order;
    }
}