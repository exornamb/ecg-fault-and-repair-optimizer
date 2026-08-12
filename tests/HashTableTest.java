package structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {

    // ---------- NORMAL CASES ----------

    @Test
    void testPutAndGet_returnsCorrectValue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Accra", 1);
        assertEquals(1, table.get("Accra"));
    }

    @Test
    void testPut_multipleKeys_allRetrievable() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Accra", 1);
        table.put("Kumasi", 2);
        table.put("Tema", 3);

        assertEquals(1, table.get("Accra"));
        assertEquals(2, table.get("Kumasi"));
        assertEquals(3, table.get("Tema"));
        assertEquals(3, table.size());
    }

    @Test
    void testContainsKey_existingKey_returnsTrue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Accra", 1);
        assertTrue(table.containsKey("Accra"));
    }

    @Test
    void testRemove_existingKey_removesAndReturnsValue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Accra", 1);

        Integer removed = table.remove("Accra");

        assertEquals(1, removed);
        assertFalse(table.containsKey("Accra"));
        assertEquals(0, table.size());
    }

    @Test
    void testPut_sameKeyTwice_overwritesValue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Accra", 1);
        table.put("Accra", 2);

        assertEquals(2, table.get("Accra"));
        assertEquals(1, table.size()); // still only 1 entry, not 2
    }

    @Test
    void testClear_removesAllEntries() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Accra", 1);
        table.put("Kumasi", 2);

        table.clear();

        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
        assertNull(table.get("Accra"));
    }

    // ---------- BOUNDARY CASES ----------

    @Test
    void testNewTable_isEmpty() {
        HashTable<String, Integer> table = new HashTable<>();
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
    }

    @Test
    void testGet_onEmptyTable_returnsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.get("nothingHere"));
    }

    @Test
    void testSingleEntry_sizeAndIsEmptyCorrect() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("onlyOne", 100);

        assertEquals(1, table.size());
        assertFalse(table.isEmpty());
    }

    @Test
    void testResize_triggeredByManyInserts_stillFindsAllKeys() {
        // Default capacity is 16, load factor 0.75 -> resize kicks in after ~12 entries.
        // Insert enough entries to force at least one resize and confirm nothing gets lost.
        HashTable<Integer, String> table = new HashTable<>();

        for (int i = 0; i < 50; i++) {
            table.put(i, "value" + i);
        }

        assertEquals(50, table.size());
        for (int i = 0; i < 50; i++) {
            assertEquals("value" + i, table.get(i));
        }
    }

    // ---------- INVALID / EDGE CASES ----------

    @Test
    void testGet_missingKey_returnsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Accra", 1);

        assertNull(table.get("NotInTable"));
    }

    @Test
    void testContainsKey_missingKey_returnsFalse() {
        HashTable<String, Integer> table = new HashTable<>();
        assertFalse(table.containsKey("ghost"));
    }

    @Test
    void testRemove_missingKey_returnsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("Accra", 1);

        assertNull(table.remove("NotInTable"));
        assertEquals(1, table.size()); // untouched
    }

    @Test
    void testPut_nullKey_doesNotCrash() {
        HashTable<String, Integer> table = new HashTable<>();
        // indexFor() explicitly handles key == null by hashing to 0, so this should work.
        table.put(null, 99);
        assertEquals(99, table.get(null));
    }

    @Test
    void testRemove_onEmptyTable_returnsNullNoCrash() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.remove("anything"));
    }
}