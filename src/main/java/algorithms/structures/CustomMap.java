package main.java.algorithms.structures;

public class CustomMap<K, V> {

    private final HashTable<K, V> table;

    public CustomMap() {
        table = new HashTable<>();
    }

    public void put(K key, V value) {

        if (key == null) {
            throw new IllegalArgumentException(
                    "Key cannot be null"
            );
        }

        table.put(key, value);
    }

    public V get(K key) {
        return table.get(key);
    }

    public V remove(K key) {
        return table.remove(key);
    }

    public boolean containsKey(K key) {
        return table.containsKey(key);
    }

    public int size() {
        return table.size();
    }

    public boolean isEmpty() {
        return table.isEmpty();
    }

    public void clear() {
        table.clear();
    }
}