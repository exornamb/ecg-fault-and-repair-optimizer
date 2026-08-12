package tests.structures;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    // ---------- NORMAL CASES ----------

    @Test
    void testAddEdge_createsDirectedConnection() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 10);

        assertTrue(graph.hasEdge(0, 1));
        assertEquals(10, graph.getWeight(0, 1));
    }

    @Test
    void testAddEdge_isOneDirectionOnly() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 10);

        // directed: 0 -> 1 exists, but 1 -> 0 should NOT exist
        assertTrue(graph.hasEdge(0, 1));
        assertFalse(graph.hasEdge(1, 0));
    }

    @Test
    void testAddUndirectedEdge_createsConnectionBothWays() {
        Graph graph = new Graph(5);
        graph.addUndirectedEdge(0, 1, 15);

        assertTrue(graph.hasEdge(0, 1));
        assertTrue(graph.hasEdge(1, 0));
        assertEquals(15, graph.getWeight(0, 1));
        assertEquals(15, graph.getWeight(1, 0));
    }

    @Test
    void testGetNeighbors_returnsCorrectEdges() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 8);

        List<Graph.Edge> neighbors = graph.getNeighbors(0);

        assertEquals(2, neighbors.size());
    }

    @Test
    void testGetEdgeCount_countsAllDirectedEdges() {
        Graph graph = new Graph(5);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 5);
        graph.addUndirectedEdge(2, 3, 5); // adds 2 directed edges

        assertEquals(4, graph.getEdgeCount());
    }

    @Test
    void testGetVertexCount_returnsCorrectSize() {
        Graph graph = new Graph(7);
        assertEquals(7, graph.getVertexCount());
    }

    @Test
    void testGetAdjacencyMatrix_returnsIndependentCopy() {
        // Editing the returned matrix should NOT affect the graph's real data.
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 5);

        double[][] matrix = graph.getAdjacencyMatrix();
        matrix[0][1] = 999;

        assertEquals(5, graph.getWeight(0, 1));
    }

    // ---------- BOUNDARY CASES ----------

    @Test
    void testNewGraph_hasNoEdges() {
        Graph graph = new Graph(4);
        assertEquals(0, graph.getEdgeCount());
        assertFalse(graph.hasEdge(0, 1));
    }

    @Test
    void testSingleVertexGraph_hasSelfDistanceZero() {
        Graph graph = new Graph(1);
        assertEquals(0, graph.getWeight(0, 0));
    }

    @Test
    void testAddEdge_zeroWeight_isAllowed() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 0);

        assertTrue(graph.hasEdge(0, 1));
        assertEquals(0, graph.getWeight(0, 1));
    }

    @Test
    void testGetNeighbors_vertexWithNoEdges_returnsEmptyList() {
        Graph graph = new Graph(5);
        List<Graph.Edge> neighbors = graph.getNeighbors(3);

        assertTrue(neighbors.isEmpty());
    }

    @Test
    void testAddEdge_sameEdgeTwice_lastWeightWinsInMatrix() {
        Graph graph = new Graph(3);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 1, 9); // overwrite

        assertEquals(9, graph.getWeight(0, 1));
        // but the adjacency list keeps both entries (2 separate Edge objects)
        assertEquals(2, graph.getNeighbors(0).size());
    }

    // ---------- INVALID / EDGE CASES ----------

    @Test
    void testConstructor_zeroVertices_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Graph(0)
        );
    }

    @Test
    void testConstructor_negativeVertices_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Graph(-2)
        );
    }

    @Test
    void testAddEdge_negativeWeight_throwsException() {
        Graph graph = new Graph(3);

        assertThrows(
                IllegalArgumentException.class,
                () -> graph.addEdge(0, 1, -5)
        );
    }

    @Test
    void testAddEdge_vertexOutOfRange_throwsException() {
        Graph graph = new Graph(3);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> graph.addEdge(0, 10, 5)
        );
    }

    @Test
    void testHasEdge_vertexOutOfRange_throwsException() {
        Graph graph = new Graph(3);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> graph.hasEdge(0, 99)
        );
    }

    @Test
    void testGetNeighbors_vertexOutOfRange_throwsException() {
        Graph graph = new Graph(3);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> graph.getNeighbors(-1)
        );
    }
}