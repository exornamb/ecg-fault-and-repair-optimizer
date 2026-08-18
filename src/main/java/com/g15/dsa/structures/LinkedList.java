package com.g15.dsa.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Custom Generic Doubly-Linked List implementation built from scratch.
 * 
 * Supports O(1) head and tail insertions and deletions, bidirectional pointer
 * traversal, and fail-safe index access.
 * 
 * @param <T> Element type
 */
public class LinkedList<T> implements Iterable<T> {

    public static class Node<T> {
        public T data;
        public Node<T> prev;
        public Node<T> next;

        public Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public void add(T data) {
        addLast(data);
    }

    public void add(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) {
            addFirst(data);
            return;
        }
        if (index == size) {
            addLast(data);
            return;
        }
        Node<T> current = getNode(index);
        Node<T> newNode = new Node<>(data);
        newNode.prev = current.prev;
        newNode.next = current;
        current.prev.next = newNode;
        current.prev = newNode;
        size++;
    }

    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        T data = head.data;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        size--;
        return data;
    }

    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        T data = tail.data;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return data;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) return removeFirst();
        if (index == size - 1) return removeLast();

        Node<T> target = getNode(index);
        T data = target.data;
        target.prev.next = target.next;
        target.next.prev = target.prev;
        size--;
        return data;
    }

    public boolean remove(T data) {
        Node<T> current = head;
        while (current != null) {
            if ((data == null && current.data == null) || (data != null && data.equals(current.data))) {
                if (current == head) removeFirst();
                else if (current == tail) removeLast();
                else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                    size--;
                }
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public T getFirst() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        return head.data;
    }

    public T getLast() {
        if (isEmpty()) throw new NoSuchElementException("List is empty");
        return tail.data;
    }

    public T get(int index) {
        return getNode(index).data;
    }

    public T set(int index, T data) {
        Node<T> node = getNode(index);
        T old = node.data;
        node.data = data;
        return old;
    }

    public boolean contains(T data) {
        return indexOf(data) != -1;
    }

    public int indexOf(T data) {
        int index = 0;
        Node<T> current = head;
        while (current != null) {
            if ((data == null && current.data == null) || (data != null && data.equals(current.data))) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index < (size >> 1)) {
            Node<T> current = head;
            for (int i = 0; i < index; i++) current = current.next;
            return current;
        } else {
            Node<T> current = tail;
            for (int i = size - 1; i > index; i--) current = current.prev;
            return current;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(" <-> ");
            current = current.next;
        }
        return sb.append("]").toString();
    }
}
