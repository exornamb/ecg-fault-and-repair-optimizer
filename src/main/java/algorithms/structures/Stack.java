package algorithms.structures;

import java.util.NoSuchElementException;

public class Stack<T> {

    private Node<T> top;
    private int size;

    private static class Node<T> {

        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    public void push(T value) {

        Node<T> newNode =
                new Node<>(value);

        newNode.next = top;
        top = newNode;

        size++;
    }

    public T pop() {

        if (top == null) {
            throw new NoSuchElementException(
                    "Stack is empty"
            );
        }

        T value = top.data;

        top = top.next;

        size--;

        return value;
    }

    public T peek() {

        if (top == null) {
            throw new NoSuchElementException(
                    "Stack is empty"
            );
        }

        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    public void clear() {

        top = null;
        size = 0;
    }
}