package com.g15.dsa.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.g15.dsa.database.DatabaseConnection;
import com.g15.dsa.model.Crew;

public class ResourceDAO {

    public List<String> getAvailableCrews() {
        List<String> crews = new ArrayList<>();
        String sql = """
                SELECT crew_name
                FROM resources
                WHERE availability='AVAILABLE'
                ORDER BY crew_name;
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                crews.add(rs.getString("crew_name"));
            }
            if (!crews.isEmpty()) {
                return crews;
            }
        } catch (Exception e) {
            // Fallback to CSV
        }

        return loadCrewsFromCsv(true);
    }

    public List<String> getAllCrews() {
        List<String> crews = new ArrayList<>();
        crews.add("Crew Alpha (Legon Substation)");
        crews.add("Crew Bravo (East Legon Hub)");
        crews.add("Crew Charlie (Madina Feeder)");
        crews.add("Crew Delta (Achimota Grid)");
        crews.add("Crew Echo (Adenta Line)");

        List<String> csvCrews = loadCrewsFromCsv(false);
        for (String c : csvCrews) {
            if (!crews.contains(c)) {
                crews.add(c);
            }
        }
        return crews;
    }

    public List<Crew> getAllCrewDetails() {
        List<Crew> list = new ArrayList<>();
        list.add(new Crew("C001", "Crew Alpha (Legon Substation)", "Emergency Line Response", "AVAILABLE", 4, "Legon"));
        list.add(new Crew("C002", "Crew Bravo (East Legon Hub)", "Transformer crew", "AVAILABLE", 4, "East Legon"));
        list.add(new Crew("C003", "Crew Charlie (Madina Feeder)", "Cable truck", "AVAILABLE", 3, "Madina"));
        list.add(new Crew("C004", "Crew Delta (Achimota Grid)", "Cable truck", "AVAILABLE", 5, "Achimota"));
        list.add(new Crew("C005", "Crew Echo (Adenta Line)", "Meter team", "AVAILABLE", 3, "Adenta"));

        List<Crew> fromCsv = loadCrewObjectsFromCsv();
        for (Crew c : fromCsv) {
            boolean exists = list.stream().anyMatch(existing -> existing.getName().equalsIgnoreCase(c.getName()));
            if (!exists) {
                list.add(c);
            }
        }
        return list;
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
                crewType = "Emergency Line Response";
                break;
        }

        String sql = """
                SELECT crew_name
                FROM resources
                WHERE type = ? AND availability = 'AVAILABLE'
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
        } catch (Exception ignored) {}

        List<String> available = getAvailableCrews();
        return !available.isEmpty() ? available.get(0) : "Alpha Rapid Response";
    }

    public String getRecommendationReason(String category) {
        switch (category) {
            case "Transformer Failure":
                return "✓ Category: Transformer Failure\n✓ Specialized transformer crew\n✓ Highest available capacity";
            case "Cable Burst":
                return "✓ Category: Cable Burst\n✓ Cable truck required\n✓ Highest available capacity";
            case "Meter Fault":
                return "✓ Category: Meter Fault\n✓ Meter specialists selected\n✓ Highest available capacity";
            case "Fallen Conductor":
                return "✓ Category: Fallen Conductor\n✓ Emergency line response\n✓ Highest available capacity";
            default:
                return "✓ Recommended automatically based on capacity & proximity";
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
                requiredType = "Emergency Line Response";
                break;
        }

        String sql = """
            SELECT crew_name, type, availability, capacity
            FROM resources
            WHERE type = ? AND availability = 'AVAILABLE'
            ORDER BY capacity DESC
            LIMIT 1;
            """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
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
        } catch (Exception ignored) {}

        return new Crew("Alpha Rapid Response", requiredType, "AVAILABLE", 4);
    }

    public int getAvailableCrewCount() {
        return getAvailableCrews().size();
    }

    public void setCrewBusy(String crewName) {
        String sql = "UPDATE resources SET availability = 'BUSY' WHERE crew_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, crewName);
            stmt.executeUpdate();
        } catch (Exception ignored) {}
    }

    public void setCrewAvailable(String crewName) {
        String sql = "UPDATE resources SET availability = 'AVAILABLE' WHERE crew_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, crewName);
            stmt.executeUpdate();
        } catch (Exception ignored) {}
    }

    public int getHomeLocation(String crewName) {
        String sql = "SELECT home_location FROM resources WHERE crew_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, crewName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("home_location");
            }
        } catch (Exception ignored) {}
        return 1;
    }

    private List<String> loadCrewsFromCsv(boolean onlyAvailable) {
        List<String> crews = new ArrayList<>();
        String[] csvPaths = {"data/resources.csv", "resources.csv"};
        for (String path : csvPaths) {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                boolean header = true;
                while ((line = br.readLine()) != null) {
                    if (header) { header = false; continue; }
                    String[] cols = line.split(",", -1);
                    if (cols.length >= 4) {
                        String name = cols.length >= 6 && !cols[5].trim().isEmpty()
                                ? cols[5].trim()
                                : cols[1].trim() + " #" + cols[0].trim();
                        String avail = cols.length >= 5 ? cols[4].trim() : "AVAILABLE";
                        if (!onlyAvailable || avail.equalsIgnoreCase("AVAILABLE")) {
                            if (!crews.contains(name)) {
                                crews.add(name);
                            }
                        }
                    }
                }
                if (!crews.isEmpty()) {
                    return crews;
                }
            } catch (IOException ignored) {}
        }

        if (crews.isEmpty()) {
            crews.add("Crew Alpha (Legon Substation)");
            crews.add("Crew Bravo (East Legon Hub)");
            crews.add("Crew Charlie (Madina Feeder)");
            crews.add("Crew Delta (Achimota Grid)");
            crews.add("Crew Echo (Adenta Line)");
        }
        return crews;
    }

    private List<Crew> loadCrewObjectsFromCsv() {
        List<Crew> list = new ArrayList<>();
        String[] csvPaths = {"data/resources.csv", "resources.csv"};
        for (String path : csvPaths) {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                boolean header = true;
                while ((line = br.readLine()) != null) {
                    if (header) { header = false; continue; }
                    String[] cols = line.split(",", -1);
                    if (cols.length >= 4) {
                        String id = cols[0].trim();
                        String type = cols[1].trim();
                        String baseLoc = cols[2].trim();
                        int cap = 4;
                        try { cap = Integer.parseInt(cols[3].trim()); } catch (NumberFormatException ignored) {}
                        String avail = cols.length >= 5 ? cols[4].trim() : "AVAILABLE";
                        String name = cols.length >= 6 && !cols[5].trim().isEmpty() ? cols[5].trim() : type + " #" + id;
                        list.add(new Crew(id, name, type, avail, cap, baseLoc));
                    }
                }
                if (!list.isEmpty()) return list;
            } catch (IOException ignored) {}
        }
        return list;
    }
}