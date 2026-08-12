package structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DisjointSetTest {

    // ---------- NORMAL CASES ----------

    @Test
    void testInitial_everyoneIsOwnCaptain() {
        DisjointSet ds = new DisjointSet(5);

        for (int i = 0; i < 5; i++) {
            assertEquals(i, ds.find(i));
        }
    }

    @Test
    void testUnion_joinsTwoElements_theyBecomeConnected() {
        DisjointSet ds = new DisjointSet(5);
        ds.union(0, 1);

        assertTrue(ds.connected(0, 1));
    }

    @Test
    void testUnion_unrelatedElements_remainDisconnected() {
        DisjointSet ds = new DisjointSet(5);
        ds.union(0, 1);

        assertFalse(ds.connected(0, 2));
    }

    @Test
    void testUnion_chainOfUnions_allEndUpConnected() {
        // 0-1, 1-2, 2-3 should all end up in one team, even though
        // 0 and 3 were never unioned directly.
        DisjointSet ds = new DisjointSet(5);
        ds.union(0, 1);
        ds.union(1, 2);
        ds.union(2, 3);

        assertTrue(ds.connected(0, 3));
        assertFalse(ds.connected(0, 4));
    }

    @Test
    void testUnion_sameElementTwice_doesNothingBad() {
        DisjointSet ds = new DisjointSet(5);
        ds.union(0, 0);

        assertTrue(ds.connected(0, 0));
    }

    @Test
    void testSize_returnsElementCount() {
        DisjointSet ds = new DisjointSet(7);
        assertEquals(7, ds.size());
    }

    // ---------- BOUNDARY CASES ----------

    @Test
    void testConstructor_sizeOne_singleElementConnectedToItself() {
        DisjointSet ds = new DisjointSet(1);

        assertTrue(ds.connected(0, 0));
        assertEquals(1, ds.size());
    }

    @Test
    void testFind_firstAndLastValidIndex_noCrash() {
        DisjointSet ds = new DisjointSet(10);

        assertEquals(0, ds.find(0));
        assertEquals(9, ds.find(9));
    }

    @Test
    void testUnion_allElementsIntoOneTeam() {
        DisjointSet ds = new DisjointSet(5);
        ds.union(0, 1);
        ds.union(2, 3);
        ds.union(1, 3);
        ds.union(3, 4);

        // everyone should now be connected to everyone
        for (int i = 0; i < 5; i++) {
            assertTrue(ds.connected(0, i));
        }
    }

    // ---------- INVALID / EDGE CASES ----------

    @Test
    void testConstructor_zeroSize_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DisjointSet(0)
        );
    }

    @Test
    void testConstructor_negativeSize_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DisjointSet(-3)
        );
    }

    @Test
    void testFind_valueTooLarge_throwsException() {
        DisjointSet ds = new DisjointSet(5);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> ds.find(5) // valid range is 0-4
        );
    }

    @Test
    void testFind_negativeValue_throwsException() {
        DisjointSet ds = new DisjointSet(5);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> ds.find(-1)
        );
    }

    @Test
    void testUnion_outOfRangeValue_throwsException() {
        DisjointSet ds = new DisjointSet(5);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> ds.union(0, 10)
        );
    }

    @Test
    void testConnected_outOfRangeValue_throwsException() {
        DisjointSet ds = new DisjointSet(5);

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> ds.connected(0, 100)
        );
    }
}