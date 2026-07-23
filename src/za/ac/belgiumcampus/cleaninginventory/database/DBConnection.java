package za.ac.belgiumcampus.cleaninginventory.database;

import za.ac.belgiumcampus.cleaninginventory.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Single entry point for obtaining JDBC connections to PostgreSQL (Supabase).
 */
public class DBConnection {

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "PostgreSQL JDBC driver not found. Add postgresql.jar to the project libraries.",
                    e);
        }

        return DriverManager.getConnection(
                AppConfig.getDbUrl(),
                AppConfig.getDbUsername(),
                AppConfig.getDbPassword());
    }
}
