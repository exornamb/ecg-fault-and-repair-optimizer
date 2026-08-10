package dao;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class AlgorithmRunDAO {

    /**
     * Returns average runtime in milliseconds
     * for each algorithm at each input size.
     */
    public Map<String, Map<Integer, Double>>
    getAverageRuntime() {

        Map<String, Map<Integer, Double>> results =
                new LinkedHashMap<>();

        String sql = """
            SELECT
                algorithm,
                input_size,
                AVG(time_ns) AS average_time
            FROM algorithm_runs
            GROUP BY algorithm, input_size
            ORDER BY algorithm, input_size;
            """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            while (rs.next()) {

                String algorithm =
                        rs.getString("algorithm");

                int inputSize =
                        rs.getInt("input_size");

                double averageNs =
                        rs.getDouble("average_time");

                // Convert nanoseconds to milliseconds
                double averageMs =
                        averageNs / 1_000_000.0;

                results
                        .computeIfAbsent(
                                algorithm,
                                key -> new LinkedHashMap<>()
                        )
                        .put(
                                inputSize,
                                averageMs
                        );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return results;
    }


    /**
     * Returns average memory usage in KB
     * for each algorithm at each input size.
     */
    public Map<String, Map<Integer, Double>>
    getAverageMemory() {

        Map<String, Map<Integer, Double>> results =
                new LinkedHashMap<>();

        String sql = """
            SELECT
                algorithm,
                input_size,
                AVG(memory_kb) AS average_memory
            FROM algorithm_runs
            GROUP BY algorithm, input_size
            ORDER BY algorithm, input_size;
            """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            while (rs.next()) {

                String algorithm =
                        rs.getString("algorithm");

                int inputSize =
                        rs.getInt("input_size");

                double averageMemory =
                        rs.getDouble("average_memory");

                results
                        .computeIfAbsent(
                                algorithm,
                                key -> new LinkedHashMap<>()
                        )
                        .put(
                                inputSize,
                                averageMemory
                        );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return results;
    }
}