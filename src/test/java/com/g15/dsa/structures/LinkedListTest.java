package com.g15.dsa.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;

class LinkedListTest {

    private LinkedList<Integer> list;

    @BeforeEach
    void setUp() { list = new LinkedList<>(); }

    // === NORMAL CASES ===
    @Test void addFirst() { list.addFirst(1); list.addFirst(2); assertEquals(2, list.getFirst()); }
    @Test void addLast() { list.addLast(1); list.addLast(2); assertEquals(2, list.getLast()); }
    @Test void addAndGet() { list.add(5); list.add(10); assertEquals(5, list.get(0)); assertEquals(10, list.get(1)); }
    @Test void removeFirst() { list.add(1); list.add(2); list.removeFirst(); assertEquals(2, list.getFirst()); }
    @Test void removeLast() { list.add(1); list.add(2); list.removeLast(); assertEquals(1, list.getLast()); }
    @Test void removeByValue() { list.add(5); list.add(10); assertTrue(list.remove(Integer.valueOf(5))); assertEquals(10, list.getFirst()); }
    @Test void contains() { list.add(7); assertTrue(list.contains(7)); assertFalse(list.contains(99)); }
    @Test void indexOf() { list.add(1); list.add(2); list.add(3); assertEquals(2, list.indexOf(3)); }
    @Test void size() { list.add(1); list.add(2); assertEquals(2, list.size()); }
    @Test void addAtIndex() { list.add(1); list.add(3); list.add(1,2); assertEquals(2, list.get(1)); }

    // === BOUNDARY CASES ===
    @Test void emptyIsEmpty() { assertTrue(list.isEmpty()); assertEquals(0, list.size()); }
    @Test void singleElement() { list.add(42); assertEquals(42, list.getFirst()); assertEquals(42, list.getLast()); }
    @Test void clear() { list.add(1); list.add(2); list.clear(); assertTrue(list.isEmpty()); }
    @Test void containsNullNotInList() { assertFalse(list.contains(null)); }

    // === INVALID INPUT CASES ===
    @Test void removeFirstEmpty() { assertThrows(NoSuchElementException.class, () -> list.removeFirst()); }
    @Test void removeLastEmpty() { assertThrows(NoSuchElementException.class, () -> list.removeLast()); }
    @Test void getFirstEmpty() { assertThrows(NoSuchElementException.class, () -> list.getFirst()); }
    @Test void getOutOfBounds() { list.add(1); assertThrows(IndexOutOfBoundsException.class, () -> list.get(5)); }
}
