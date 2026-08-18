package com.g15.dsa.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BSTTest {

    private BST<Integer> tree;

    @BeforeEach
    void setUp() {
        tree = new BST<>();
    }

    // === NORMAL CASES ===
    @Test
    void insertAndSearch() {
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        assertTrue(tree.search(50));
        assertTrue(tree.search(30));
        assertTrue(tree.search(70));
        assertFalse(tree.search(100));
    }

    @Test
    void inOrderTraversalIsSorted() {
        tree.insert(50);
        tree.insert(20);
        tree.insert(80);
        tree.insert(10);
        tree.insert(30);
        List<Integer> list = tree.inorder();
        assertEquals(5, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
        assertEquals(50, list.get(3));
        assertEquals(80, list.get(4));
    }

    @Test
    void heightCalculation() {
        tree.insert(50);
        tree.insert(25);
        tree.insert(75);
        tree.insert(10);
        assertEquals(3, tree.height());
    }

    // === BOUNDARY CASES ===
    @Test
    void emptyTree() {
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.height());
        assertFalse(tree.search(42));
    }

    @Test
    void singleElement() {
        tree.insert(100);
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.height());
        assertTrue(tree.search(100));
    }

    @Test
    void clearTree() {
        tree.insert(1);
        tree.insert(2);
        tree.clear();
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.height());
    }

    // === INVALID INPUTS ===
    @Test
    void insertNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> tree.insert(null));
    }
}
