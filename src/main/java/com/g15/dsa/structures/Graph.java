package com.g15.dsa.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graph {

    private final int vertices;

    // Adjacency list
    private final List<Edge>[] adjacencyList;

    // Adjacency matrix
    private final double[][] adjacencyMatrix;

    public Graph(int vertices) {

        if (vertices <= 0) {
            throw new IllegalArgumentException(
                    "Number of vertices must be greater than zero"
            );
        }

        this.vertices = vertices;

        adjacencyList = new ArrayList[vertices];

        for (int i = 0; i < vertices; i++) {
            adjacencyList[i] = new ArrayList<>();
        }

        adjacencyMatrix = new double[vertices][vertices];

        for (int i = 0; i < vertices; i++) {
            Arrays.fill(
                    adjacencyMatrix[i],
                    Double.POSITIVE_INFINITY
            );

            adjacencyMatrix[i][i] = 0;
        }
    }

    // =========================================================
    // EDGE
    // =========================================================

    public static class Edge {

        private final int from;
        private final int to;
        private final double weight;

        public Edge(
                int from,
                int to,
                double weight) {

            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public double getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return from +
                    " -> " +
                    to +
                    " (" +
                    weight +
                    ")";
        }
    }

    // =========================================================
    // ADD EDGE
    // =========================================================

    public void addEdge(
            int from,
            int to,
            double weight) {

        validateVertex(from);
        validateVertex(to);

        if (weight < 0) {
            throw new IllegalArgumentException(
                    "Graph weight cannot be negative"
            );
        }

        adjacencyList[from].add(
                new Edge(
                        from,
                        to,
                        weight
                )
        );

        adjacencyMatrix[from][to] = weight;
    }

    // =========================================================
    // ADD UNDIRECTED EDGE
    // =========================================================

    public void addUndirectedEdge(
            int first,
            int second,
            double weight) {

        addEdge(
                first,
                second,
                weight
        );

        addEdge(
                second,
                first,
                weight
        );
    }

    // =========================================================
    // ADJACENCY LIST
    // =========================================================

    public List<Edge> getNeighbors(int vertex) {

        validateVertex(vertex);

        return new ArrayList<>(
                adjacencyList[vertex]
        );
    }

    @SuppressWarnings("unchecked")
    public List<Edge>[] getAdjacencyList() {

        List<Edge>[] copy =
                new ArrayList[vertices];

        for (int i = 0; i < vertices; i++) {

            copy[i] =
                    new ArrayList<>(
                            adjacencyList[i]
                    );
        }

        return copy;
    }

    // =========================================================
    // ADJACENCY MATRIX
    // =========================================================

    public double[][] getAdjacencyMatrix() {

        double[][] copy =
                new double[vertices][vertices];

        for (int i = 0; i < vertices; i++) {

            System.arraycopy(
                    adjacencyMatrix[i],
                    0,
                    copy[i],
                    0,
                    vertices
            );
        }

        return copy;
    }

    // =========================================================
    // GRAPH INFORMATION
    // =========================================================

    public int getVertexCount() {
        return vertices;
    }

    public int getEdgeCount() {

        int count = 0;

        for (List<Edge> edges :
                adjacencyList) {

            count += edges.size();
        }

        return count;
    }

    public boolean hasEdge(
            int from,
            int to) {

        validateVertex(from);
        validateVertex(to);

        return adjacencyMatrix[from][to]
                != Double.POSITIVE_INFINITY;
    }

    public double getWeight(
            int from,
            int to) {

        validateVertex(from);
        validateVertex(to);

        return adjacencyMatrix[from][to];
    }

    // =========================================================
    // VERTEX VALIDATION
    // =========================================================

    private void validateVertex(int vertex) {

        if (
                vertex < 0
                        || vertex >= vertices
        ) {

            throw new IndexOutOfBoundsException(
                    "Vertex " +
                            vertex +
                            " is outside the valid range 0-" +
                            (vertices - 1)
            );
        }
    }
}