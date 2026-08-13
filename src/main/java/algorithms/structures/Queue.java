package com.g15.dsa.queue;

import java.util.NoSuchElementException;

/**
 * Generic singly-linked-list based FIFO queue, implemented from scratch
 * (no java.util.Queue / java.util.LinkedList used internally).
 *
 * Used in the ECG Dumsor Response Optimizer for the plain first-come-first-served
 * service request line, and as the backing structure that CircularQueue's
 * behaviour is compared against.
 *
 * Time complexity: enqueue O(1), dequeue O(1), peek O(1), isEmpty O(1), size O(1)
 * Space complexity: O(n)
 *
 * @param <T> type of elements held in this queue
 */
public class Queue<T> {

    /** Internal singly-linked node. Package-private on purpose: callers never see it. */
    private static class Node<T> {
        final T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> front; // next item to be removed
    private Node<T> rear;  // last item added
    private int size;

    public Queue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    /**
     * Adds an item to the back of the queue.
     * @param item element to add; must not be null
     * @throws IllegalArgumentException if item is null
     */
    public void enqueue(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot enqueue a null item");
        }
        Node<T> node = new Node<>(item);
        if (isEmpty()) {
            front = node;
            rear = node;
        } else {
            rear.next = node;
            rear = node;
        }
        size++;
    }

    /**
     * Removes and returns the item at the front of the queue.
     * @throws NoSuchElementException if the queue is empty
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot dequeue from an empty queue");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null; // queue is now empty
        }
        size--;
        return data;
    }

    /**
     * Returns, without removing, the item at the front of the queue.
     * @throws NoSuchElementException if the queue is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot peek an empty queue");
        }
        return front.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /** Removes all elements, leaving an empty queue. O(1) — old nodes are GC'd. */
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Queue(front -> rear): [");
        Node<T> curr = front;
        while (curr != null) {
            sb.append(curr.data);
            if (curr.next != null) {
                sb.append(", ");
            }
            curr = curr.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
