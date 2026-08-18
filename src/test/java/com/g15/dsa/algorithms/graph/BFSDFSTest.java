package com.g15.dsa.algorithms.graph;

import com.g15.dsa.structures.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BFSDFSTest {

    private Graph graph;

    @BeforeEach
    void setUp() {
        graph = new Graph(6);
        graph.addUndirectedEdge(0, 1, 1);
        graph.addUndirectedEdge(0, 2, 1);
        graph.addUndirectedEdge(1, 3, 1);
        graph.addUndirectedEdge(1, 4, 1);
        graph.addUndirectedEdge(2, 5, 1);
    }

    // === BFS TESTS ===
    @Test void bfsStartsAtSource() { List<Integer> order = BFS.traverse(graph, 0); assertEquals(0, order.get(0)); }
    @Test void bfsVisitsAllReachable() { List<Integer> order = BFS.traverse(graph, 0); assertEquals(6, order.size()); }
    @Test void bfsLevelOrder() { List<Integer> order = BFS.traverse(graph, 0); assertEquals(0, order.get(0)); assertTrue(order.indexOf(1) < order.indexOf(3)); }
    @Test void bfsReachable() { assertTrue(BFS.isReachable(graph, 0, 5)); }
    @Test void bfsNotReachable() {
        Graph disconnected = new Graph(4);
        disconnected.addUndirectedEdge(0, 1, 1);
        disconnected.addUndirectedEdge(2, 3, 1);
        assertFalse(BFS.isReachable(disconnected, 0, 3));
    }
    @Test void bfsSelfReachable() { assertTrue(BFS.isReachable(graph, 2, 2)); }

    // === DFS TESTS ===
    @Test void dfsStartsAtSource() { List<Integer> order = DFS.traverse(graph, 0); assertEquals(0, order.get(0)); }
    @Test void dfsVisitsAllReachable() { List<Integer> order = DFS.traverse(graph, 0); assertEquals(6, order.size()); }
    @Test void dfsIterativeMatchesRecursive() {
        List<Integer> recursive = DFS.traverse(graph, 0);
        List<Integer> iterative = DFS.traverseIterative(graph, 0);
        assertEquals(recursive.size(), iterative.size());
    }

    // === BOUNDARY ===
    @Test void singleNodeBFS() {
        Graph g = new Graph(1);
        List<Integer> order = BFS.traverse(g, 0);
        assertEquals(1, order.size());
    }
    @Test void singleNodeDFS() {
        Graph g = new Graph(1);
        List<Integer> order = DFS.traverse(g, 0);
        assertEquals(1, order.size());
    }

    // === INVALID ===
    @Test void bfsInvalidVertex() { assertThrows(IllegalArgumentException.class, () -> BFS.traverse(graph, -1)); }
    @Test void dfsInvalidVertex() { assertThrows(IllegalArgumentException.class, () -> DFS.traverse(graph, 10)); }
}
