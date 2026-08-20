package com.g15.dsa.experiments;

import com.g15.dsa.algorithms.searching.BinarySearch;
import com.g15.dsa.algorithms.searching.LinearSearch;
import com.g15.dsa.algorithms.sorting.*;
import com.g15.dsa.structures.*;
import com.g15.dsa.dao.AlgorithmRunDAO;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Experiments.java
 * Runs all 6 required empirical performance experiments and writes results to CSV.
 */
public class Experiments {

    private static final int RUNS = 5;
    private static final int[] SIZES = {100, 500, 1000, 5000, 10000};
    private static final int[] GRAPH_SIZES = {50, 100, 200, 500};
    private static final int[] HASH_SIZES = {100, 500, 1000, 5000, 10000, 20000};
    private static final Random RANDOM = new Random(42L);

    public static void main(String[] args) throws Exception {
        System.out.println("=== ECG Dumsor Response Optimizer - Performance Experiments ===");
        System.out.println("Group 15: Codebility v2.0\n");

        runSearchExperiment();
        runSortExperiment();
        runHashExperiment();
        runHeapExperiment();
        runTreeComparison();
        runGraphExperiment();

        System.out.println("\nAll experiments complete. Results written to data/ directory.");
    }

    // ===========================================================
    // EXPERIMENT 1: Search Comparison (Linear vs Binary)
    // ===========================================================
    static void runSearchExperiment() throws IOException {
        System.out.println("[1/6] Search Comparison...");
        AlgorithmRunDAO runDao = new AlgorithmRunDAO();
        try (FileWriter fw = new FileWriter("data/search_experiment.csv")) {
            fw.write("algorithm,input_size,run,time_ms\n");
            for (int n : SIZES) {
                Integer[] arr = generateSortedIntegerArray(n);
                Integer target = arr[RANDOM.nextInt(n)];

                for (int r = 0; r < RUNS; r++) {
                    long t0 = System.nanoTime();
                    LinearSearch.search(arr, target);
                    long elapsedNs1 = System.nanoTime() - t0;
                    double ms = elapsedNs1 / 1_000_000.0;
                    fw.write(String.format("LinearSearch,%d,%d,%.4f%n", n, r + 1, ms));
                    runDao.insertRun("LinearSearch", n, elapsedNs1, 0, r + 1);

                    t0 = System.nanoTime();
                    BinarySearch.search(arr, target);
                    long elapsedNs2 = System.nanoTime() - t0;
                    ms = elapsedNs2 / 1_000_000.0;
                    fw.write(String.format("BinarySearch,%d,%d,%.4f%n", n, r + 1, ms));
                    runDao.insertRun("BinarySearch", n, elapsedNs2, 0, r + 1);
                }
            }
        }
        System.out.println("  Done -> data/search_experiment.csv");
    }

    // ===========================================================
    // EXPERIMENT 2: Sorting Comparison
    // ===========================================================
    static void runSortExperiment() throws IOException {
        System.out.println("[2/6] Sorting Comparison...");
        AlgorithmRunDAO runDao = new AlgorithmRunDAO();
        try (FileWriter fw = new FileWriter("data/sort_experiment.csv")) {
            fw.write("algorithm,input_size,run,time_ms\n");
            for (int n : SIZES) {
                for (int r = 0; r < RUNS; r++) {
                    Integer[] arr;

                    arr = generateRandomIntegerArray(n);
                    long t = System.nanoTime(); SelectionSort.sort(arr); long elapsed = System.nanoTime() - t; double ms = elapsed/1_000_000.0;
                    fw.write(String.format("SelectionSort,%d,%d,%.4f%n", n, r+1, ms));
                    runDao.insertRun("SelectionSort", n, elapsed, 0, r + 1);

                    arr = generateRandomIntegerArray(n);
                    t = System.nanoTime(); InsertionSort.sort(arr); elapsed = System.nanoTime() - t; ms = elapsed/1_000_000.0;
                    fw.write(String.format("InsertionSort,%d,%d,%.4f%n", n, r+1, ms));
                    runDao.insertRun("InsertionSort", n, elapsed, 0, r + 1);

                    arr = generateRandomIntegerArray(n);
                    t = System.nanoTime(); MergeSort.sort(arr); elapsed = System.nanoTime() - t; ms = elapsed/1_000_000.0;
                    fw.write(String.format("MergeSort,%d,%d,%.4f%n", n, r+1, ms));
                    runDao.insertRun("MergeSort", n, elapsed, 0, r + 1);

                    arr = generateRandomIntegerArray(n);
                    t = System.nanoTime(); QuickSort.sort(arr); elapsed = System.nanoTime() - t; ms = elapsed/1_000_000.0;
                    fw.write(String.format("QuickSort,%d,%d,%.4f%n", n, r+1, ms));
                    runDao.insertRun("QuickSort", n, elapsed, 0, r + 1);
                }
            }
        }
        System.out.println("  Done -> data/sort_experiment.csv");
    }

    // ===========================================================
    // EXPERIMENT 3: Hash Table Load Factor & Collision Analysis
    // ===========================================================
    static void runHashExperiment() throws IOException {
        System.out.println("[3/6] Hash Table Load Factor & Collisions...");
        AlgorithmRunDAO runDao = new AlgorithmRunDAO();
        try (FileWriter fw = new FileWriter("data/hash_experiment.csv")) {
            fw.write("keys_inserted,final_capacity,load_factor,collisions\n");
            for (int n : HASH_SIZES) {
                long t0 = System.nanoTime();
                HashTable<Integer, Integer> ht = new HashTable<>();
                for (int i = 0; i < n; i++) {
                    ht.put(RANDOM.nextInt(n * 10), i);
                }
                long elapsed = System.nanoTime() - t0;
                fw.write(String.format("%d,%d,%.4f,%d%n",
                    n,
                    ht.capacity(),
                    (double) ht.size() / ht.capacity(),
                    ht.getCollisionCount()));
                runDao.insertRun("HashTablePut", n, elapsed, 0, 1);
            }
        }
        System.out.println("  Done -> data/hash_experiment.csv");
    }

    // ===========================================================
    // EXPERIMENT 4: Heap Priority Queue Dispatch Benchmark
    // ===========================================================
    static void runHeapExperiment() throws IOException {
        System.out.println("[4/6] Heap Priority Queue Dispatch...");
        AlgorithmRunDAO runDao = new AlgorithmRunDAO();
        try (FileWriter fw = new FileWriter("data/heap_experiment.csv")) {
            fw.write("operation,input_size,run,time_ms\n");
            for (int n : HASH_SIZES) {
                for (int r = 0; r < RUNS; r++) {
                    com.g15.dsa.structures.PriorityQueue<Integer> pq = new com.g15.dsa.structures.PriorityQueue<>();
                    Integer[] items = generateRandomIntegerArray(n);

                    long t = System.nanoTime();
                    for (Integer item : items) pq.insert(item);
                    long elapsedInsert = System.nanoTime() - t;
                    double ms = elapsedInsert/1_000_000.0;
                    fw.write(String.format("insert,%d,%d,%.4f%n", n, r+1, ms));
                    runDao.insertRun("HeapInsert", n, elapsedInsert, 0, r + 1);

                    t = System.nanoTime();
                    while (!pq.isEmpty()) pq.extractMin();
                    long elapsedExtract = System.nanoTime() - t;
                    ms = elapsedExtract/1_000_000.0;
                    fw.write(String.format("extractAll,%d,%d,%.4f%n", n, r+1, ms));
                    runDao.insertRun("HeapExtractAll", n, elapsedExtract, 0, r + 1);
                }
            }
        }
        System.out.println("  Done -> data/heap_experiment.csv");
    }

    // ===========================================================
    // EXPERIMENT 5: BST vs Red-Black Tree Height & Search
    // ===========================================================
    static void runTreeComparison() throws IOException {
        System.out.println("[5/6] BST vs Red-Black Tree Comparison...");
        AlgorithmRunDAO runDao = new AlgorithmRunDAO();
        try (FileWriter fw = new FileWriter("data/tree_experiment.csv")) {
            fw.write("tree_type,input_size,run,insert_ms,search_ms,height\n");
            for (int n : SIZES) {
                for (int r = 0; r < RUNS; r++) {
                    Integer[] items = generateRandomIntegerArray(n);

                    BST<Integer> bst = new BST<>();
                    long t = System.nanoTime();
                    for (Integer x : items) bst.insert(x);
                    long elapsedBstInsert = System.nanoTime() - t;
                    double insertMs = elapsedBstInsert/1_000_000.0;
                    t = System.nanoTime();
                    bst.contains(items[RANDOM.nextInt(n)]);
                    long elapsedBstSearch = System.nanoTime() - t;
                    double searchMs = elapsedBstSearch/1_000_000.0;
                    fw.write(String.format("BST,%d,%d,%.4f,%.4f,%d%n", n, r+1, insertMs, searchMs, bst.height()));
                    runDao.insertRun("BSTInsert", n, elapsedBstInsert, 0, r + 1);
                    runDao.insertRun("BSTSearch", n, elapsedBstSearch, 0, r + 1);

                    RedBlackTree<Integer> rbt = new RedBlackTree<>();
                    t = System.nanoTime();
                    for (Integer x : items) rbt.insert(x);
                    long elapsedRbtInsert = System.nanoTime() - t;
                    insertMs = elapsedRbtInsert/1_000_000.0;
                    t = System.nanoTime();
                    rbt.contains(items[RANDOM.nextInt(n)]);
                    long elapsedRbtSearch = System.nanoTime() - t;
                    searchMs = elapsedRbtSearch/1_000_000.0;
                    fw.write(String.format("RedBlackTree,%d,%d,%.4f,%.4f,%d%n", n, r+1, insertMs, searchMs, rbt.height()));
                    runDao.insertRun("RedBlackTreeInsert", n, elapsedRbtInsert, 0, r + 1);
                    runDao.insertRun("RedBlackTreeSearch", n, elapsedRbtSearch, 0, r + 1);
                }
            }
        }
        System.out.println("  Done -> data/tree_experiment.csv");
    }

    // ===========================================================
    // EXPERIMENT 6: Graph Algorithm Timing
    // ===========================================================
    static void runGraphExperiment() throws IOException {
        System.out.println("[6/6] Graph Algorithm Timing...");
        AlgorithmRunDAO runDao = new AlgorithmRunDAO();
        try (FileWriter fw = new FileWriter("data/graph_experiment.csv")) {
            fw.write("algorithm,vertices,edges,run,time_ms\n");
            for (int v : GRAPH_SIZES) {
                Graph g = generateRandomGraph(v, v * 3);
                for (int r = 0; r < RUNS; r++) {
                    long t; double ms;

                    t = System.nanoTime(); com.g15.dsa.algorithms.graph.BFS.traverse(g, 0); long elapsedBfs = System.nanoTime() - t; ms = elapsedBfs/1_000_000.0;
                    fw.write(String.format("BFS,%d,%d,%d,%.4f%n", v, v*3, r+1, ms));
                    runDao.insertRun("BFS", v, elapsedBfs, 0, r + 1);

                    t = System.nanoTime(); com.g15.dsa.algorithms.graph.DFS.traverse(g, 0); long elapsedDfs = System.nanoTime() - t; ms = elapsedDfs/1_000_000.0;
                    fw.write(String.format("DFS,%d,%d,%d,%.4f%n", v, v*3, r+1, ms));
                    runDao.insertRun("DFS", v, elapsedDfs, 0, r + 1);

                    t = System.nanoTime(); com.g15.dsa.algorithms.graph.Dijkstra.shortestPaths(g, 0); long elapsedDijkstra = System.nanoTime() - t; ms = elapsedDijkstra/1_000_000.0;
                    fw.write(String.format("Dijkstra,%d,%d,%d,%.4f%n", v, v*3, r+1, ms));
                    runDao.insertRun("Dijkstra", v, elapsedDijkstra, 0, r + 1);

                    t = System.nanoTime(); com.g15.dsa.algorithms.graph.Prim.minimumSpanningTree(g, 0); long elapsedPrim = System.nanoTime() - t; ms = elapsedPrim/1_000_000.0;
                    fw.write(String.format("Prim,%d,%d,%d,%.4f%n", v, v*3, r+1, ms));
                    runDao.insertRun("Prim", v, elapsedPrim, 0, r + 1);

                    t = System.nanoTime(); com.g15.dsa.algorithms.graph.Kruskal.minimumSpanningTree(g); long elapsedKruskal = System.nanoTime() - t; ms = elapsedKruskal/1_000_000.0;
                    fw.write(String.format("Kruskal,%d,%d,%d,%.4f%n", v, v*3, r+1, ms));
                    runDao.insertRun("Kruskal", v, elapsedKruskal, 0, r + 1);
                }
            }
        }
        System.out.println("  Done -> data/graph_experiment.csv");
    }

    // ===========================================================
    // HELPERS
    // ===========================================================

    static Integer[] generateSortedIntegerArray(int n) {
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = i * 2;
        return arr;
    }

    static Integer[] generateRandomIntegerArray(int n) {
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = RANDOM.nextInt(n * 10);
        return arr;
    }

    static Graph generateRandomGraph(int vertices, int edges) {
        Graph g = new Graph(vertices);
        for (int i = 0; i < edges; i++) {
            int from = RANDOM.nextInt(vertices);
            int to = RANDOM.nextInt(vertices);
            if (from != to) {
                double weight = 1 + RANDOM.nextInt(100);
                g.addUndirectedEdge(from, to, weight);
            }
        }
        return g;
    }
}
