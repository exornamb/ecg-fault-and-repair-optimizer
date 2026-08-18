package com.g15.dsa.algorithms.graph;

import com.g15.dsa.structures.Graph;
import com.g15.dsa.structures.PriorityQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Dijkstra's Single-Source Shortest Path Algorithm.
 * 
 * Computes the minimum travel distance/time from an emergency dispatch hub
 * to any destination location across the road network, reconstructing full paths.
 * 
 * Time Complexity: O((V + E) log V) with PriorityQueue / Heap
 * Space Complexity: O(V)
 */
public class Dijkstra {

    public static class NodeDistance implements Comparable<NodeDistance> {
        public final int vertex;
        public final double distance;

        public NodeDistance(int vertex, double distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    public static class Result {
        private final int source;
        private final double[] distances;
        private final int[] predecessors;

        public Result(int source, double[] distances, int[] predecessors) {
            this.source = source;
            this.distances = distances;
            this.predecessors = predecessors;
        }

        public int getSource() {
            return source;
        }

        public double[] getDistances() {
            return distances;
        }

        public int[] getPredecessors() {
            return predecessors;
        }

        public double getDistanceTo(int target) {
            return distances[target];
        }

        public boolean hasPathTo(int target) {
            return distances[target] < Double.POSITIVE_INFINITY;
        }

        public List<Integer> getPathTo(int target) {
            if (!hasPathTo(target)) return Collections.emptyList();
            List<Integer> path = new ArrayList<>();
            for (int at = target; at != -1; at = predecessors[at]) {
                path.add(at);
            }
            Collections.reverse(path);
            return path;
        }
    }

    public static Result shortestPath(Graph graph, int source) {
        return shortestPaths(graph, source);
    }

    public static Result shortestPaths(Graph graph, int source) {
        validateVertex(graph, source);

        int n = graph.getVertexCount();
        double[] distances = new double[n];
        int[] predecessors = new int[n];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        Arrays.fill(predecessors, -1);

        distances[source] = 0.0;

        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        pq.insert(new NodeDistance(source, 0.0));

        boolean[] visited = new boolean[n];

        while (!pq.isEmpty()) {
            NodeDistance current = pq.extractMin();
            int u = current.vertex;

            if (visited[u]) continue;
            visited[u] = true;

            for (Graph.Edge edge : graph.getNeighbors(u)) {
                int v = edge.getTo();
                double weight = edge.getWeight();

                if (weight < 0) {
                    throw new IllegalArgumentException("Dijkstra does not support negative edge weights (found " + weight + ")");
                }

                if (!visited[v] && distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    predecessors[v] = u;
                    pq.insert(new NodeDistance(v, distances[v]));
                }
            }
        }

        return new Result(source, distances, predecessors);
    }

    private static void validateVertex(Graph graph, int vertex) {
        if (vertex < 0 || vertex >= graph.getVertexCount()) {
            throw new IllegalArgumentException("Vertex " + vertex + " is out of graph bounds [0.." + (graph.getVertexCount() - 1) + "]");
        }
    }
}
