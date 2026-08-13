package com.g15.dsa.queue;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PriorityQueueTest {

    /** Small comparable wrapper standing in for a dispatch request, scoped to this test file. */
    private static class Request implements Comparable<Request> {
        final String name;
        final int urgency; // lower number = more urgent

        Request(String name, int urgency) {
            this.name = name;
            this.urgency = urgency;
        }

        @Override
        public int compareTo(Request other) {
            return Integer.compare(this.urgency, other.urgency);
        }

        @Override
        public String toString() {
            return name + "(u=" + urgency + ")";
        }
    }

    @Test
    void extractMinReturnsSmallestFirst() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.insert(5);
        pq.insert(1);
        pq.insert(3);

        assertEquals(1, pq.extractMin());
        assertEquals(3, pq.extractMin());
        assertEquals(5, pq.extractMin());
    }

    @Test
    void insertOutOfOrderStillDrainsInAscendingOrder() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        int[] values = {50, 10, 40, 20, 30, 5, 45};
        for (int v : values) {
            pq.insert(v);
        }

        int previous = Integer.MIN_VALUE;
        while (!pq.isEmpty()) {
            int current = pq.extractMin();
            assertTrue(current >= previous, "heap must yield non-decreasing order");
            previous = current;
        }
    }

    @Test
    void urgencyOrderingDispatchesMostUrgentRequestFirst() {
        PriorityQueue<Request> pq = new PriorityQueue<>();
        pq.insert(new Request("routine-fault", 5));
        pq.insert(new Request("critical-outage", 1));
        pq.insert(new Request("moderate-fault", 3));

        assertEquals("critical-outage", pq.extractMin().name);
        assertEquals("moderate-fault", pq.extractMin().name);
        assertEquals("routine-fault", pq.extractMin().name);
    }

    @Test
    void peekReturnsHighestPriorityWithoutRemoving() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.insert(7);
        pq.insert(2);

        assertEquals(2, pq.peek());
        assertEquals(2, pq.size());
        assertEquals(2, pq.peek());
    }

    @Test
    void extractMinOnEmptyQueueThrows() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        assertThrows(NoSuchElementException.class, pq::extractMin);
    }

    @Test
    void peekOnEmptyQueueThrows() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        assertThrows(NoSuchElementException.class, pq::peek);
    }

    @Test
    void insertNullThrows() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        assertThrows(IllegalArgumentException.class, () -> pq.insert(null));
    }

    @Test
    void constructorRejectsNonPositiveInitialCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new PriorityQueue<Integer>(0));
        assertThrows(IllegalArgumentException.class, () -> new PriorityQueue<Integer>(-3));
    }

    @Test
    void growsBeyondInitialCapacityWithoutLosingElements() {
        // start tiny so we're guaranteed to exercise the resize path
        PriorityQueue<Integer> pq = new PriorityQueue<>(2);
        for (int i = 20; i >= 1; i--) {
            pq.insert(i);
        }
        assertEquals(20, pq.size());

        for (int expected = 1; expected <= 20; expected++) {
            assertEquals(expected, pq.extractMin());
        }
        assertTrue(pq.isEmpty());
    }

    @Test
    void duplicatePrioritiesAreBothReturnedWithoutLoss() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.insert(4);
        pq.insert(4);
        pq.insert(1);

        assertEquals(1, pq.extractMin());
        assertEquals(4, pq.extractMin());
        assertEquals(4, pq.extractMin());
        assertTrue(pq.isEmpty());
    }

    @Test
    void singleElementInsertThenExtractEmptiesQueue() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.insert(42);
        assertEquals(42, pq.extractMin());
        assertTrue(pq.isEmpty());
    }
}
