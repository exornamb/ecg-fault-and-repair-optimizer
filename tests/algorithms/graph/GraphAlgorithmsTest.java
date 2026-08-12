package algorithms.graph;

import algorithms.structures.Graph;
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
    // BFS
    // =========================================================

    @Test
    void bfsVisitsAllReachableVertices() {

        Graph graph = createTestGraph();

        List<Integer> result =
                BFS.traverse(graph, 0);

        assertEquals(
                Arrays.asList(0, 1, 2, 3, 4),
                result
        );
    }

    @Test
    void bfsStartingFromDifferentVertex() {

        Graph graph = createTestGraph();

        List<Integer> result =
                BFS.traverse(graph, 4);

        assertEquals(
                Arrays.asList(4, 3, 1, 2, 0),
                result
        );
    }


    // =========================================================
    // DFS
    // =========================================================

    @Test
    void dfsVisitsAllReachableVertices() {

        Graph graph = createTestGraph();

        List<Integer> result =
                DFS.traverse(graph, 0);

        assertEquals(
                Arrays.asList(0, 1, 2, 3, 4),
                result
        );
    }

    @Test
    void dfsVisitsEveryVertexExactlyOnce() {

        Graph graph = createTestGraph();

        List<Integer> result =
                DFS.traverse(graph, 0);

        assertEquals(5, result.size());

        assertEquals(
                5,
                result.stream()
                        .distinct()
                        .count()
        );
    }


    // =========================================================
    // DIJKSTRA
    // =========================================================

    @Test
    void dijkstraFindsShortestDistance() {

        Graph graph = createTestGraph();

        Dijkstra.Result result =
                Dijkstra.shortestPaths(
                        graph,
                        0
                );

        assertEquals(
                0,
                result.getDistance(0)
        );

        assertEquals(
                3,
                result.getDistance(1)
        );

        assertEquals(
                2,
                result.getDistance(2)
        );

        assertEquals(
                3,
                result.getDistance(3)
        );

        assertEquals(
                6,
                result.getDistance(4)
        );
    }

    @Test
    void dijkstraReconstructsShortestPath() {

        Graph graph = createTestGraph();

        Dijkstra.Result result =
                Dijkstra.shortestPaths(
                        graph,
                        0
                );

        assertArrayEquals(
                new int[]{0, 2, 3, 4},
                result.getPath(4)
        );
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


    // =========================================================
    // GRAPH STRUCTURE
    // =========================================================

    @Test
    void graphContainsExpectedEdges() {

        Graph graph = createTestGraph();

        assertTrue(
                graph.hasEdge(0, 1)
        );

        assertTrue(
                graph.hasEdge(1, 0)
        );

        assertTrue(
                graph.hasEdge(2, 3)
        );
    }

    @Test
    void graphReturnsCorrectEdgeWeights() {

        Graph graph = createTestGraph();

        assertEquals(
                4.0,
                graph.getWeight(0, 1)
        );

        assertEquals(
                2.0,
                graph.getWeight(0, 2)
        );

        assertEquals(
                3.0,
                graph.getWeight(3, 4)
        );
    }


    // =========================================================
    // DISCONNECTED GRAPH
    // =========================================================

    @Test
    void bfsDoesNotVisitDisconnectedVertices() {

        Graph graph = new Graph(4);

        graph.addUndirectedEdge(
                0,
                1,
                1
        );

        graph.addUndirectedEdge(
                2,
                3,
                1
        );

        List<Integer> result =
                BFS.traverse(graph, 0);

        assertEquals(
                Arrays.asList(0, 1),
                result
        );
    }
}