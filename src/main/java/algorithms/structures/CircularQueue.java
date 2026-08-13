package com.g15.dsa.queue;

import java.util.NoSuchElementException;

/**
 * Fixed-capacity array-based circular queue, implemented from scratch.
 *
 * Unlike Queue.java (which grows without bound), this reuses freed array
 * slots by advancing front/rear with modulo arithmetic, so a bounded buffer
 * (e.g. a fixed number of active dispatch slots) never has to shift elements
 * or reallocate. This wrap-around behaviour is the thing this structure is
 * built to demonstrate.
 *
 * Time complexity: enqueue O(1), dequeue O(1), peek O(1)
 * Space complexity: O(capacity)
 *
 * @param <T> type of elements held in this queue
 */
public class CircularQueue<T> {

    private final Object[] items;
    private final int capacity;
    private int front; // index of the next item to dequeue
    private int rear;  // index of the last item enqueued
    private int size;

    public CircularQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive, got " + capacity);
        }
        this.capacity = capacity;
        this.items = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    /**
     * Adds an item to the back of the queue, wrapping the rear index around
     * to index 0 once it passes the end of the backing array.
     * @throws IllegalArgumentException if item is null
     * @throws IllegalStateException if the queue is already full
     */
    public void enqueue(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot enqueue a null item");
        }
        if (isFull()) {
            throw new IllegalStateException("Circular queue is full (capacity " + capacity + ")");
        }
        rear = (rear + 1) % capacity;
        items[rear] = item;
        size++;
    }

    /**
     * Removes and returns the item at the front, wrapping the front index
     * around once it passes the end of the backing array.
     * @throws NoSuchElementException if the queue is empty
     */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot dequeue from an empty circular queue");
        }
        T data = (T) items[front];
        items[front] = null; // avoid holding a stale reference
        front = (front + 1) % capacity;
        size--;
        return data;
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot peek an empty circular queue");
        }
        return (T) items[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CircularQueue(front -> rear): [");
        for (int i = 0; i < size; i++) {
            int idx = (front + i) % capacity;
            sb.append(items[idx]);
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        sb.append("] (size=").append(size).append("/").append(capacity).append(")");
        return sb.toString();
    }
}
