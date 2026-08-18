package com.g15.dsa.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TeamParametersTest {

    @Test
    void studentIndexIsCorrect() {
        assertEquals("22396802", TeamParameters.STUDENT_INDEX);
    }

    @Test
    void studentNameMatches() {
        assertTrue(TeamParameters.STUDENT_NAME.contains("Michelle"));
    }

    @Test
    void urgencyWeightMatchesDerivation() {
        // Digit sum of 22396802: 2+2+3+9+6+8+0+2 = 32. 32 % 5 = 2. 1.0 + 2*0.2 = 1.4
        assertEquals(1.4, TeamParameters.URGENCY_WEIGHT, 0.0001);
    }

    @Test
    void roadPenaltyMatchesDerivation() {
        // Last 2 digits: 02. 02 % 10 = 2. 1.0 + 2*0.1 = 1.2
        assertEquals(1.2, TeamParameters.ROAD_PENALTY, 0.0001);
    }

    @Test
    void hashCapacityIsNextPrime() {
        // 22396802 % 50 = 2. 100 + 2 = 102. Next prime is 103.
        assertEquals(103, TeamParameters.HASH_CAPACITY);
    }

    @Test
    void hashSeedIsLastFourDigits() {
        // Last 4 digits: 6802
        assertEquals(6802, TeamParameters.HASH_SEED);
    }
}
