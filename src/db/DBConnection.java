package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Loads DB credentials from config.properties (kept out of git via .gitignore)
 * and hands back a ready-to-use JDBC Connection.
 *
 * Usage:
 *   try (Connection conn = DBConnection.getConnection()) {
 *       // run queries
 *   }
 */
public class DBConnection {

    private static final String CONFIG_PATH = "config.properties";
    private static Properties props;

    private static Properties loadProperties() throws IOException {
        if (props == null) {
            props = new Properties();
            try (FileInputStream in = new FileInputStream(CONFIG_PATH)) {
                props.load(in);
            }
        }
        return props;
    }

    public static Connection getConnection() throws SQLException, IOException {
        Properties p = loadProperties();

        String url = p.getProperty("db.url");
        String user = p.getProperty("db.user");
        String password = p.getProperty("db.password");

        if (url == null || user == null || password == null) {
            throw new IllegalStateException(
                "Missing db.url / db.user / db.password in " + CONFIG_PATH);
        }

        return DriverManager.getConnection(url, user, password);
    }

    /** Quick manual test: run this to confirm the connection works. */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connected: " + !conn.isClosed());
        } catch (SQLException | IOException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}
