package com.g15.dsa.dao;

import com.g15.dsa.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AlgorithmRunDAO — Persists and retrieves empirical benchmark results
 * to/from the algorithm_runs PostgreSQL table.
 */
public class AlgorithmRunDAO {

    public boolean insertRun(String algorithmName, int inputSize, long timeNs, long memoryKb, int runNumber) {
        String sql = "INSERT INTO algorithm_runs (algorithm_name, input_size, time_ns, memory_kb, run_number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, algorithmName);
            ps.setInt(2, inputSize);
            ps.setLong(3, timeNs);
            ps.setLong(4, memoryKb);
            ps.setInt(5, runNumber);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("AlgorithmRunDAO.insertRun failed: " + e.getMessage());
            return false;
        }
    }

    public List<String> getSummary() {
        List<String> rows = new ArrayList<>();
        String sql = "SELECT algorithm_name, input_size, " +
                     "ROUND(AVG(time_ns/1000000.0)::numeric, 2) AS avg_ms, " +
                     "ROUND(AVG(memory_kb)::numeric, 0) AS avg_kb, COUNT(*) AS runs " +
                     "FROM algorithm_runs " +
                     "GROUP BY algorithm_name, input_size " +
                     "ORDER BY algorithm_name, input_size";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(String.format("%-18s n=%-7d avg=%-10s ms  mem=%-8s KB  runs=%d",
                    rs.getString("algorithm_name"),
                    rs.getInt("input_size"),
                    rs.getString("avg_ms"),
                    rs.getString("avg_kb"),
                    rs.getInt("runs")));
            }
        } catch (Exception e) {
            rows.add("Database unavailable: " + e.getMessage());
        }
        return rows;
    }

    public long getRunCount() {
        String sql = "SELECT COUNT(*) FROM algorithm_runs";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getLong(1);
        } catch (Exception e) {
            System.err.println("AlgorithmRunDAO.getRunCount failed: " + e.getMessage());
        }
        return 0;
    }
}
