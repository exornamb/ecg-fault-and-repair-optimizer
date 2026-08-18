package com.g15.dsa.structures;

import java.util.ArrayList;
import java.util.List;

public class BST<T extends Comparable<T>> {

    private Node<T> root;

    private static class Node<T> {

        T data;
        Node<T> left;
        Node<T> right;

        Node(T data) {
            this.data = data;
        }
    }

    public void insert(T value) {

        if (value == null) {
            throw new IllegalArgumentException(
                    "Cannot insert null"
            );
        }

        root = insertRecursive(root, value);
    }

    private Node<T> insertRecursive(
            Node<T> node,
            T value) {

        if (node == null) {
            return new Node<>(value);
        }

        int comparison =
                value.compareTo(node.data);

        if (comparison < 0) {

            node.left =
                    insertRecursive(
                            node.left,
                            value
                    );

        } else if (comparison > 0) {

            node.right =
                    insertRecursive(
                            node.right,
                            value
                    );
        }

        return node;
    }

    public boolean contains(T value) {
        return search(value);
    }

    public boolean search(T value) {

        if (value == null) {
            return false;
        }

        Node<T> current = root;

        while (current != null) {

            int comparison =
                    value.compareTo(current.data);

            if (comparison == 0) {
                return true;
            }

            if (comparison < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return false;
    }

    public List<T> inorder() {

        List<T> result =
                new ArrayList<>();

        inorderRecursive(
                root,
                result
        );

        return result;
    }

    private void inorderRecursive(
            Node<T> node,
            List<T> result) {

        if (node == null) {
            return;
        }

        inorderRecursive(
                node.left,
                result
        );

        result.add(node.data);

        inorderRecursive(
                node.right,
                result
        );
    }

    public List<T> searchPath(T value) {

        List<T> path =
                new ArrayList<>();

        if (value == null) {
            return path;
        }

        Node<T> current = root;

        while (current != null) {

            path.add(current.data);

            int comparison =
                    value.compareTo(current.data);

            if (comparison == 0) {
                break;
            }

            if (comparison < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return path;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {

        if (node == null) {
            return 0;
        }

        return 1 +
                Math.max(
                        height(node.left),
                        height(node.right)
                );
    }

    public void clear() {
        root = null;
    }
}