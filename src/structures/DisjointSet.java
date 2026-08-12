package structures;

public class DisjointSet {

    private final int[] parent;
    private final int[] rank;

    public DisjointSet(int size) {

        if (size <= 0) {
            throw new IllegalArgumentException(
                    "Size must be greater than zero"
            );
        }

        parent = new int[size];
        rank = new int[size];

        makeSet();
    }

    // =========================================================
    // MAKE SET
    // =========================================================

    private void makeSet() {

        for (int i = 0; i < parent.length; i++) {

            parent[i] = i;
            rank[i] = 0;
        }
    }

    // =========================================================
    // FIND WITH PATH COMPRESSION
    // =========================================================

    public int find(int value) {

        validate(value);

        if (parent[value] != value) {

            parent[value] =
                    find(parent[value]);
        }

        return parent[value];
    }

    // =========================================================
    // UNION BY RANK
    // =========================================================

    public void union(
            int first,
            int second) {

        int rootFirst = find(first);
        int rootSecond = find(second);

        if (rootFirst == rootSecond) {
            return;
        }

        if (rank[rootFirst] < rank[rootSecond]) {

            parent[rootFirst] = rootSecond;

        } else if (
                rank[rootFirst] > rank[rootSecond]
        ) {

            parent[rootSecond] = rootFirst;

        } else {

            parent[rootSecond] = rootFirst;

            rank[rootFirst]++;
        }
    }

    // =========================================================
    // CONNECTIVITY
    // =========================================================

    public boolean connected(
            int first,
            int second) {

        return find(first) == find(second);
    }

    // =========================================================
    // SIZE
    // =========================================================

    public int size() {
        return parent.length;
    }

    // =========================================================
    // VALIDATION
    // =========================================================

    private void validate(int value) {

        if (value < 0 || value >= parent.length) {

            throw new IndexOutOfBoundsException(
                    "Value " + value +
                            " is outside the valid range 0-" +
                            (parent.length - 1)
            );
        }
    }

    // =========================================================
    // DEBUG / TRACE SUPPORT
    // =========================================================

    public int[] getParents() {

        return parent.clone();
    }

    public int[] getRanks() {

        return rank.clone();
    }
}