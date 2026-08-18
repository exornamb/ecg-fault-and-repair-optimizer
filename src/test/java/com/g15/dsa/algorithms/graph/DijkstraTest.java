package com.g15.dsa.algorithms.graph;

import com.g15.dsa.structures.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class DijkstraTest {

    private Graph graph;

    @BeforeEach
    void setUp() {
        graph = new Graph(5);
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 3);
    }

    // === NORMAL ===
    @Test void correctDistanceToSelf() {
        Dijkstra.Result r = Dijkstra.shortestPaths(graph, 0);
        assertEquals(0.0, r.getDistanceTo(0), 0.001);
    }
    @Test void correctShortestPath() {
        Dijkstra.Result r = Dijkstra.shortestPaths(graph, 0);
        assertEquals(6.0, r.getDistanceTo(4), 0.001);
    }
    @Test void pathReconstructionCorrect() {
        Dijkstra.Result r = Dijkstra.shortestPaths(graph, 0);
        List<Integer> path = r.getPathTo(4);
        assertEquals(0, path.get(0));
        assertEquals(4, path.get(path.size() - 1));
    }
    @Test void predecessorNotNull() {
        Dijkstra.Result r = Dijkstra.shortestPaths(graph, 0);
        assertNotNull(r.getPredecessors());
    }
    @Test void hasPath() {
        Dijkstra.Result r = Dijkstra.shortestPaths(graph, 0);
        assertTrue(r.hasPathTo(4));
    }

    // === BOUNDARY ===
    @Test void singleVertex() {
        Graph g = new Graph(1);
        Dijkstra.Result r = Dijkstra.shortestPaths(g, 0);
        assertEquals(0.0, r.getDistanceTo(0), 0.001);
    }
    @Test void disconnectedVertex() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 5);
        Dijkstra.Result r = Dijkstra.shortestPaths(g, 0);
        assertFalse(r.hasPathTo(2));
    }
    @Test void emptyPathToDisconnected() {
        Graph g = new Graph(3);
        g.addEdge(0, 1, 5);
        Dijkstra.Result r = Dijkstra.shortestPaths(g, 0);
        assertTrue(r.getPathTo(2).isEmpty());
    }

    // === INVALID ===
    @Test void negativeWeightThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            Graph g = new Graph(3);
            g.addEdge(0, 1, -1);
            Dijkstra.shortestPaths(g, 0);
        });
    }
    @Test void invalidSourceThrows() {
        assertThrows(IllegalArgumentException.class, () -> Dijkstra.shortestPaths(graph, -1));
    }
}
