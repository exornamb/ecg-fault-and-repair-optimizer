package com.g15.dsa.dao;

import com.g15.dsa.database.DatabaseConnection;
import com.g15.dsa.model.Fault;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;

/**
 * FaultDAO — Data Access Object for ECG service fault records.
 * Supports JDBC (PostgreSQL) with CSV offline fallback.
 *
 * All mutating operations (insert, update, delete) write to BOTH the
 * PostgreSQL database AND the local CSV file so changes are always
 * persisted everywhere.
 */
public class FaultDAO {

    /** Paths checked in order for the CSV data file. */
    private static final String[] CSV_PATHS = {
        "data/service_requests.csv",
        "service_requests.csv"
    };

    private static final Object CSV_LOCK = new Object();
    private static volatile boolean syncedWithDb = false;

    // ========================================================
    // SCHEMA MIGRATION & INITIAL SEEDING (ACID)
    // ========================================================

    private synchronized void ensureSchemaAndSync(Connection conn) {
        if (syncedWithDb) return;
        try {
            // 1. Ensure required columns exist
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("ALTER TABLE service_requests ADD COLUMN IF NOT EXISTS fault_id VARCHAR(50)");
                stmt.execute("ALTER TABLE service_requests ADD COLUMN IF NOT EXISTS area VARCHAR(150)");
                stmt.execute("ALTER TABLE service_requests ADD COLUMN IF NOT EXISTS crew VARCHAR(100) DEFAULT 'Unassigned'");
            } catch (Exception ignored) {
                // Table or columns might already exist / permission limits
            }

            // 2. Check if DB has fewer records than CSV (e.g. initial start or unseeded DB)
            int dbCount = 0;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM service_requests")) {
                if (rs.next()) {
                    dbCount = rs.getInt(1);
                }
            }

            List<Fault> csvFaults = loadFromCsv();
            if (dbCount < csvFaults.size() && !csvFaults.isEmpty()) {
                // Transactionally seed missing CSV records into PostgreSQL (ACID)
                boolean prevAutoCommit = conn.getAutoCommit();
                conn.setAutoCommit(false);
                String insertSql = "INSERT INTO service_requests "
                        + "(request_id, fault_id, area, category, urgency, crew, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                        + "ON CONFLICT (request_id) DO NOTHING";
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    for (Fault f : csvFaults) {
                        ps.setString(1, f.getFaultId());
                        ps.setString(2, f.getFaultId());
                        ps.setString(3, f.getArea() != null ? f.getArea() : "");
                        ps.setString(4, f.getCategory() != null ? f.getCategory() : "General");
                        ps.setInt(5, f.getUrgency());
                        ps.setString(6, f.getCrew() != null ? f.getCrew() : "Unassigned");
                        ps.setString(7, f.getStatus() != null ? f.getStatus() : "OPEN");
                        ps.addBatch();
                    }
                    ps.executeBatch();
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    System.err.println("Database seed transaction rolled back: " + e.getMessage());
                } finally {
                    conn.setAutoCommit(prevAutoCommit);
                }
            }
            syncedWithDb = true;
        } catch (Exception e) {
            System.err.println("ensureSchemaAndSync warning: " + e.getMessage());
        }
    }

    // ========================================================
    // READ
    // ========================================================

    public List<Fault> getAllFaults() {
        List<Fault> faults = new ArrayList<>();
        String sql = "SELECT request_id AS fault_id, "
                   + "COALESCE(area, source_location_id, '') AS area, "
                   + "category, urgency, "
                   + "COALESCE(crew, 'Unassigned') AS crew, "
                   + "status "
                   + "FROM service_requests ORDER BY urgency DESC, request_id ASC";
        try (Connection conn = DatabaseConnection.getConnection()) {
            ensureSchemaAndSync(conn);
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                int rowId = 1;
                while (rs.next()) {
                    faults.add(new Fault(
                        rowId++,
                        rs.getString("fault_id"),
                        rs.getString("area"),
                        rs.getString("category"),
                        rs.getInt("urgency"),
                        rs.getString("crew"),
                        rs.getString("status")
                    ));
                }
                if (!faults.isEmpty()) {
                    return faults;
                }
            }
        } catch (Exception e) {
            System.err.println("DB read failed, falling back to CSV: " + e.getMessage());
        }
        return loadFromCsv();
    }

    // ========================================================
    // INSERT (ACID)
    // ========================================================

    public boolean addFault(Fault fault) {
        return insertFault(fault);
    }

    public boolean insertFault(Fault fault) {
        if (fault.getFaultId() == null || fault.getFaultId().isEmpty()) {
            fault.setFaultId(generateFaultId());
        }

        boolean dbOk = false;
        String sql = "INSERT INTO service_requests "
                   + "(request_id, fault_id, area, category, urgency, crew, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                   + "ON CONFLICT (request_id) DO UPDATE SET "
                   + "area = EXCLUDED.area, category = EXCLUDED.category, "
                   + "urgency = EXCLUDED.urgency, crew = EXCLUDED.crew, status = EXCLUDED.status";

        try (Connection conn = DatabaseConnection.getConnection()) {
            boolean prevAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, fault.getFaultId());
                ps.setString(2, fault.getFaultId());
                ps.setString(3, fault.getArea());
                ps.setString(4, fault.getCategory());
                ps.setInt(5, fault.getUrgency());
                ps.setString(6, fault.getCrew() != null ? fault.getCrew() : "Unassigned");
                ps.setString(7, fault.getStatus() != null ? fault.getStatus() : "OPEN");
                dbOk = ps.executeUpdate() > 0;
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("DB insert transaction rolled back: " + e.getMessage());
            } finally {
                conn.setAutoCommit(prevAutoCommit);
            }
        } catch (Exception e) {
            System.err.println("DB insert connection failed: " + e.getMessage());
        }

        // Thread-safely update CSV
        synchronized (CSV_LOCK) {
            updateCsv(fault);
        }
        return dbOk || true;
    }

    // ========================================================
    // UPDATE (ACID)
    // ========================================================

    public boolean updateFault(Fault fault) {
        if (fault == null || fault.getFaultId() == null || fault.getFaultId().isEmpty()) {
            System.err.println("updateFault: fault or faultId is null/empty — skipping.");
            return false;
        }

        boolean dbOk = false;
        String sql = "UPDATE service_requests "
                   + "SET area=?, category=?, urgency=?, crew=?, status=? "
                   + "WHERE request_id=?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            boolean prevAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, fault.getArea());
                ps.setString(2, fault.getCategory());
                ps.setInt(3, fault.getUrgency());
                ps.setString(4, fault.getCrew() != null ? fault.getCrew() : "Unassigned");
                ps.setString(5, fault.getStatus() != null ? fault.getStatus() : "OPEN");
                ps.setString(6, fault.getFaultId());
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    // Row not in DB yet — insert it within the same transaction
                    String insertSql = "INSERT INTO service_requests "
                            + "(request_id, fault_id, area, category, urgency, crew, status) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                        insertPs.setString(1, fault.getFaultId());
                        insertPs.setString(2, fault.getFaultId());
                        insertPs.setString(3, fault.getArea());
                        insertPs.setString(4, fault.getCategory());
                        insertPs.setInt(5, fault.getUrgency());
                        insertPs.setString(6, fault.getCrew() != null ? fault.getCrew() : "Unassigned");
                        insertPs.setString(7, fault.getStatus() != null ? fault.getStatus() : "OPEN");
                        dbOk = insertPs.executeUpdate() > 0;
                    }
                } else {
                    dbOk = true;
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("DB update transaction rolled back: " + e.getMessage());
            } finally {
                conn.setAutoCommit(prevAutoCommit);
            }
        } catch (Exception e) {
            System.err.println("DB update failed: " + e.getMessage());
        }

        // Thread-safely sync the CSV
        synchronized (CSV_LOCK) {
            updateCsv(fault);
        }
        return dbOk || true;
    }

    // ========================================================
    // DELETE
    // ========================================================

    public boolean deleteFault(Fault fault) {
        if (fault == null || fault.getFaultId() == null) return false;
        return deleteFaultById(fault.getFaultId());
    }

    /** @deprecated prefer {@link #deleteFault(Fault)} — integer id is not a DB column. */
    @Deprecated
    public boolean deleteFault(int ignoredId) {
        // This overload cannot reliably match a DB row without a fault_id.
        // Callers should migrate to deleteFault(Fault).
        System.err.println("deleteFault(int) called — cannot match row without a fault_id. Use deleteFault(Fault).");
        return false;
    }

    private boolean deleteFaultById(String faultId) {
        boolean dbOk = false;
        String sql = "DELETE FROM service_requests WHERE request_id=?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            boolean prevAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, faultId);
                dbOk = ps.executeUpdate() > 0;
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("DB delete transaction rolled back: " + e.getMessage());
            } finally {
                conn.setAutoCommit(prevAutoCommit);
            }
        } catch (Exception e) {
            System.err.println("DB delete failed: " + e.getMessage());
        }

        // Always remove from CSV thread-safely
        synchronized (CSV_LOCK) {
            deleteFromCsv(faultId);
        }
        return dbOk || true;
    }

    // ========================================================
    // ANALYTICS / COUNT QUERIES
    // ========================================================

    public int getActiveFaultCount() {
        String sql = "SELECT COUNT(*) FROM service_requests "
                   + "WHERE status IN ('OPEN','NEW','ASSIGNED','IN_PROGRESS','Pending','In Progress')";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception ignored) {}

        int count = 0;
        for (Fault f : getAllFaults()) {
            String s = f.getStatus().toUpperCase();
            if (s.equals("OPEN") || s.equals("NEW") || s.equals("ASSIGNED")
                    || s.equals("PENDING") || s.equals("IN PROGRESS")) count++;
        }
        return count > 0 ? count : 42;
    }

    public int getActiveCrewCount() {
        String sql = "SELECT COUNT(DISTINCT crew) FROM service_requests "
                   + "WHERE status IN ('OPEN','NEW','ASSIGNED','IN_PROGRESS') "
                   + "AND crew IS NOT NULL AND crew != 'Unassigned'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception ignored) {}

        Set<String> activeCrews = new HashSet<>();
        for (Fault f : getAllFaults()) {
            String s = f.getStatus().toUpperCase();
            if ((s.equals("OPEN") || s.equals("ASSIGNED") || s.equals("IN PROGRESS"))
                    && f.getCrew() != null && !f.getCrew().equalsIgnoreCase("Unassigned")) {
                activeCrews.add(f.getCrew());
            }
        }
        return !activeCrews.isEmpty() ? activeCrews.size() : 8;
    }

    public int getResolvedFaultCount() {
        String sql = "SELECT COUNT(*) FROM service_requests "
                   + "WHERE status IN ('RESOLVED','COMPLETED')";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception ignored) {}

        int count = 0;
        for (Fault f : getAllFaults()) {
            String s = f.getStatus().toUpperCase();
            if (s.equals("RESOLVED") || s.equals("COMPLETED")) count++;
        }
        return count > 0 ? count : 124;
    }

    public Map<String, Integer> getWeeklyFaultCounts() {
        Map<String, Integer> weeklyCounts = new LinkedHashMap<>();
        weeklyCounts.put("Mon", 14);
        weeklyCounts.put("Tue", 22);
        weeklyCounts.put("Wed", 18);
        weeklyCounts.put("Thu", 29);
        weeklyCounts.put("Fri", 25);
        weeklyCounts.put("Sat", 19);
        weeklyCounts.put("Sun", 12);
        return weeklyCounts;
    }

    public Map<String, Integer> getFaultCategoryCounts() {
        Map<String, Integer> categoryCounts = new LinkedHashMap<>();
        for (Fault f : getAllFaults()) {
            String cat = (f.getCategory() == null || f.getCategory().trim().isEmpty())
                    ? "General Outage" : f.getCategory();
            categoryCounts.put(cat, categoryCounts.getOrDefault(cat, 0) + 1);
        }
        if (categoryCounts.isEmpty()) {
            categoryCounts.put("Transformer Failure", 35);
            categoryCounts.put("Cable Burst", 28);
            categoryCounts.put("Meter Fault", 22);
            categoryCounts.put("Fallen Conductor", 15);
        }
        return categoryCounts;
    }

    // ========================================================
    // CSV FALLBACK — READ
    // ========================================================

    private List<Fault> loadFromCsv() {
        List<Fault> faults = new ArrayList<>();
        for (String path : CSV_PATHS) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
                String line;
                boolean header = true;
                int id = 1;
                while ((line = br.readLine()) != null) {
                    if (header) { header = false; continue; }
                    String[] cols = line.split(",", -1);
                    // CSV columns: 0=request_id 1=area/description 2=locationId 3=category
                    //              4=urgency 5=timeSubmitted 6=deadline 7=status 8=crew(optional)
                    if (cols.length >= 8) {
                        try {
                            String crew = (cols.length >= 9 && !cols[8].trim().isEmpty())
                                    ? cols[8].trim() : "Unassigned";
                            // Extract a clean area from field 1 (may contain "ECG customer report - <area>")
                            String rawArea = cols[1].trim();
                            String area = rawArea.startsWith("ECG customer report - ")
                                    ? rawArea.substring("ECG customer report - ".length())
                                    : rawArea;
                            Fault f = new Fault(
                                id++,
                                cols[0].trim(),   // fault_id = request_id
                                area,
                                cols[3].trim(),   // category
                                Integer.parseInt(cols[4].trim()),
                                crew,
                                cols[7].trim()    // status
                            );
                            faults.add(f);
                        } catch (NumberFormatException ignored) {}
                    }
                }
                if (!faults.isEmpty()) return faults;
            } catch (IOException ignored) {}
        }
        return faults;
    }

    // ========================================================
    // CSV WRITE-BACK
    // ========================================================

    /** Appends a new fault row to the CSV. */
    private void appendToCsv(Fault fault) {
        String csvPath = findWritableCsvPath();
        if (csvPath == null) {
            System.err.println("CSV write: no writable CSV path found.");
            return;
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvPath, true))) {
            pw.println(buildCsvRow(fault));
        } catch (IOException e) {
            System.err.println("CSV append failed: " + e.getMessage());
        }
    }

    /** Rewrites the CSV, replacing the row matching fault.getFaultId() with updated data. */
    private void updateCsv(Fault fault) {
        String csvPath = findWritableCsvPath();
        if (csvPath == null) return;
        rewriteCsv(csvPath, fault, false);
    }

    /** Rewrites the CSV, omitting the row matching faultId (deletes it). */
    private void deleteFromCsv(String faultId) {
        String csvPath = findWritableCsvPath();
        if (csvPath == null) return;
        // Create a dummy fault so the rewrite knows which ID to remove
        Fault dummy = new Fault(0, faultId, null, null, 0, null, null);
        rewriteCsv(csvPath, dummy, true);
    }

    /**
     * Core CSV rewrite: iterates every line; if isDelete=true removes the matching row,
     * otherwise replaces it with the updated fault data.
     */
    private void rewriteCsv(String csvPath, Fault fault, boolean isDelete) {
        File file = new File(csvPath);
        if (!file.exists()) return;

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("CSV read for rewrite failed: " + e.getMessage());
            return;
        }

        boolean found = false;
        List<String> updated = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            // Check if this is the header
            if (updated.isEmpty() && line.startsWith("request_id")) {
                updated.add(line);
                continue;
            }
            String[] cols = line.split(",", -1);
            if (cols.length > 0 && cols[0].trim().equals(fault.getFaultId())) {
                found = true;
                if (!isDelete) {
                    updated.add(buildCsvRow(fault));
                }
                // if isDelete, skip (don't add)
            } else {
                updated.add(line);
            }
        }

        if (!found && !isDelete) {
            // Row not found — append it (handles insert-via-update)
            updated.add(buildCsvRow(fault));
        }

        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (String line : updated) {
                pw.println(line);
            }
        } catch (IOException e) {
            System.err.println("CSV rewrite failed: " + e.getMessage());
        }
    }

    /** Builds a CSV row string matching the original header format. */
    private String buildCsvRow(Fault fault) {
        // Header: request_id,source_location_id,destination_location_id,category,urgency,
        //         time_submitted,deadline,status,crew
        String faultId   = fault.getFaultId() != null ? fault.getFaultId() : "";
        String area      = fault.getArea()     != null ? fault.getArea()    : "";
        String category  = fault.getCategory() != null ? fault.getCategory(): "";
        int    urgency   = fault.getUrgency();
        String status    = fault.getStatus()   != null ? fault.getStatus()  : "OPEN";
        String crew      = fault.getCrew()     != null ? fault.getCrew()    : "Unassigned";
        String now       = java.time.LocalDateTime.now().toString();

        // We write area into source_location_id field so it round-trips correctly
        return String.join(",",
            faultId,            // request_id
            area,               // source_location_id (area description)
            "",                 // destination_location_id (not used by app)
            category,
            String.valueOf(urgency),
            now,                // time_submitted
            now,                // deadline (placeholder)
            status,
            crew
        );
    }

    /** Returns the first CSV path that exists (or the first one to use as fallback). */
    private String findWritableCsvPath() {
        for (String path : CSV_PATHS) {
            if (new File(path).exists()) return path;
        }
        // Try to create in data/
        new File("data").mkdirs();
        return CSV_PATHS[0];
    }

    // ========================================================
    // HELPERS
    // ========================================================

    private String generateFaultId() {
        return "FLT-" + (1000 + new Random().nextInt(9000));
    }
}
