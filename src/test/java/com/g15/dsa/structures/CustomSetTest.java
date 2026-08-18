package com.g15.dsa.structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomSetTest {

    // ---------- NORMAL CASES ----------

    @Test
    void testAdd_singleValue_containsReturnsTrue() {
        CustomSet<String> set = new CustomSet<>();
        set.add("Accra");

        assertTrue(set.contains("Accra"));
        assertEquals(1, set.size());
    }

    @Test
    void testAdd_multipleDistinctValues_allPresent() {
        CustomSet<String> set = new CustomSet<>();
        set.add("Accra");
        set.add("Kumasi");
        set.add("Tema");

        assertEquals(3, set.size());
        assertTrue(set.contains("Kumasi"));
    }

    @Test
    void testRemove_existingValue_removesIt() {
        CustomSet<String> set = new CustomSet<>();
        set.add("Accra");

        boolean removed = set.remove("Accra");

        assertTrue(removed);
        assertFalse(set.contains("Accra"));
        assertEquals(0, set.size());
    }

    @Test
    void testClear_emptiesTheSet() {
        CustomSet<String> set = new CustomSet<>();
        set.add("Accra");
        set.add("Kumasi");

        set.clear();

        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    // ---------- BOUNDARY CASES ----------

    @Test
    void testNewSet_isEmpty() {
        CustomSet<String> set = new CustomSet<>();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    void testAdd_duplicateValue_doesNotIncreaseSize() {
        // This is the whole point of a Set: no duplicates allowed.
        CustomSet<String> set = new CustomSet<>();
        set.add("Accra");
        set.add("Accra");
        set.add("Accra");

        assertEquals(1, set.size());
    }

    @Test
    void testAdd_singleValue_isNotEmpty() {
        CustomSet<String> set = new CustomSet<>();
        set.add("onlyOne");

        assertFalse(set.isEmpty());
    }

    // ---------- INVALID / EDGE CASES ----------

    @Test
    void testAdd_nullValue_throwsException() {
        CustomSet<String> set = new CustomSet<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> set.add(null)
        );
    }

    @Test
    void testContains_missingValue_returnsFalse() {
        CustomSet<String> set = new CustomSet<>();
        set.add("Accra");

        assertFalse(set.contains("NotInSet"));
    }

    @Test
    void testRemove_missingValue_returnsFalse() {
        CustomSet<String> set = new CustomSet<>();
        set.add("Accra");

        boolean removed = set.remove("NotInSet");

        assertFalse(removed);
        assertEquals(1, set.size()); // untouched
    }

    @Test
    void testRemove_onEmptySet_returnsFalseNoCrash() {
        CustomSet<String> set = new CustomSet<>();
        assertFalse(set.remove("anything"));
    }

    @Test
    void testContains_onEmptySet_returnsFalse() {
        CustomSet<String> set = new CustomSet<>();
        assertFalse(set.contains("anything"));
    }
}