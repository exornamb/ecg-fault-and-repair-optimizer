package com.g15.dsa.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DatabaseConnection.java
 * Manages PostgreSQL JDBC connection pooling and credentials.
 * Loads configuration from config.properties or environment variables.
 */
public class DatabaseConnection {

    private static final String CONFIG_PATH = "config.properties";
    private static Properties properties;

    public static synchronized Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            File configFile = new File(CONFIG_PATH);
            if (configFile.exists()) {
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    properties.load(fis);
                } catch (IOException e) {
                    System.err.println("Warning: Failed to load " + CONFIG_PATH + ": " + e.getMessage());
                }
            } else {
                // Default fallback configuration for local or environment variables
                String envUrl = System.getenv("DB_URL");
                String envUser = System.getenv("DB_USER");
                String envPass = System.getenv("DB_PASSWORD");
                if (envUrl != null) properties.setProperty("db.url", envUrl);
                if (envUser != null) properties.setProperty("db.user", envUser);
                if (envPass != null) properties.setProperty("db.password", envPass);
            }
        }
        return properties;
    }

    public static Connection getConnection() throws SQLException {
        Properties props = getProperties();
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        if (url == null) {
            url = "jdbc:postgresql://localhost:5432/ecg_dispatch_db";
        }
        if (user == null) user = "postgres";
        if (password == null) password = "postgres";

        return DriverManager.getConnection(url, user, password);
    }

    public static boolean isAvailable() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        if (isAvailable()) {
            System.out.println("Database connection established successfully!");
        } else {
            System.out.println("Database offline or unreachable. Offline CSV dataset fallback will be used.");
        }
    }
}
