package com.g15.dsa.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class RedBlackTreeTest {

    private RedBlackTree<Integer> rbt;

    @BeforeEach
    void setUp() {
        rbt = new RedBlackTree<>();
    }

    // === NORMAL CASES ===
    @Test
    void insertAndSearch() {
        rbt.insert(10);
        rbt.insert(20);
        rbt.insert(30);
        assertTrue(rbt.search(10));
        assertTrue(rbt.search(20));
        assertTrue(rbt.search(30));
        assertFalse(rbt.search(99));
    }

    @Test
    void inOrderTraversalIsSorted() {
        int[] vals = {15, 8, 25, 4, 11, 20, 30};
        for (int v : vals) rbt.insert(v);

        List<Integer> sorted = rbt.inorder();
        assertEquals(7, sorted.size());
        assertEquals(4, sorted.get(0));
        assertEquals(8, sorted.get(1));
        assertEquals(30, sorted.get(6));
    }

    @Test
    void balancedHeightCheck() {
        // Inserting strictly sorted elements into an unbalanced BST creates height = 10,
        // but in RedBlackTree height should be <= 2 * log2(n+1)
        for (int i = 1; i <= 15; i++) {
            rbt.insert(i);
        }
        assertTrue(rbt.height() <= 6, "RBT height should remain logarithmic");
    }

    // === BOUNDARY CASES ===
    @Test
    void emptyTree() {
        assertTrue(rbt.isEmpty());
        assertEquals(0, rbt.height());
        assertFalse(rbt.search(5));
    }

    @Test
    void singleNode() {
        rbt.insert(42);
        assertFalse(rbt.isEmpty());
        assertEquals(1, rbt.height());
        assertTrue(rbt.search(42));
    }

    @Test
    void clearTree() {
        rbt.insert(1);
        rbt.insert(2);
        rbt.clear();
        assertTrue(rbt.isEmpty());
        assertEquals(0, rbt.height());
    }

    // === INVALID INPUTS ===
    @Test
    void insertNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> rbt.insert(null));
    }
}
