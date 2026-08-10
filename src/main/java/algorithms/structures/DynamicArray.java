package algorithms.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DynamicArray<T> implements Iterable<T> {

    private Object[] data;
    private int size;

    private static final int DEFAULT_CAPACITY = 10;

    public DynamicArray() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException(
                    "Initial capacity must be greater than 0"
            );
        }

        data = new Object[initialCapacity];
        size = 0;
    }

    public void add(T value) {

        if (size == data.length) {
            resize();
        }

        data[size] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    @SuppressWarnings("unchecked")
    public T set(int index, T value) {
        checkIndex(index);

        T oldValue = (T) data[index];

        data[index] = value;

        return oldValue;
    }

    public T remove(int index) {

        checkIndex(index);

        T removed = (T) data[index];

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        data[size - 1] = null;

        size--;

        return removed;
    }

    public boolean contains(T value) {

        for (int i = 0; i < size; i++) {

            if (value == null
                    ? data[i] == null
                    : value.equals(data[i])) {

                return true;
            }
        }

        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {

        for (int i = 0; i < size; i++) {
            data[i] = null;
        }

        size = 0;
    }

    private void resize() {

        Object[] newData =
                new Object[data.length * 2];

        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }

        data = newData;
    }

    private void checkIndex(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index +
                            ", Size: " + size
            );
        }
    }

    @Override
    public Iterator<T> iterator() {

        return new Iterator<>() {

            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public T next() {

                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                return get(currentIndex++);
            }
        };
    }
}