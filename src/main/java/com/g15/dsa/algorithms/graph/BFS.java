package com.g15.dsa.algorithms.graph;

import com.g15.dsa.structures.DynamicArray;
import com.g15.dsa.structures.Graph;
import com.g15.dsa.structures.Queue;

import java.util.ArrayList;
import java.util.List;

/**
 * Breadth-First Search (BFS) Traversal and Reachability.
 * 
 * Uses our custom FIFO Queue to traverse the graph level by level.
 * Useful in the ECG power network for identifying all substations
 * directly affected by an upstream outage.
 * 
 * Time Complexity: O(V + E)
 * Space Complexity: O(V)
 */
public class BFS {

    public static List<Integer> traverse(Graph graph, int startVertex) {
        validateVertex(graph, startVertex);

        List<Integer> visitedOrder = new ArrayList<>();
        boolean[] visited = new boolean[graph.getVertexCount()];
        Queue<Integer> queue = new Queue<>();

        visited[startVertex] = true;
        queue.enqueue(startVertex);

        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            visitedOrder.add(current);

            for (Graph.Edge edge : graph.getNeighbors(current)) {
                int neighbor = edge.getTo();
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.enqueue(neighbor);
                }
            }
        }

        return visitedOrder;
    }

    public static boolean isReachable(Graph graph, int source, int destination) {
        validateVertex(graph, source);
        validateVertex(graph, destination);

        if (source == destination) return true;

        boolean[] visited = new boolean[graph.getVertexCount()];
        Queue<Integer> queue = new Queue<>();

        visited[source] = true;
        queue.enqueue(source);

        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            if (current == destination) return true;

            for (Graph.Edge edge : graph.getNeighbors(current)) {
                int neighbor = edge.getTo();
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.enqueue(neighbor);
                }
            }
        }

        return false;
    }

    private static void validateVertex(Graph graph, int vertex) {
        if (vertex < 0 || vertex >= graph.getVertexCount()) {
            throw new IllegalArgumentException("Vertex " + vertex + " is out of graph bounds [0.." + (graph.getVertexCount() - 1) + "]");
        }
    }
}
