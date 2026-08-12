package db;

import java.sql.Connection;

public class DatabaseTest {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            if (connection != null) {
                System.out.println(
                        "SUCCESS: Connected to ecg_legon PostgreSQL database!"
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "FAILED: Could not connect to database."
            );

            e.printStackTrace();
        }
    }
}
