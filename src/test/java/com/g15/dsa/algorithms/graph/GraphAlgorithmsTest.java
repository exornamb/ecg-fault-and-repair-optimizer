package com.g15.dsa.algorithms.graph;

import com.g15.dsa.structures.Graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmsTest {

    private Graph createTestGraph() {

        Graph graph = new Graph(5);

        graph.addUndirectedEdge(0, 1, 4);
        graph.addUndirectedEdge(0, 2, 2);
        graph.addUndirectedEdge(1, 2, 1);
        graph.addUndirectedEdge(1, 3, 5);
        graph.addUndirectedEdge(2, 3, 1);
        graph.addUndirectedEdge(3, 4, 3);

        return graph;
    }


    // =========================================================
    // PRIM
    // =========================================================

    @Test
    void primCreatesMinimumSpanningTree() {

        Graph graph = createTestGraph();

        Prim.Result result =
                Prim.minimumSpanningTree(
                        graph,
                        0
                );

        assertEquals(
                4,
                Arrays.stream(
                                result.getParents()
                        )
                        .filter(parent -> parent != -1)
                        .count()
        );
    }

    @Test
    void primCalculatesCorrectTotalWeight() {

        Graph graph = createTestGraph();

        Prim.Result result =
                Prim.minimumSpanningTree(
                        graph,
                        0
                );

        assertEquals(
                7.0,
                result.getTotalWeight()
        );
    }


    // =========================================================
    // KRUSKAL
    // =========================================================

    @Test
    void kruskalCreatesMinimumSpanningTree() {

        Graph graph = createTestGraph();

        Kruskal.Result result =
                Kruskal.minimumSpanningTree(
                        graph
                );

        assertEquals(
                4,
                result.getEdgeCount()
        );
    }

    @Test
    void kruskalCalculatesCorrectTotalWeight() {

        Graph graph = createTestGraph();

        Kruskal.Result result =
                Kruskal.minimumSpanningTree(
                        graph
                );

        assertEquals(
                7.0,
                result.getTotalWeight()
        );
    }

}
