package com.g15.dsa.database;

import com.g15.dsa.dao.FaultDAO;
import com.g15.dsa.model.Fault;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class DatabaseTest {

    @Test
    void faultDaoFallbackLoadsCsv() {
        FaultDAO dao = new FaultDAO();
        List<Fault> faults = dao.getAllFaults();
        assertNotNull(faults);
        // Even when PostgreSQL is offline, CSV fallback loads the seed records
        assertFalse(faults.isEmpty(), "FaultDAO should load records from DB or CSV fallback");
    }

    @Test
    void faultPropertiesLoadedCorrectly() {
        FaultDAO dao = new FaultDAO();
        List<Fault> faults = dao.getAllFaults();
        if (!faults.isEmpty()) {
            Fault first = faults.get(0);
            assertNotNull(first.getFaultId());
            assertNotNull(first.getArea());
            assertTrue(first.getUrgency() >= 1 && first.getUrgency() <= 5);
        }
    }
}
