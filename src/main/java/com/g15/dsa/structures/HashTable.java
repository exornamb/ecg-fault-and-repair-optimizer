package com.g15.dsa.structures;

import com.g15.dsa.database.TeamParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HashTable<K, V> {

    private Entry<K, V>[] table;
    private int size;
    private int collisionCount;

    // Derived from Michelle's index (22396802): next_prime(100 + (22396802 % 50)) = 103
    public static final int DEFAULT_CAPACITY = TeamParameters.HASH_CAPACITY; // 103
    public static final int HASH_SEED = TeamParameters.HASH_SEED;           // 6802
    private static final double LOAD_FACTOR = 0.75;

    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        table = new Entry[initialCapacity];
        size = 0;
        collisionCount = 0;
    }

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public void put(K key, V value) {
        if ((double) (size + 1) / table.length > LOAD_FACTOR) {
            resize();
        }

        int index = indexFor(key);
        Entry<K, V> head = table[index];

        if (head == null) {
            table[index] = new Entry<>(key, value);
            size++;
            return;
        }

        collisionCount++;
        Entry<K, V> curr = head;
        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                curr.value = value;
                return;
            }
            if (curr.next == null) break;
            curr = curr.next;
        }

        curr.next = new Entry<>(key, value);
        size++;
    }

    public V get(K key) {
        int index = indexFor(key);
        Entry<K, V> curr = table[index];

        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                return curr.value;
            }
            curr = curr.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V remove(K key) {
        int index = indexFor(key);
        Entry<K, V> curr = table[index];
        Entry<K, V> prev = null;

        while (curr != null) {
            if (Objects.equals(curr.key, key)) {
                if (prev == null) {
                    table[index] = curr.next;
                } else {
                    prev.next = curr.next;
                }
                size--;
                return curr.value;
            }
            prev = curr;
            curr = curr.next;
        }
        return null;
    }

    public List<K> keys() {
        List<K> keyList = new ArrayList<>();
        for (Entry<K, V> head : table) {
            Entry<K, V> curr = head;
            while (curr != null) {
                keyList.add(curr.key);
                curr = curr.next;
            }
        }
        return keyList;
    }

    public List<V> values() {
        List<V> valList = new ArrayList<>();
        for (Entry<K, V> head : table) {
            Entry<K, V> curr = head;
            while (curr != null) {
                valList.add(curr.value);
                curr = curr.next;
            }
        }
        return valList;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return table.length;
    }

    public int getCollisionCount() {
        return collisionCount;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        table = new Entry[DEFAULT_CAPACITY];
        size = 0;
        collisionCount = 0;
    }

    private int indexFor(K key) {
        if (key == null) return 0;
        // Hash computation using Michelle's index-derived HASH_SEED (6802)
        int h = key.hashCode() ^ HASH_SEED;
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldTable = table;
        // Resize to the next prime number after doubling to maintain prime capacity
        int targetCapacity = oldTable.length * 2;
        int newCapacity = nextPrime(targetCapacity);
        table = new Entry[newCapacity];
        size = 0;
        collisionCount = 0;

        for (Entry<K, V> head : oldTable) {
            Entry<K, V> curr = head;
            while (curr != null) {
                put(curr.key, curr.value);
                curr = curr.next;
            }
        }
    }

    /**
     * Finds the next prime number greater than or equal to n.
     */
    public static int nextPrime(int n) {
        if (n <= 2) return 2;
        int prime = (n % 2 == 0) ? n + 1 : n;
        while (!isPrime(prime)) {
            prime += 2;
        }
        return prime;
    }

    /**
     * Checks if a number is prime.
     */
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; (long) i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }
}