package dao;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LocationDAO {

    public ObservableList<String> getAllLocations() {

        ObservableList<String> locations =
                FXCollections.observableArrayList();

        String sql = """
                SELECT name
                FROM locations
                ORDER BY name;
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

                locations.add(
                        rs.getString("name")
                );

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return locations;

    }

}