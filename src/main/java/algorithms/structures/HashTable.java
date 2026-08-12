package main.java.algorithms.structures;

public class HashTable<K, V> {

    private Entry<K, V>[] table;
    private int size;

    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    public HashTable() {
        table = new Entry[DEFAULT_CAPACITY];
    }

    public void put(K key, V value) {

        if ((double) (size + 1) / table.length
                > LOAD_FACTOR) {

            resize();
        }

        int index = indexFor(key);

        Entry<K, V> current = table[index];

        while (current != null) {

            if (keysEqual(current.key, key)) {

                current.value = value;
                return;
            }

            current = current.next;
        }

        Entry<K, V> newEntry =
                new Entry<>(key, value);

        newEntry.next = table[index];

        table[index] = newEntry;

        size++;
    }

    public V get(K key) {

        int index = indexFor(key);

        Entry<K, V> current =
                table[index];

        while (current != null) {

            if (keysEqual(current.key, key)) {
                return current.value;
            }

            current = current.next;
        }

        return null;
    }

    public boolean containsKey(K key) {

        return getEntry(key) != null;
    }

    public V remove(K key) {

        int index = indexFor(key);

        Entry<K, V> current =
                table[index];

        Entry<K, V> previous = null;

        while (current != null) {

            if (keysEqual(current.key, key)) {

                if (previous == null) {
                    table[index] = current.next;
                } else {
                    previous.next = current.next;
                }

                size--;

                return current.value;
            }

            previous = current;
            current = current.next;
        }

        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {

        table = new Entry[DEFAULT_CAPACITY];

        size = 0;
    }

    private Entry<K, V> getEntry(K key) {

        int index = indexFor(key);

        Entry<K, V> current =
                table[index];

        while (current != null) {

            if (keysEqual(current.key, key)) {
                return current;
            }

            current = current.next;
        }

        return null;
    }

    private int indexFor(K key) {

        int hash =
                key == null
                        ? 0
                        : key.hashCode();

        hash ^= (hash >>> 16);

        return (hash & 0x7fffffff)
                % table.length;
    }

    private boolean keysEqual(
            K first,
            K second) {

        if (first == null) {
            return second == null;
        }

        return first.equals(second);
    }

    private void resize() {

        Entry<K, V>[] oldTable =
                table;

        table =
                new Entry[oldTable.length * 2];

        size = 0;

        for (Entry<K, V> bucket : oldTable) {

            Entry<K, V> current = bucket;

            while (current != null) {

                put(
                        current.key,
                        current.value
                );

                current = current.next;
            }
        }
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
}