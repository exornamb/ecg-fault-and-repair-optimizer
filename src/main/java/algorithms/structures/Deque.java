package com.g15.dsa.queue;

import java.util.NoSuchElementException;

/**
 * Generic doubly-linked-list based double-ended queue, implemented from
 * scratch. Supports insertion and removal at both ends in O(1).
 *
 * In the dispatch system this backs the "urgent insertion" behaviour: normal
 * service requests are pushed to the back with addLast (ordinary FIFO), but
 * a high-priority fault can jump the whole line with addFirst so it is the
 * very next thing removeFirst() returns.
 *
 * Time complexity: addFirst/addLast/removeFirst/removeLast/peekFirst/peekLast
 * are all O(1). Space complexity: O(n)
 *
 * @param <T> type of elements held in this deque
 */
public class Deque<T> {

    private static class Node<T> {
        final T data;
        Node<T> prev;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head; // front of the deque
    private Node<T> tail; // back of the deque
    private int size;

    public Deque() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /** Inserts an item at the front. Used for urgent requests that must jump the queue. */
    public void addFirst(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item");
        }
        Node<T> node = new Node<>(item);
        if (isEmpty()) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    /** Inserts an item at the back. Used for ordinary, non-urgent requests. */
    public void addLast(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item");
        }
        Node<T> node = new Node<>(item);
        if (isEmpty()) {
            head = node;
            tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
        size++;
    }

    /** Removes and returns the item at the front. */
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from an empty deque");
        }
        T data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        } else {
            head.prev = null;
        }
        size--;
        return data;
    }

    /** Removes and returns the item at the back. */
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from an empty deque");
        }
        T data = tail.data;
        tail = tail.prev;
        if (tail == null) {
            head = null;
        } else {
            tail.next = null;
        }
        size--;
        return data;
    }

    public T peekFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot peek an empty deque");
        }
        return head.data;
    }

    public T peekLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot peek an empty deque");
        }
        return tail.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Deque(front -> back): [");
        Node<T> curr = head;
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
