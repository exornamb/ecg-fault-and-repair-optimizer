package algorithms.structures;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class PriorityQueue<T> {

    private Object[] heap;
    private int size;
    private final Comparator<T> comparator;

    private static final int DEFAULT_CAPACITY = 10;

    public PriorityQueue(Comparator<T> comparator) {

        if (comparator == null) {
            throw new IllegalArgumentException(
                    "Comparator cannot be null"
            );
        }

        this.comparator = comparator;
        this.heap = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public void enqueue(T value) {

        ensureCapacity();

        heap[size] = value;

        siftUp(size);

        size++;
    }

    public T dequeue() {

        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Priority queue is empty"
            );
        }

        T highestPriority =
                elementAt(0);

        size--;

        heap[0] = heap[size];
        heap[size] = null;

        if (size > 0) {
            siftDown(0);
        }

        return highestPriority;
    }

    public T peek() {

        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Priority queue is empty"
            );
        }

        return elementAt(0);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {

        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }

        size = 0;
    }

    private void siftUp(int index) {

        while (index > 0) {

            int parent =
                    (index - 1) / 2;

            T current =
                    elementAt(index);

            T parentValue =
                    elementAt(parent);

            if (comparator.compare(
                    current,
                    parentValue
            ) <= 0) {
                break;
            }

            swap(index, parent);

            index = parent;
        }
    }

    private void siftDown(int index) {

        while (true) {

            int left =
                    2 * index + 1;

            int right =
                    2 * index + 2;

            int highest = index;

            if (left < size
                    && comparator.compare(
                    elementAt(left),
                    elementAt(highest)
            ) > 0) {

                highest = left;
            }

            if (right < size
                    && comparator.compare(
                    elementAt(right),
                    elementAt(highest)
            ) > 0) {

                highest = right;
            }

            if (highest == index) {
                break;
            }

            swap(index, highest);

            index = highest;
        }
    }

    private void swap(
            int first,
            int second) {

        Object temp =
                heap[first];

        heap[first] =
                heap[second];

        heap[second] =
                temp;
    }

    private void ensureCapacity() {

        if (size < heap.length) {
            return;
        }

        Object[] newHeap =
                new Object[heap.length * 2];

        System.arraycopy(
                heap,
                0,
                newHeap,
                0,
                size
        );

        heap = newHeap;
    }

    @SuppressWarnings("unchecked")
    private T elementAt(int index) {
        return (T) heap[index];
    }
}