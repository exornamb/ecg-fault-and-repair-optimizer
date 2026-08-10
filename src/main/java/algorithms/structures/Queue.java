package algorithms.structures;

import java.util.NoSuchElementException;

public class Queue<T> {

    private Node<T> front;
    private Node<T> rear;
    private int size;

    private static class Node<T> {

        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    public void enqueue(T value) {

        Node<T> newNode =
                new Node<>(value);

        if (rear == null) {

            front = newNode;
            rear = newNode;

        } else {

            rear.next = newNode;
            rear = newNode;
        }

        size++;
    }

    public T dequeue() {

        if (front == null) {
            throw new NoSuchElementException(
                    "Queue is empty"
            );
        }

        T value = front.data;

        front = front.next;

        size--;

        if (front == null) {
            rear = null;
        }

        return value;
    }

    public T peek() {

        if (front == null) {
            throw new NoSuchElementException(
                    "Queue is empty"
            );
        }

        return front.data;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return size;
    }

    public void clear() {

        front = null;
        rear = null;
        size = 0;
    }
}