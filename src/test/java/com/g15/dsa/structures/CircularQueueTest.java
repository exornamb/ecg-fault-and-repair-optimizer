package com.g15.dsa.structures;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class CircularQueueTest {

    @Test
    void enqueueThenDequeueMaintainsFifoOrder() {
        CircularQueue<Integer> cq = new CircularQueue<>(5);
        cq.enqueue(1);
        cq.enqueue(2);
        cq.enqueue(3);

        assertEquals(1, cq.dequeue());
        assertEquals(2, cq.dequeue());
        assertEquals(3, cq.dequeue());
    }

    @Test
    void wrapAroundReusesFreedSlotsCorrectly() {
        // capacity 3: fill it, drain two, refill two more -> rear index must wrap past the array end
        CircularQueue<String> cq = new CircularQueue<>(3);
        cq.enqueue("A");
        cq.enqueue("B");
        cq.enqueue("C");

        assertEquals("A", cq.dequeue());
        assertEquals("B", cq.dequeue());

        cq.enqueue("D"); // rear wraps to index 0
        cq.enqueue("E"); // rear wraps to index 1

        assertEquals("C", cq.dequeue());
        assertEquals("D", cq.dequeue());
        assertEquals("E", cq.dequeue());
        assertTrue(cq.isEmpty());
    }

    @Test
    void isFullAfterFillingToCapacity() {
        CircularQueue<Integer> cq = new CircularQueue<>(2);
        assertFalse(cq.isFull());
        cq.enqueue(1);
        cq.enqueue(2);
        assertTrue(cq.isFull());
    }

    @Test
    void enqueueOnFullQueueThrows() {
        CircularQueue<Integer> cq = new CircularQueue<>(1);
        cq.enqueue(100);
        assertThrows(IllegalStateException.class, () -> cq.enqueue(200));
    }

    @Test
    void dequeueOnEmptyQueueThrows() {
        CircularQueue<Integer> cq = new CircularQueue<>(3);
        assertThrows(NoSuchElementException.class, cq::dequeue);
    }

    @Test
    void peekOnEmptyQueueThrows() {
        CircularQueue<Integer> cq = new CircularQueue<>(3);
        assertThrows(NoSuchElementException.class, cq::peek);
    }

    @Test
    void enqueueNullThrows() {
        CircularQueue<String> cq = new CircularQueue<>(3);
        assertThrows(IllegalArgumentException.class, () -> cq.enqueue(null));
    }

    @Test
    void constructorRejectsNonPositiveCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new CircularQueue<Integer>(0));
        assertThrows(IllegalArgumentException.class, () -> new CircularQueue<Integer>(-5));
    }

    @Test
    void sizeAndCapacityAccessorsAreConsistent() {
        CircularQueue<Integer> cq = new CircularQueue<>(4);
        assertEquals(4, cq.capacity());
        assertEquals(0, cq.size());
        cq.enqueue(1);
        cq.enqueue(2);
        assertEquals(2, cq.size());
    }

    @Test
    void peekDoesNotRemoveElement() {
        CircularQueue<Integer> cq = new CircularQueue<>(3);
        cq.enqueue(9);
        assertEquals(9, cq.peek());
        assertEquals(1, cq.size());
        assertEquals(9, cq.dequeue());
    }
}
