package com.g15.dsa.algorithms.graph;

import com.g15.dsa.structures.DisjointSet;
import com.g15.dsa.structures.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Kruskal {

    public static Result minimumSpanningTree(
            Graph graph) {

        if (graph == null) {
            throw new IllegalArgumentException(
                    "Graph cannot be null"
            );
        }

        int vertices =
                graph.getVertexCount();

        List<Graph.Edge> edges =
                new ArrayList<>();

        /*
         * Graph stores directed edges.
         * For an undirected graph, each edge appears
         * in both directions, so keep only one copy.
         */
        for (int from = 0;
             from < vertices;
             from++) {

            for (Graph.Edge edge :
                    graph.getNeighbors(from)) {

                if (edge.getFrom()
                        < edge.getTo()) {

                    edges.add(edge);
                }
            }
        }

        edges.sort(
                Comparator.comparingDouble(
                        Graph.Edge::getWeight
                )
        );

        DisjointSet disjointSet =
                new DisjointSet(vertices);

        List<Graph.Edge> mstEdges =
                new ArrayList<>();

        double totalWeight = 0;

        for (Graph.Edge edge : edges) {

            int from =
                    edge.getFrom();

            int to =
                    edge.getTo();

            if (!disjointSet.connected(
                    from,
                    to
            )) {

                disjointSet.union(
                        from,
                        to
                );

                mstEdges.add(edge);

                totalWeight +=
                        edge.getWeight();

                if (mstEdges.size()
                        == vertices - 1) {

                    break;
                }
            }
        }

        return new Result(
                mstEdges,
                totalWeight
        );
    }

    // =========================================================
    // RESULT
    // =========================================================

    public static class Result {

        private final List<Graph.Edge> edges;
        private final double totalWeight;

        private Result(
                List<Graph.Edge> edges,
                double totalWeight) {

            this.edges =
                    new ArrayList<>(edges);

            this.totalWeight =
                    totalWeight;
        }

        public List<Graph.Edge> getEdges() {
            return new ArrayList<>(edges);
        }

        public double getTotalWeight() {
            return totalWeight;
        }

        public int getEdgeCount() {
            return edges.size();
        }
    }
}