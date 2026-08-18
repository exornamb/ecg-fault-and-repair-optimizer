package com.g15.dsa.experiments;

import com.g15.dsa.algorithms.searching.BinarySearch;
import com.g15.dsa.algorithms.searching.LinearSearch;
import com.g15.dsa.algorithms.sorting.*;
import com.g15.dsa.structures.*;

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
        try (FileWriter fw = new FileWriter("data/search_experiment.csv")) {
            fw.write("algorithm,input_size,run,time_ms\n");
            for (int n : SIZES) {
                Integer[] arr = generateSortedIntegerArray(n);
                Integer target = arr[RANDOM.nextInt(n)];

                for (int r = 0; r < RUNS; r++) {
                    long t0 = System.nanoTime();
                    LinearSearch.search(arr, target);
                    double ms = (System.nanoTime() - t0) / 1_000_000.0;
                    fw.write(String.format("LinearSearch,%d,%d,%.4f%n", n, r + 1, ms));

                    t0 = System.nanoTime();
                    BinarySearch.search(arr, target);
                    ms = (System.nanoTime() - t0) / 1_000_000.0;
                    fw.write(String.format("BinarySearch,%d,%d,%.4f%n", n, r + 1, ms));
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
        try (FileWriter fw = new FileWriter("data/sort_experiment.csv")) {
            fw.write("algorithm,input_size,run,time_ms\n");
            for (int n : SIZES) {
                for (int r = 0; r < RUNS; r++) {
                    Integer[] arr;

                    arr = generateRandomIntegerArray(n);
                    long t = System.nanoTime(); SelectionSort.sort(arr); double ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("SelectionSort,%d,%d,%.4f%n", n, r+1, ms));

                    arr = generateRandomIntegerArray(n);
                    t = System.nanoTime(); InsertionSort.sort(arr); ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("InsertionSort,%d,%d,%.4f%n", n, r+1, ms));

                    arr = generateRandomIntegerArray(n);
                    t = System.nanoTime(); MergeSort.sort(arr); ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("MergeSort,%d,%d,%.4f%n", n, r+1, ms));

                    arr = generateRandomIntegerArray(n);
                    t = System.nanoTime(); QuickSort.sort(arr); ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("QuickSort,%d,%d,%.4f%n", n, r+1, ms));
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
        try (FileWriter fw = new FileWriter("data/hash_experiment.csv")) {
            fw.write("keys_inserted,final_capacity,load_factor,collisions\n");
            for (int n : HASH_SIZES) {
                HashTable<Integer, Integer> ht = new HashTable<>();
                for (int i = 0; i < n; i++) {
                    ht.put(RANDOM.nextInt(n * 10), i);
                }
                fw.write(String.format("%d,%d,%.4f,%d%n",
                    n,
                    ht.capacity(),
                    (double) ht.size() / ht.capacity(),
                    ht.getCollisionCount()));
            }
        }
        System.out.println("  Done -> data/hash_experiment.csv");
    }

    // ===========================================================
    // EXPERIMENT 4: Heap Priority Queue Dispatch Benchmark
    // ===========================================================
    static void runHeapExperiment() throws IOException {
        System.out.println("[4/6] Heap Priority Queue Dispatch...");
        try (FileWriter fw = new FileWriter("data/heap_experiment.csv")) {
            fw.write("operation,input_size,run,time_ms\n");
            for (int n : HASH_SIZES) {
                for (int r = 0; r < RUNS; r++) {
                    com.g15.dsa.structures.PriorityQueue<Integer> pq = new com.g15.dsa.structures.PriorityQueue<>();
                    Integer[] items = generateRandomIntegerArray(n);

                    long t = System.nanoTime();
                    for (Integer item : items) pq.insert(item);
                    double ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("insert,%d,%d,%.4f%n", n, r+1, ms));

                    t = System.nanoTime();
                    while (!pq.isEmpty()) pq.extractMin();
                    ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("extractAll,%d,%d,%.4f%n", n, r+1, ms));
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
        try (FileWriter fw = new FileWriter("data/tree_experiment.csv")) {
            fw.write("tree_type,input_size,run,insert_ms,search_ms,height\n");
            for (int n : SIZES) {
                for (int r = 0; r < RUNS; r++) {
                    Integer[] items = generateRandomIntegerArray(n);

                    BST<Integer> bst = new BST<>();
                    long t = System.nanoTime();
                    for (Integer x : items) bst.insert(x);
                    double insertMs = (System.nanoTime()-t)/1_000_000.0;
                    t = System.nanoTime();
                    bst.contains(items[RANDOM.nextInt(n)]);
                    double searchMs = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("BST,%d,%d,%.4f,%.4f,%d%n", n, r+1, insertMs, searchMs, bst.height()));

                    RedBlackTree<Integer> rbt = new RedBlackTree<>();
                    t = System.nanoTime();
                    for (Integer x : items) rbt.insert(x);
                    insertMs = (System.nanoTime()-t)/1_000_000.0;
                    t = System.nanoTime();
                    rbt.contains(items[RANDOM.nextInt(n)]);
                    searchMs = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("RedBlackTree,%d,%d,%.4f,%.4f,%d%n", n, r+1, insertMs, searchMs, rbt.height()));
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
        try (FileWriter fw = new FileWriter("data/graph_experiment.csv")) {
            fw.write("algorithm,vertices,edges,run,time_ms\n");
            for (int v : GRAPH_SIZES) {
                Graph g = generateRandomGraph(v, v * 3);
                for (int r = 0; r < RUNS; r++) {
                    long t; double ms;

                    t = System.nanoTime(); com.g15.dsa.algorithms.graph.BFS.traverse(g, 0); ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("BFS,%d,%d,%d,%.4f%n", v, v*3, r+1, ms));

                    t = System.nanoTime(); com.g15.dsa.algorithms.graph.DFS.traverse(g, 0); ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("DFS,%d,%d,%d,%.4f%n", v, v*3, r+1, ms));

                    t = System.nanoTime(); com.g15.dsa.algorithms.graph.Dijkstra.shortestPaths(g, 0); ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("Dijkstra,%d,%d,%d,%.4f%n", v, v*3, r+1, ms));

                    t = System.nanoTime(); com.g15.dsa.algorithms.graph.Prim.minimumSpanningTree(g, 0); ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("Prim,%d,%d,%d,%.4f%n", v, v*3, r+1, ms));

                    t = System.nanoTime(); com.g15.dsa.algorithms.graph.Kruskal.minimumSpanningTree(g); ms = (System.nanoTime()-t)/1_000_000.0;
                    fw.write(String.format("Kruskal,%d,%d,%d,%.4f%n", v, v*3, r+1, ms));
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
