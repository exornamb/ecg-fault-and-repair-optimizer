package structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomMapTest {

    // ---------- NORMAL CASES ----------

    @Test
    void testPutAndGet_returnsCorrectValue() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("Accra", 1);

        assertEquals(1, map.get("Accra"));
    }

    @Test
    void testPut_multipleKeys_allRetrievable() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("Accra", 1);
        map.put("Kumasi", 2);

        assertEquals(1, map.get("Accra"));
        assertEquals(2, map.get("Kumasi"));
        assertEquals(2, map.size());
    }

    @Test
    void testContainsKey_existingKey_returnsTrue() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("Accra", 1);

        assertTrue(map.containsKey("Accra"));
    }

    @Test
    void testRemove_existingKey_removesAndReturnsValue() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("Accra", 1);

        Integer removed = map.remove("Accra");

        assertEquals(1, removed);
        assertFalse(map.containsKey("Accra"));
    }

    @Test
    void testPut_sameKeyTwice_overwritesValue() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("Accra", 1);
        map.put("Accra", 2);

        assertEquals(2, map.get("Accra"));
        assertEquals(1, map.size());
    }

    @Test
    void testClear_emptiesTheMap() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("Accra", 1);
        map.put("Kumasi", 2);

        map.clear();

        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    // ---------- BOUNDARY CASES ----------

    @Test
    void testNewMap_isEmpty() {
        CustomMap<String, Integer> map = new CustomMap<>();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    @Test
    void testGet_onEmptyMap_returnsNull() {
        CustomMap<String, Integer> map = new CustomMap<>();
        assertNull(map.get("nothingHere"));
    }

    @Test
    void testSingleEntry_sizeAndIsEmptyCorrect() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("onlyOne", 100);

        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
    }

    // ---------- INVALID / EDGE CASES ----------

    @Test
    void testPut_nullKey_throwsException() {
        CustomMap<String, Integer> map = new CustomMap<>();

        assertThrows(
                IllegalArgumentException.class,
                () -> map.put(null, 1)
        );
    }

    @Test
    void testGet_missingKey_returnsNull() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("Accra", 1);

        assertNull(map.get("NotInMap"));
    }

    @Test
    void testContainsKey_missingKey_returnsFalse() {
        CustomMap<String, Integer> map = new CustomMap<>();
        assertFalse(map.containsKey("ghost"));
    }

    @Test
    void testRemove_missingKey_returnsNull() {
        CustomMap<String, Integer> map = new CustomMap<>();
        map.put("Accra", 1);

        assertNull(map.remove("NotInMap"));
        assertEquals(1, map.size()); // untouched
    }

    @Test
    void testRemove_onEmptyMap_returnsNullNoCrash() {
        CustomMap<String, Integer> map = new CustomMap<>();
        assertNull(map.remove("anything"));
    }
}
