package algorithms.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Iterable<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {

        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    public void add(T value) {

        Node<T> newNode =
                new Node<>(value);

        if (head == null) {

            head = newNode;
            tail = newNode;

        } else {

            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    public void addFirst(T value) {

        Node<T> newNode =
                new Node<>(value);

        newNode.next = head;
        head = newNode;

        if (tail == null) {
            tail = newNode;
        }

        size++;
    }

    public T get(int index) {

        return nodeAt(index).data;
    }

    public T removeFirst() {

        if (head == null) {
            throw new NoSuchElementException(
                    "List is empty"
            );
        }

        T value = head.data;

        head = head.next;

        size--;

        if (size == 0) {
            tail = null;
        }

        return value;
    }

    public boolean remove(T value) {

        if (head == null) {
            return false;
        }

        if (equalsValue(head.data, value)) {

            removeFirst();

            return true;
        }

        Node<T> current = head;

        while (current.next != null) {

            if (equalsValue(
                    current.next.data,
                    value)) {

                if (current.next == tail) {
                    tail = current;
                }

                current.next =
                        current.next.next;

                size--;

                return true;
            }

            current = current.next;
        }

        return false;
    }

    public boolean contains(T value) {

        Node<T> current = head;

        while (current != null) {

            if (equalsValue(
                    current.data,
                    value)) {

                return true;
            }

            current = current.next;
        }

        return false;
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

    private Node<T> nodeAt(int index) {

        if (index < 0 || index >= size) {

            throw new IndexOutOfBoundsException(
                    "Index: " + index +
                            ", Size: " + size
            );
        }

        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current;
    }

    private boolean equalsValue(
            T first,
            T second) {

        if (first == null) {
            return second == null;
        }

        return first.equals(second);
    }

    @Override
    public Iterator<T> iterator() {

        return new Iterator<>() {

            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {

                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                T value = current.data;

                current = current.next;

                return value;
            }
        };
    }
}