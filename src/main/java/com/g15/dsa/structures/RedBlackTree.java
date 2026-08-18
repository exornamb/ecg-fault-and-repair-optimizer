package com.g15.dsa.structures;

import java.util.ArrayList;
import java.util.List;

public class RedBlackTree<T extends Comparable<T>> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        T data;
        Node left;
        Node right;
        Node parent;
        boolean color = RED;

        Node(T data) {
            this.data = data;
        }
    }

    private Node root;

    public void insert(T value) {

        if (value == null) {
            throw new IllegalArgumentException(
                    "Cannot insert null"
            );
        }

        Node newNode = new Node(value);

        if (root == null) {
            root = newNode;
            root.color = BLACK;
            return;
        }

        Node parent = null;
        Node current = root;

        while (current != null) {

            parent = current;

            int comparison =
                    value.compareTo(current.data);

            if (comparison < 0) {
                current = current.left;
            } else if (comparison > 0) {
                current = current.right;
            } else {
                return;
            }
        }

        newNode.parent = parent;

        if (value.compareTo(parent.data) < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }

        fixAfterInsertion(newNode);
    }

    private void fixAfterInsertion(Node node) {

        while (node != root && colorOf(parentOf(node)) == RED) {

            Node parent = parentOf(node);
            Node grandparent = parentOf(parent);

            if (parent == leftOf(grandparent)) {

                Node uncle = rightOf(grandparent);

                if (colorOf(uncle) == RED) {

                    setColor(parent, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grandparent, RED);

                    node = grandparent;

                } else {

                    if (node == rightOf(parent)) {

                        node = parent;

                        rotateLeft(node);

                        parent = parentOf(node);
                        grandparent = parentOf(parent);
                    }

                    setColor(parent, BLACK);
                    setColor(grandparent, RED);

                    rotateRight(grandparent);
                }

            } else {

                Node uncle = leftOf(grandparent);

                if (colorOf(uncle) == RED) {

                    setColor(parent, BLACK);
                    setColor(uncle, BLACK);
                    setColor(grandparent, RED);

                    node = grandparent;

                } else {

                    if (node == leftOf(parent)) {

                        node = parent;

                        rotateRight(node);

                        parent = parentOf(node);
                        grandparent = parentOf(parent);
                    }

                    setColor(parent, BLACK);
                    setColor(grandparent, RED);

                    rotateLeft(grandparent);
                }
            }
        }

        root.color = BLACK;
    }

    private void rotateLeft(Node node) {

        if (node == null) {
            return;
        }

        Node right = node.right;

        node.right = right.left;

        if (right.left != null) {
            right.left.parent = node;
        }

        right.parent = node.parent;

        if (node.parent == null) {
            root = right;

        } else if (node == node.parent.left) {
            node.parent.left = right;

        } else {
            node.parent.right = right;
        }

        right.left = node;
        node.parent = right;
    }

    private void rotateRight(Node node) {

        if (node == null) {
            return;
        }

        Node left = node.left;

        node.left = left.right;

        if (left.right != null) {
            left.right.parent = node;
        }

        left.parent = node.parent;

        if (node.parent == null) {
            root = left;

        } else if (node == node.parent.right) {
            node.parent.right = left;

        } else {
            node.parent.left = left;
        }

        left.right = node;
        node.parent = left;
    }

    public boolean contains(T value) {
        return search(value);
    }

    public boolean search(T value) {

        if (value == null) {
            return false;
        }

        Node current = root;

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

        inorder(root, result);

        return result;
    }

    private void inorder(
            Node node,
            List<T> result) {

        if (node == null) {
            return;
        }

        inorder(node.left, result);

        result.add(node.data);

        inorder(node.right, result);
    }

    public int height() {
        return height(root);
    }

    private int height(Node node) {

        if (node == null) {
            return 0;
        }

        return 1 + Math.max(
                height(node.left),
                height(node.right)
        );
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
    }

    private Node parentOf(Node node) {
        return node == null ? null : node.parent;
    }

    private Node leftOf(Node node) {
        return node == null ? null : node.left;
    }

    private Node rightOf(Node node) {
        return node == null ? null : node.right;
    }

    private boolean colorOf(Node node) {
        return node == null ? BLACK : node.color;
    }

    private void setColor(
            Node node,
            boolean color) {

        if (node != null) {
            node.color = color;
        }
    }
}