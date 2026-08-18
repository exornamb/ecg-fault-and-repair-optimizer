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

    @Test
    void hashTableUsesTeamParametersByDefault() {
        com.g15.dsa.structures.HashTable<String, String> table = new com.g15.dsa.structures.HashTable<>();
        assertEquals(TeamParameters.HASH_CAPACITY, table.capacity());
        assertEquals(103, table.capacity());
    }

    @Test
    void dispatchScoringIncorporatesParameters() {
        double dist = 10.0;
        int urgency = 5;
        // Score = (5 * 1.4) / (10 * 1.2) = 7.0 / 12.0 = 0.5833
        double score = com.g15.dsa.controller.DispatchController.calculateDispatchScore(dist, urgency);
        assertEquals(7.0 / 12.0, score, 0.001);

        double penalized = com.g15.dsa.controller.DispatchController.calculatePenalizedDistance(dist);
        assertEquals(12.0, penalized, 0.001);
    }

    @Test
    void faultModelUsesUrgencyWeight() {
        com.g15.dsa.model.Fault fault = new com.g15.dsa.model.Fault(1, "SR-01", "Legon", "Feeder", 5, "Alpha", "OPEN");
        assertEquals(5 * 1.4, fault.getWeightedUrgency(), 0.0001);
        assertEquals(7.0, fault.getWeightedUrgency(), 0.0001);
    }
}
