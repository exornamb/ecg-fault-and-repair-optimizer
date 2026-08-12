package dao;

import database.DatabaseConnection;
import model.Crew;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResourceDAO {

    public ObservableList<String> getAvailableCrews() {

        ObservableList<String> crews =
                FXCollections.observableArrayList();

        String sql = """
                SELECT
                                 crew_name,
                                 type,
                                 availability,
                                 capacity
                             FROM resources
                             WHERE availability='AVAILABLE'
                             ORDER BY crew_name;
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

                crews.add(
                        rs.getString("crew_name")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return crews;
    }

    public String getRecommendedCrew(String category) {

        String crewType;

        switch (category) {

            case "Cable Burst":
            case "Fallen Conductor":
                crewType = "Cable truck";
                break;

            case "Transformer Failure":
                crewType = "Transformer crew";
                break;

            case "Meter Fault":
                crewType = "Meter team";
                break;

            default:
                return null;
        }

        String sql = """
                SELECT crew_name
                        FROM resources
                        WHERE type = ?
                          AND availability = 'AVAILABLE'
                        ORDER BY capacity DESC, id
                        LIMIT 1;
            """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, crewType);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("crew_name");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getRecommendationReason(String category) {

        switch (category) {

            case "Transformer Failure":
                return """
                    ✓ Category: Transformer Failure
                    ✓ Specialized transformer crew
                    ✓ Highest available capacity
                    """;

            case "Cable Burst":
                return """
                    ✓ Category: Cable Burst
                    ✓ Cable truck required
                    ✓ Highest available capacity
                    """;

            case "Meter Fault":
                return """
                    ✓ Category: Meter Fault
                    ✓ Meter specialists selected
                    ✓ Highest available capacity
                    """;

            case "Fallen Conductor":
                return """
                    ✓ Category: Fallen Conductor
                    ✓ Emergency line response
                    ✓ Highest available capacity
                    """;

            default:
                return "✓ Recommended automatically";
        }
    }

    public Crew getBestCrew(String category) {

        String requiredType;

        switch (category) {

            case "Transformer Failure":
                requiredType = "Transformer crew";
                break;

            case "Cable Burst":
            case "Fallen Conductor":
                requiredType = "Cable truck";
                break;

            case "Meter Fault":
                requiredType = "Meter team";
                break;

            default:
                return null;
        }

        String sql = """
            SELECT
                crew_name,
                type,
                availability,
                capacity
            FROM resources
            WHERE type = ?
              AND availability = 'AVAILABLE'
            ORDER BY capacity DESC
            LIMIT 1;
            """;

        try (

                Connection conn = DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)

        ) {

            stmt.setString(1, requiredType);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Crew(

                        rs.getString("crew_name"),

                        rs.getString("type"),

                        rs.getString("availability"),

                        rs.getInt("capacity")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    public int getAvailableCrewCount() {

        String sql = """
            SELECT COUNT(*)
            FROM resources
            WHERE availability = 'AVAILABLE';
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

    public void setCrewBusy(String crewName) {

        String sql = """
            UPDATE resources
            SET availability = 'BUSY'
            WHERE crew_name = ?;
            """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)

        ) {

            stmt.setString(1, crewName);

            stmt.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void setCrewAvailable(String crewName) {

        String sql = """
            UPDATE resources
            SET availability = 'AVAILABLE'
            WHERE crew_name = ?;
            """;

        try (

                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)

        ) {

            stmt.setString(1, crewName);

            stmt.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public ObservableList<String> getAllCrews() {

        ObservableList<String> crews =
                FXCollections.observableArrayList();

        String sql = """
            SELECT crew_name
            FROM resources
            ORDER BY crew_name;
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

                crews.add(
                        rs.getString("crew_name")
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return crews;
    }

    public int getHomeLocation(String crewName) {

        String sql = """
        SELECT home_location
        FROM resources
        WHERE crew_name = ?;
        """;

        try (
                Connection conn =
                        DatabaseConnection.getConnection();

                PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, crewName);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("home_location");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}