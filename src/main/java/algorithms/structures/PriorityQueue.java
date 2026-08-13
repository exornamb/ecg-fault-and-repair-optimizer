package com.g15.dsa.queue;

import java.util.NoSuchElementException;

/**
 * Generic binary min-heap priority queue, implemented from scratch on top of
 * a resizable array (no java.util.PriorityQueue used).
 *
 * The element that compares smallest is always returned first. In the
 * dispatch system, requests are wrapped so that a lower urgency number means
 * "more urgent" (e.g. 1 = critical outage, 5 = routine fault), which is why
 * extractMin() below doubles as "get the next crew's highest-priority job" —
 * this is the structure behind the brief's "heap dispatch order" requirement.
 *
 * Time complexity: insert O(log n), extractMin O(log n), peek O(1)
 * Space complexity: O(n)
 *
 * @param <T> element type; must implement Comparable
 */
public class PriorityQueue<T extends Comparable<T>> {

    private static final int DEFAULT_CAPACITY = 16;

    private Object[] heap;
    private int size;

    public PriorityQueue() {
        this(DEFAULT_CAPACITY);
    }

    public PriorityQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive, got " + initialCapacity);
        }
        this.heap = new Object[initialCapacity];
        this.size = 0;
    }

    /**
     * Inserts an item and restores the heap property by "sifting up".
     * @throws IllegalArgumentException if item is null
     */
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot insert a null item");
        }
        ensureCapacity();
        heap[size] = item;
        siftUp(size);
        size++;
    }

    /**
     * Removes and returns the highest-priority (smallest, per compareTo) item,
     * restoring the heap property by "sifting down" from the root.
     * @throws NoSuchElementException if the priority queue is empty
     */
    @SuppressWarnings("unchecked")
    public T extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot extract from an empty priority queue");
        }
        T min = (T) heap[0];
        size--;
        heap[0] = heap[size];
        heap[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return min;
    }

    /** Returns, without removing, the highest-priority item. */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot peek an empty priority queue");
        }
        return (T) heap[0];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (((T) heap[i]).compareTo((T) heap[parent]) < 0) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;

            if (left < size && ((T) heap[left]).compareTo((T) heap[smallest]) < 0) {
                smallest = left;
            }
            if (right < size && ((T) heap[right]).compareTo((T) heap[smallest]) < 0) {
                smallest = right;
            }
            if (smallest == i) {
                break;
            }
            swap(i, smallest);
            i = smallest;
        }
    }

    private void swap(int i, int j) {
        Object tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            Object[] bigger = new Object[heap.length * 2];
            System.arraycopy(heap, 0, bigger, 0, heap.length);
            heap = bigger;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        StringBuilder sb = new StringBuilder("PriorityQueue(heap array, root first): [");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
