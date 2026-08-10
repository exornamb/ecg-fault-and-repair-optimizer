package algorithms.structures;

import java.util.NoSuchElementException;

public class CircularQueue<T> {

    private Object[] data;
    private int front;
    private int rear;
    private int size;

    public CircularQueue(int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Capacity must be greater than zero"
            );
        }

        data = new Object[capacity];
        front = 0;
        rear = 0;
        size = 0;
    }

    public void enqueue(T value) {

        if (isFull()) {
            throw new IllegalStateException(
                    "Circular queue is full"
            );
        }

        data[rear] = value;

        rear = (rear + 1) % data.length;

        size++;
    }

    public T dequeue() {

        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Circular queue is empty"
            );
        }

        T value = elementAt(front);

        data[front] = null;

        front = (front + 1) % data.length;

        size--;

        return value;
    }

    public T peek() {

        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Circular queue is empty"
            );
        }

        return elementAt(front);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == data.length;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return data.length;
    }

    public void clear() {

        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }

        front = 0;
        rear = 0;
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private T elementAt(int index) {
        return (T) data[index];
    }
}