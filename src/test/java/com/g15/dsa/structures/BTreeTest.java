package com.g15.dsa.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BTreeTest {

    private BTree<Integer> bTree;

    @BeforeEach
    void setUp() {
        bTree = new BTree<>(3); // t = 3, max keys per node = 5
    }

    // === NORMAL CASES ===
    @Test
    void insertAndContains() {
        bTree.insert(10);
        bTree.insert(20);
        bTree.insert(5);
        bTree.insert(6);
        bTree.insert(12);
        bTree.insert(30);

        assertTrue(bTree.contains(10));
        assertTrue(bTree.contains(20));
        assertTrue(bTree.contains(5));
        assertTrue(bTree.contains(30));
        assertFalse(bTree.contains(999));
    }

    @Test
    void inOrderTraversalIsSorted() {
        int[] vals = {50, 10, 80, 20, 70, 30, 90, 40, 60};
        for (int v : vals) bTree.insert(v);

        List<Integer> inorder = bTree.inorder();
        assertEquals(9, inorder.size());
        assertEquals(10, inorder.get(0));
        assertEquals(90, inorder.get(inorder.size() - 1));
        for (int i = 0; i < inorder.size() - 1; i++) {
            assertTrue(inorder.get(i) < inorder.get(i + 1));
        }
    }

    @Test
    void sizeTracking() {
        assertEquals(0, bTree.size());
        bTree.insert(1);
        bTree.insert(2);
        bTree.insert(3);
        assertEquals(3, bTree.size());
    }

    // === BOUNDARY CASES ===
    @Test
    void emptyTree() {
        assertTrue(bTree.isEmpty());
        assertEquals(0, bTree.size());
        assertEquals(0, bTree.height());
        assertFalse(bTree.contains(10));
    }

    @Test
    void singleElement() {
        bTree.insert(42);
        assertFalse(bTree.isEmpty());
        assertEquals(1, bTree.size());
        assertTrue(bTree.contains(42));
    }

    @Test
    void clearTree() {
        bTree.insert(1);
        bTree.insert(2);
        bTree.clear();
        assertTrue(bTree.isEmpty());
        assertEquals(0, bTree.size());
    }

    // === INVALID INPUTS ===
    @Test
    void insertNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> bTree.insert(null));
    }

    @Test
    void invalidDegreeThrows() {
        assertThrows(IllegalArgumentException.class, () -> new BTree<>(1));
    }
}
