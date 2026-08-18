package com.g15.dsa.structures;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

    @Test
    void enqueueThenDequeueMaintainsFifoOrder() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);

        assertEquals(1, q.dequeue());
        assertEquals(2, q.dequeue());
        assertEquals(3, q.dequeue());
    }

    @Test
    void peekReturnsFrontWithoutRemoving() {
        Queue<String> q = new Queue<>();
        q.enqueue("first");
        q.enqueue("second");

        assertEquals("first", q.peek());
        assertEquals(2, q.size(), "peek must not change size");
        assertEquals("first", q.peek());
    }

    @Test
    void newQueueIsEmpty() {
        Queue<Integer> q = new Queue<>();
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
    }

    @Test
    void sizeTracksNumberOfElements() {
        Queue<Integer> q = new Queue<>();
        assertEquals(0, q.size());
        q.enqueue(10);
        assertEquals(1, q.size());
        q.enqueue(20);
        assertEquals(2, q.size());
        q.dequeue();
        assertEquals(1, q.size());
    }

    @Test
    void singleElementEnqueueThenDequeueEmptiesQueue() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(42);
        assertEquals(42, q.dequeue());
        assertTrue(q.isEmpty());
    }

    @Test
    void dequeueOnEmptyQueueThrows() {
        Queue<Integer> q = new Queue<>();
        assertThrows(NoSuchElementException.class, q::dequeue);
    }

    @Test
    void peekOnEmptyQueueThrows() {
        Queue<Integer> q = new Queue<>();
        assertThrows(NoSuchElementException.class, q::peek);
    }

    @Test
    void enqueueNullThrows() {
        Queue<String> q = new Queue<>();
        assertThrows(IllegalArgumentException.class, () -> q.enqueue(null));
    }

    @Test
    void clearEmptiesQueueAndResetsSize() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.clear();

        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
        assertThrows(NoSuchElementException.class, q::dequeue);
    }

    @Test
    void queueCanBeReusedAfterBecomingEmpty() {
        Queue<Integer> q = new Queue<>();
        q.enqueue(1);
        q.dequeue();
        // front/rear must reset correctly so the queue still works after emptying
        q.enqueue(2);
        q.enqueue(3);

        assertEquals(2, q.dequeue());
        assertEquals(3, q.dequeue());
        assertTrue(q.isEmpty());
    }
}
