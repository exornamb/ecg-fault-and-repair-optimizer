package main.java.algorithms.structures;

public class CustomSet<T> {

    private final HashTable<T, Boolean> table;

    public CustomSet() {
        table = new HashTable<>();
    }

    public void add(T value) {

        if (value == null) {
            throw new IllegalArgumentException(
                    "Cannot add null"
            );
        }

        table.put(value, true);
    }

    public boolean contains(T value) {
        return table.containsKey(value);
    }

    public boolean remove(T value) {
        return table.remove(value) != null;
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