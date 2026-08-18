package com.g15.dsa.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    private DynamicArray<Integer> arr;

    @BeforeEach
    void setUp() { arr = new DynamicArray<>(); }

    // === NORMAL CASES ===
    @Test void addAndGet() { arr.add(10); assertEquals(10, arr.get(0)); }
    @Test void addMultiple() { for (int i=1;i<=5;i++) arr.add(i); assertEquals(5, arr.size()); }
    @Test void setReplaces() { arr.add(1); arr.set(0,99); assertEquals(99, arr.get(0)); }
    @Test void removeByIndex() { arr.add(1); arr.add(2); arr.remove(0); assertEquals(2, arr.get(0)); }
    @Test void removeByValue() { arr.add(42); assertTrue(arr.remove(Integer.valueOf(42))); }
    @Test void contains() { arr.add(7); assertTrue(arr.contains(7)); assertFalse(arr.contains(99)); }
    @Test void indexOf() { arr.add(5); arr.add(10); assertEquals(1, arr.indexOf(10)); }
    @Test void autoGrows() { for (int i=0;i<25;i++) arr.add(i); assertEquals(25, arr.size()); assertTrue(arr.capacity() >= 25); }
    @Test void insertAtIndex() { arr.add(1); arr.add(3); arr.add(1,2); assertEquals(2, arr.get(1)); assertEquals(3, arr.get(2)); }

    // === BOUNDARY CASES ===
    @Test void emptySize() { assertEquals(0, arr.size()); }
    @Test void singleElement() { arr.add(100); assertEquals(1, arr.size()); assertEquals(100, arr.get(0)); }
    @Test void clearResetsSize() { arr.add(1); arr.add(2); arr.clear(); assertEquals(0, arr.size()); }
    @Test void removeFromEnd() { arr.add(1); arr.add(2); arr.remove(1); assertEquals(1, arr.size()); }

    // === INVALID INPUT CASES ===
    @Test void getNegativeIndex() { arr.add(1); assertThrows(IndexOutOfBoundsException.class, () -> arr.get(-1)); }
    @Test void getOutOfBound() { arr.add(1); assertThrows(IndexOutOfBoundsException.class, () -> arr.get(5)); }
    @Test void removeOutOfBound() { arr.add(1); assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(2)); }
    @Test void negativeCapacity() { assertThrows(IllegalArgumentException.class, () -> new DynamicArray<>(-1)); }
}
