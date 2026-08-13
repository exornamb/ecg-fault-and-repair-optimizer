package algorithms.structures;

import java.util.ArrayList;
import java.util.List;

public class BTree<T extends Comparable<T>> {

    private final int minimumDegree;
    private Node root;

    private class Node {

        List<T> keys = new ArrayList<>();
        List<Node> children = new ArrayList<>();
        boolean leaf;

        Node(boolean leaf) {
            this.leaf = leaf;
        }
    }

    public BTree() {
        this(2);
    }

    public BTree(int minimumDegree) {

        if (minimumDegree < 2) {
            throw new IllegalArgumentException(
                    "Minimum degree must be at least 2"
            );
        }

        this.minimumDegree = minimumDegree;
        this.root = new Node(true);
    }

    // =========================================================
    // INSERT
    // =========================================================

    public void insert(T value) {

        if (value == null) {
            throw new IllegalArgumentException(
                    "Cannot insert null"
            );
        }

        if (contains(value)) {
            return;
        }

        if (root.keys.size()
                == 2 * minimumDegree - 1) {

            Node newRoot = new Node(false);

            newRoot.children.add(root);

            root = newRoot;

            splitChild(
                    root,
                    0
            );
        }

        insertNonFull(
                root,
                value
        );
    }

    private void insertNonFull(
            Node node,
            T value) {

        int index =
                node.keys.size() - 1;

        if (node.leaf) {

            node.keys.add(null);

            while (
                    index >= 0
                            && value.compareTo(
                            node.keys.get(index)
                    ) < 0
            ) {

                node.keys.set(
                        index + 1,
                        node.keys.get(index)
                );

                index--;
            }

            node.keys.set(
                    index + 1,
                    value
            );

        } else {

            while (
                    index >= 0
                            && value.compareTo(
                            node.keys.get(index)
                    ) < 0
            ) {
                index--;
            }

            index++;

            Node child =
                    node.children.get(index);

            if (
                    child.keys.size()
                            == 2 * minimumDegree - 1
            ) {

                splitChild(
                        node,
                        index
                );

                if (
                        value.compareTo(
                                node.keys.get(index)
                        ) > 0
                ) {
                    index++;
                }
            }

            insertNonFull(
                    node.children.get(index),
                    value
            );
        }
    }

    // =========================================================
    // NODE SPLITTING
    // =========================================================

    private void splitChild(
            Node parent,
            int childIndex) {

        Node fullChild =
                parent.children.get(childIndex);

        Node newChild =
                new Node(fullChild.leaf);

        T middleKey =
                fullChild.keys.get(
                        minimumDegree - 1
                );

        // Move keys after the middle key
        for (
                int i = minimumDegree;
                i < fullChild.keys.size();
                i++
        ) {

            newChild.keys.add(
                    fullChild.keys.get(i)
            );
        }

        // Remove moved keys from old child
        while (
                fullChild.keys.size()
                        > minimumDegree - 1
        ) {

            fullChild.keys.remove(
                    fullChild.keys.size() - 1
            );
        }

        // Move children if this is an internal node
        if (!fullChild.leaf) {

            for (
                    int i = minimumDegree;
                    i < fullChild.children.size();
                    i++
            ) {

                newChild.children.add(
                        fullChild.children.get(i)
                );
            }

            while (
                    fullChild.children.size()
                            > minimumDegree
            ) {

                fullChild.children.remove(
                        fullChild.children.size() - 1
                );
            }
        }

        parent.children.add(
                childIndex + 1,
                newChild
        );

        parent.keys.add(
                childIndex,
                middleKey
        );
    }

    // =========================================================
    // SEARCH
    // =========================================================

    public boolean contains(T value) {

        if (value == null) {
            return false;
        }

        return search(
                root,
                value
        );
    }

    private boolean search(
            Node node,
            T value) {

        int index = 0;

        while (
                index < node.keys.size()
                        && value.compareTo(
                        node.keys.get(index)
                ) > 0
        ) {

            index++;
        }

        if (
                index < node.keys.size()
                        && value.compareTo(
                        node.keys.get(index)
                ) == 0
        ) {
            return true;
        }

        if (node.leaf) {
            return false;
        }

        return search(
                node.children.get(index),
                value
        );
    }

    // =========================================================
    // SEARCH PATH
    // =========================================================

    public List<List<T>> searchPath(T value) {

        List<List<T>> path =
                new ArrayList<>();

        if (value == null) {
            return path;
        }

        Node current = root;

        while (current != null) {

            path.add(
                    new ArrayList<>(
                            current.keys
                    )
            );

            int index = 0;

            while (
                    index < current.keys.size()
                            && value.compareTo(
                            current.keys.get(index)
                    ) > 0
            ) {

                index++;
            }

            if (
                    index < current.keys.size()
                            && value.compareTo(
                            current.keys.get(index)
                    ) == 0
            ) {

                break;
            }

            if (current.leaf) {
                break;
            }

            current =
                    current.children.get(index);
        }

        return path;
    }

    // =========================================================
    // TRAVERSAL
    // =========================================================

    public List<T> inorder() {

        List<T> result =
                new ArrayList<>();

        inorder(
                root,
                result
        );

        return result;
    }

    private void inorder(
            Node node,
            List<T> result) {

        if (node.leaf) {

            result.addAll(
                    node.keys
            );

            return;
        }

        for (
                int i = 0;
                i < node.keys.size();
                i++
        ) {

            inorder(
                    node.children.get(i),
                    result
            );

            result.add(
                    node.keys.get(i)
            );
        }

        inorder(
                node.children.get(
                        node.children.size() - 1
                ),
                result
        );
    }

    // =========================================================
    // UTILITY
    // =========================================================

    public boolean isEmpty() {
        return root.keys.isEmpty();
    }

    public int size() {
        return countKeys(root);
    }

    private int countKeys(Node node) {

        int count =
                node.keys.size();

        if (!node.leaf) {

            for (Node child :
                    node.children) {

                count += countKeys(child);
            }
        }

        return count;
    }

    public int height() {
        return height(root);
    }

    private int height(Node node) {

        if (node.leaf) {
            return 1;
        }

        return 1 +
                height(
                        node.children.get(0)
                );
    }

    public void clear() {
        root = new Node(true);
    }

    public int getMinimumDegree() {
        return minimumDegree;
    }
}