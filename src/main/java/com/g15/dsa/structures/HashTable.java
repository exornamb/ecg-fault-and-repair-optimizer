package com.g15.dsa.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HashTable<K, V> {

    private Entry<K, V>[] table;
    private int size;
    private int collisionCount;

    private static final int DEFAULT_CAPACITY = 16;
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
        return Math.abs(key.hashCode()) % table.length;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] oldTable = table;
        table = new Entry[oldTable.length * 2];
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
}