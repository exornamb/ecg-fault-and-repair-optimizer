package com.g15.dsa.structures;

import com.g15.dsa.database.TeamParameters;

import java.util.HashMap;
import java.util.Map;

public class HashCollisionAnalysis {

    private static final double LOAD_FACTOR = 0.75;

    public static void main(String[] args) {

        // Test at 3 different fill levels
        int[] testSizes = { 100, 1000, 20000 };

        for (int n : testSizes) {
            runTrial(n);
        }
    }

    private static void runTrial(int numberOfKeys) {

        int capacity = TeamParameters.HASH_CAPACITY; // Prime initial capacity (103)
        int size = 0;
        int collisions = 0;

        // Tracks how many keys currently sit in each bucket index.
        Map<Integer, Integer> bucketCounts = new HashMap<>();

        for (int i = 0; i < numberOfKeys; i++) {

            String key = "Location" + i;

            // Resize check - same trigger condition as HashTable.put()
            if ((double) (size + 1) / capacity > LOAD_FACTOR) {
                capacity = HashTable.nextPrime(capacity * 2);
                collisions = 0;
                bucketCounts.clear();

                // Recompute every existing key's bucket under the new capacity
                for (int j = 0; j < size; j++) {
                    int idx = indexFor("Location" + j, capacity);
                    int count = bucketCounts.getOrDefault(idx, 0);
                    if (count > 0) {
                        collisions++;
                    }
                    bucketCounts.put(idx, count + 1);
                }
            }

            int index = indexFor(key, capacity);
            int countAtBucket = bucketCounts.getOrDefault(index, 0);

            if (countAtBucket > 0) {
                collisions++;
            }

            bucketCounts.put(index, countAtBucket + 1);
            size++;
        }

        double loadFactor = (double) size / capacity;

        System.out.println("---- " + numberOfKeys + " keys inserted ----");
        System.out.println("Final table capacity: " + capacity + (HashTable.isPrime(capacity) ? " (Prime)" : ""));
        System.out.println("Final load factor: " + String.format("%.2f", loadFactor));
        System.out.println("Total collisions: " + collisions);
        System.out.println();
    }

    /**
     * Same spreading formula as HashTable.indexFor() - incorporating TeamParameters.HASH_SEED (6802)
     * so this script's numbers match the real HashTable behavior exactly.
     */
    private static int indexFor(String key, int capacity) {
        int hash = key.hashCode() ^ TeamParameters.HASH_SEED;
        hash ^= (hash >>> 16);
        return (hash & 0x7fffffff) % capacity;
    }
}