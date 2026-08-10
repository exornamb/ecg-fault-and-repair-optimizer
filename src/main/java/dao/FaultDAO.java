package dao;

import model.CrewWorkload;
import javafx.collections.ObservableList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.HashMap;
import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Fault;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FaultDAO {

    // =========================
    // GET ALL FAULTS
    // =========================

    public ObservableList<Fault> getAllFaults() {

        ObservableList<Fault> faults =
                FXCollections.observableArrayList();

        String sql = """
                SELECT
                    sr.id,
                    l.name,
                    sr.category,
                    sr.urgency,
                    sr.assigned_crew,
                    sr.status
                FROM service_requests sr
                JOIN locations l
                    ON sr.location_id = l.id
                ORDER BY sr.id;
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

                faults.add(

                        new Fault(

                                rs.getInt("id"),

                                "FLT-" + rs.getInt("id"),

                                rs.getString("name"),

                                rs.getString("category"),

                                rs.getInt("urgency"),

                                rs.getString("assigned_crew"),

                                rs.getString("status")
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return faults;
    }


    // =========================
    // ASSIGN CREW
    // =========================

    public void assignCrew(int id, String crew) {

        String sql = """
                UPDATE service_requests
                SET assigned_crew = ?,
                    status = 'ASSIGNED'
                WHERE id = ?;
                """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)

        ) {

            stmt.setString(1, crew);

            stmt.setInt(2, id);

            stmt.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // INSERT FAULT
    // =========================

    public void insertFault(Fault fault) {

        String locationSql = """
                SELECT id
                FROM locations
                WHERE name = ?
                """;

        String insertSql = """
                INSERT INTO service_requests
                (
                    source,
                    location_id,
                    category,
                    urgency,
                    submitted,
                    deadline,
                    status,
                    assigned_crew
                )
                VALUES
                (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement locationStmt =
                        conn.prepareStatement(locationSql)

        ) {

            locationStmt.setString(
                    1,
                    fault.getArea()
            );

            ResultSet rs =
                    locationStmt.executeQuery();

            if (!rs.next()) {

                return;
            }

            int locationId =
                    rs.getInt("id");

            PreparedStatement insertStmt =
                    conn.prepareStatement(insertSql);

            insertStmt.setString(
                    1,
                    "Customer Report"
            );

            insertStmt.setInt(
                    2,
                    locationId
            );

            insertStmt.setString(
                    3,
                    fault.getCategory()
            );

            insertStmt.setInt(
                    4,
                    fault.getUrgency()
            );

            insertStmt.setString(
                    5,
                    java.time.LocalDateTime.now().toString()
            );

            insertStmt.setString(
                    6,
                    java.time.LocalDateTime.now()
                            .plusDays(1)
                            .toString()
            );

            insertStmt.setString(
                    7,
                    fault.getStatus()
            );

            insertStmt.setString(
                    8,
                    fault.getCrew()
            );

            insertStmt.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // UPDATE FAULT
    // =========================

    public void updateFault(Fault fault) {

        String locationSql = """
                SELECT id
                FROM locations
                WHERE name = ?
                """;

        String updateSql = """
                UPDATE service_requests
                SET
                    location_id = ?,
                    category = ?,
                    urgency = ?,
                    status = ?,
                    assigned_crew = ?
                WHERE id = ?
                """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement locationStmt =
                        conn.prepareStatement(locationSql)

        ) {

            locationStmt.setString(
                    1,
                    fault.getArea()
            );

            ResultSet rs =
                    locationStmt.executeQuery();

            if (!rs.next()) {

                return;
            }

            int locationId =
                    rs.getInt("id");

            PreparedStatement updateStmt =
                    conn.prepareStatement(updateSql);

            updateStmt.setInt(
                    1,
                    locationId
            );

            updateStmt.setString(
                    2,
                    fault.getCategory()
            );

            updateStmt.setInt(
                    3,
                    fault.getUrgency()
            );

            updateStmt.setString(
                    4,
                    fault.getStatus()
            );

            updateStmt.setString(
                    5,
                    fault.getCrew()
            );

            updateStmt.setInt(
                    6,
                    fault.getId()
            );

            updateStmt.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================
    // DELETE FAULT
    // =========================

    public void deleteFault(int id) {

        String sql = """
                DELETE FROM service_requests
                WHERE id = ?
                """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)

        ) {

            stmt.setInt(
                    1,
                    id
            );

            stmt.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Map<String, Integer> getFaultCategoryCounts() {

        Map<String, Integer> counts = new HashMap<>();

        String sql = """
        SELECT category, COUNT(*) AS total
        FROM service_requests
        GROUP BY category
        ORDER BY category
    """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                counts.put(
                        rs.getString("category"),
                        rs.getInt("total")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return counts;
    }
    public Map<String, Integer> getWeeklyFaultCounts() {

        Map<String, Integer> counts = new LinkedHashMap<>();

        // Ensure all days appear even if they have zero faults
        counts.put("Mon", 0);
        counts.put("Tue", 0);
        counts.put("Wed", 0);
        counts.put("Thu", 0);
        counts.put("Fri", 0);
        counts.put("Sat", 0);
        counts.put("Sun", 0);

        String sql = """
        SELECT
            TO_CHAR(CAST(submitted AS TIMESTAMP), 'Dy') AS day,
            COUNT(*) AS total
        FROM service_requests
        GROUP BY day
        """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                String day = rs.getString("day").trim();

                counts.put(day, rs.getInt("total"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return counts;
    }

    public Map<String, Integer> getAreaCounts() {

        Map<String, Integer> counts =
                new LinkedHashMap<>();

        String sql = """
        SELECT
            l.name,
            COUNT(*) AS total
        FROM service_requests sr
        JOIN locations l
            ON sr.location_id = l.id
        GROUP BY l.name
        ORDER BY total DESC;
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

                counts.put(

                        rs.getString("name"),

                        rs.getInt("total")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return counts;
    }

    public Map<String, Integer> getPriorityCounts() {

        Map<String, Integer> counts =
                new LinkedHashMap<>();

        String sql = """
        SELECT
            urgency,
            COUNT(*) AS total
        FROM service_requests
        GROUP BY urgency
        ORDER BY urgency DESC;
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

                String priority;

                switch (rs.getInt("urgency")) {

                    case 5:
                        priority = "Critical";
                        break;

                    case 4:
                        priority = "High";
                        break;

                    case 3:
                        priority = "Medium";
                        break;

                    case 2:
                        priority = "Low";
                        break;

                    default:
                        priority = "Very Low";
                }

                counts.put(
                        priority,
                        rs.getInt("total")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return counts;
    }

    public Map<String, Integer> getStatusCounts() {

        Map<String, Integer> counts =
                new LinkedHashMap<>();

        String sql = """
        SELECT
            status,
            COUNT(*) AS total
        FROM service_requests
        GROUP BY status
        ORDER BY status;
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

                counts.put(

                        rs.getString("status"),

                        rs.getInt("total")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return counts;
    }

    public Map<String, Integer> getCrewWorkloadCounts() {

        Map<String, Integer> counts =
                new LinkedHashMap<>();

        String sql = """
        SELECT
            assigned_crew,
            COUNT(*) AS total
        FROM service_requests
        WHERE assigned_crew IS NOT NULL
          AND assigned_crew <> ''
        GROUP BY assigned_crew
        ORDER BY total DESC;
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

                counts.put(

                        rs.getString("assigned_crew"),

                        rs.getInt("total")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return counts;
    }

    public ObservableList<CrewWorkload> getCrewWorkload() {

        ObservableList<CrewWorkload> workload =
                FXCollections.observableArrayList();

        String sql = """
        SELECT
            r.crew_name,
            r.availability,
            r.capacity,
            COUNT(sr.id) AS assigned_faults
        FROM resources r
        LEFT JOIN service_requests sr
            ON r.crew_name = sr.assigned_crew
           AND sr.status <> 'RESOLVED'
        GROUP BY
            r.crew_name,
            r.availability,
            r.capacity
        ORDER BY assigned_faults DESC,
                 r.crew_name;
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

                workload.add(

                        new CrewWorkload(

                                rs.getString("crew_name"),

                                rs.getInt("assigned_faults"),

                                rs.getString("availability"),

                                rs.getInt("capacity")
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return workload;
    }
    public int getActiveFaultCount() {

        String sql = """
            SELECT COUNT(*)
            FROM service_requests
            WHERE status <> 'RESOLVED';
            """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()

        ) {

            if (rs.next()) {

                return rs.getInt(1);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }
    public int getActiveCrewCount() {

        String sql = """
            SELECT COUNT(*)
            FROM resources
            WHERE availability = 'BUSY';
            """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()

        ) {

            if (rs.next()) {

                return rs.getInt(1);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    public int getResolvedFaultCount() {

        String sql = """
            SELECT COUNT(*)
            FROM service_requests
            WHERE status = 'RESOLVED';
            """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()

        ) {

            if (rs.next()) {

                return rs.getInt(1);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }

    public int getLocationId(int faultId) {

        String sql = """
        SELECT location_id
        FROM service_requests
        WHERE id = ?;
        """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, faultId);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("location_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

}