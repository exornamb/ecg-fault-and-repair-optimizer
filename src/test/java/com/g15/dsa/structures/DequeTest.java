package com.g15.dsa.structures;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DequeTest {

    @Test
    void addLastRemoveFirstActsLikeFifoQueue() {
        Deque<Integer> d = new Deque<>();
        d.addLast(1);
        d.addLast(2);
        d.addLast(3);

        assertEquals(1, d.removeFirst());
        assertEquals(2, d.removeFirst());
        assertEquals(3, d.removeFirst());
    }

    @Test
    void addFirstRemoveLastActsLikeReversedFifo() {
        Deque<Integer> d = new Deque<>();
        d.addFirst(1);
        d.addFirst(2);
        d.addFirst(3);
        // front is now [3, 2, 1]
        assertEquals(1, d.removeLast());
        assertEquals(2, d.removeLast());
        assertEquals(3, d.removeLast());
    }

    @Test
    void addFirstLetsUrgentRequestJumpTheLine() {
        Deque<String> d = new Deque<>();
        d.addLast("routine-fault-1");
        d.addLast("routine-fault-2");
        d.addFirst("URGENT-outage");

        assertEquals("URGENT-outage", d.removeFirst());
        assertEquals("routine-fault-1", d.removeFirst());
        assertEquals("routine-fault-2", d.removeFirst());
    }

    @Test
    void peekFirstAndPeekLastDoNotRemove() {
        Deque<Integer> d = new Deque<>();
        d.addLast(10);
        d.addLast(20);

        assertEquals(10, d.peekFirst());
        assertEquals(20, d.peekLast());
        assertEquals(2, d.size());
    }

    @Test
    void removeFirstOnEmptyDequeThrows() {
        Deque<Integer> d = new Deque<>();
        assertThrows(NoSuchElementException.class, d::removeFirst);
    }

    @Test
    void removeLastOnEmptyDequeThrows() {
        Deque<Integer> d = new Deque<>();
        assertThrows(NoSuchElementException.class, d::removeLast);
    }

    @Test
    void addFirstNullThrows() {
        Deque<String> d = new Deque<>();
        assertThrows(IllegalArgumentException.class, () -> d.addFirst(null));
    }

    @Test
    void addLastNullThrows() {
        Deque<String> d = new Deque<>();
        assertThrows(IllegalArgumentException.class, () -> d.addLast(null));
    }

    @Test
    void sizeTracksElementsAcrossBothEnds() {
        Deque<Integer> d = new Deque<>();
        assertEquals(0, d.size());
        d.addFirst(1);
        d.addLast(2);
        assertEquals(2, d.size());
        d.removeFirst();
        assertEquals(1, d.size());
    }

    @Test
    void mixedFrontAndBackOperationsStayConsistent() {
        Deque<Integer> d = new Deque<>();
        d.addLast(2);   // [2]
        d.addFirst(1);  // [1, 2]
        d.addLast(3);   // [1, 2, 3]
        d.addFirst(0);  // [0, 1, 2, 3]

        assertEquals(0, d.removeFirst()); // [1, 2, 3]
        assertEquals(3, d.removeLast());  // [1, 2]
        assertEquals(1, d.removeFirst()); // [2]
        assertEquals(2, d.removeLast());  // []
        assertTrue(d.isEmpty());
    }

    @Test
    void singleElementDequeBecomesEmptyAfterEitherRemoval() {
        Deque<Integer> d = new Deque<>();
        d.addFirst(99);
        assertEquals(99, d.removeLast());
        assertTrue(d.isEmpty());
    }
}
