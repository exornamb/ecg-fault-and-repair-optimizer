package algorithms.structures;

import java.util.NoSuchElementException;

public class Deque<T> {

    private Node<T> front;
    private Node<T> rear;
    private int size;

    private static class Node<T> {

        T data;
        Node<T> next;
        Node<T> previous;

        Node(T data) {
            this.data = data;
        }
    }

    public void addFront(T value) {

        Node<T> newNode =
                new Node<>(value);

        if (front == null) {

            front = newNode;
            rear = newNode;

        } else {

            newNode.next = front;
            front.previous = newNode;
            front = newNode;
        }

        size++;
    }

    public void addRear(T value) {

        Node<T> newNode =
                new Node<>(value);

        if (rear == null) {

            front = newNode;
            rear = newNode;

        } else {

            rear.next = newNode;
            newNode.previous = rear;
            rear = newNode;
        }

        size++;
    }

    public T removeFront() {

        if (front == null) {
            throw new NoSuchElementException(
                    "Deque is empty"
            );
        }

        T value = front.data;

        front = front.next;

        if (front == null) {
            rear = null;
        } else {
            front.previous = null;
        }

        size--;

        return value;
    }

    public T removeRear() {

        if (rear == null) {
            throw new NoSuchElementException(
                    "Deque is empty"
            );
        }

        T value = rear.data;

        rear = rear.previous;

        if (rear == null) {
            front = null;
        } else {
            rear.next = null;
        }

        size--;

        return value;
    }

    public T peekFront() {

        if (front == null) {
            throw new NoSuchElementException(
                    "Deque is empty"
            );
        }

        return front.data;
    }

    public T peekRear() {

        if (rear == null) {
            throw new NoSuchElementException(
                    "Deque is empty"
            );
        }

        return rear.data;
    }

    public boolean isEmpty() {
        return size == 0;
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