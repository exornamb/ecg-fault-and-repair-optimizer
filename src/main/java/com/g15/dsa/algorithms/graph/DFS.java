package com.g15.dsa.algorithms.graph;

import com.g15.dsa.structures.Graph;
import com.g15.dsa.structures.Stack;

import java.util.ArrayList;
import java.util.List;

/**
 * Depth-First Search (DFS) Traversal and Connectivity Analysis.
 * 
 * Explores deep feeder branches before backtracking. Useful for detecting
 * loops, cycles, and isolated sub-grids in the ECG distribution system.
 * 
 * Time Complexity: O(V + E)
 * Space Complexity: O(V)
 */
public class DFS {

    public static List<Integer> traverse(Graph graph, int startVertex) {
        validateVertex(graph, startVertex);

        List<Integer> visitedOrder = new ArrayList<>();
        boolean[] visited = new boolean[graph.getVertexCount()];
        dfsRecursive(graph, startVertex, visited, visitedOrder);
        return visitedOrder;
    }

    private static void dfsRecursive(Graph graph, int current, boolean[] visited, List<Integer> order) {
        visited[current] = true;
        order.add(current);

        for (Graph.Edge edge : graph.getNeighbors(current)) {
            int neighbor = edge.getTo();
            if (!visited[neighbor]) {
                dfsRecursive(graph, neighbor, visited, order);
            }
        }
    }

    public static List<Integer> traverseIterative(Graph graph, int startVertex) {
        validateVertex(graph, startVertex);

        List<Integer> visitedOrder = new ArrayList<>();
        boolean[] visited = new boolean[graph.getVertexCount()];
        Stack<Integer> stack = new Stack<>();

        stack.push(startVertex);

        while (!stack.isEmpty()) {
            int current = stack.pop();

            if (!visited[current]) {
                visited[current] = true;
                visitedOrder.add(current);

                for (Graph.Edge edge : graph.getNeighbors(current)) {
                    int neighbor = edge.getTo();
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                    }
                }
            }
        }

        return visitedOrder;
    }

    private static void validateVertex(Graph graph, int vertex) {
        if (vertex < 0 || vertex >= graph.getVertexCount()) {
            throw new IllegalArgumentException("Vertex " + vertex + " is out of graph bounds [0.." + (graph.getVertexCount() - 1) + "]");
        }
    }
}
