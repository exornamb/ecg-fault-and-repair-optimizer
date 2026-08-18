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

public class LocationDAO {

    public List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        String sql = """
                SELECT name
                FROM locations
                ORDER BY name;
                """;

        try (
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                locations.add(rs.getString("name"));
            }
            if (!locations.isEmpty()) {
                return locations;
            }
        } catch (Exception e) {
            // Fallback to CSV
        }

        return loadLocationsFromCsv();
    }

    private List<String> loadLocationsFromCsv() {
        List<String> locations = new ArrayList<>();
        String[] csvPaths = {"data/locations.csv", "locations.csv"};
        for (String path : csvPaths) {
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                boolean header = true;
                while ((line = br.readLine()) != null) {
                    if (header) { header = false; continue; }
                    String[] cols = line.split(",", -1);
                    if (cols.length >= 2) {
                        locations.add(cols[1].trim());
                    }
                }
                if (!locations.isEmpty()) {
                    return locations;
                }
            } catch (IOException ignored) {}
        }

        // Hardcoded Accra/Legon fallback list if CSV not found
        if (locations.isEmpty()) {
            locations.add("Achimota Substation");
            locations.add("Legon Main Campus Substation");
            locations.add("East Legon Switching Station");
            locations.add("Madina Market Feeder");
            locations.add("Adenta Housing Station");
            locations.add("Airport Residential Substation");
            locations.add("Noguchi Medical Research Line");
            locations.add("Diaspora Halls Feeder");
        }
        return locations;
    }
}