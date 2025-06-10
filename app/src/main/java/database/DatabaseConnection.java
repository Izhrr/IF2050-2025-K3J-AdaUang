package database;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private DatabaseConfig config;
    
    private DatabaseConnection() {
        config = DatabaseConfig.getInstance();
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                    config.getDbUrl(),
                    config.getDbUsername(), 
                    config.getDbPassword()
                );
                
                if (config.isDebugMode()) {
                    System.out.println(" Database connection established");
                }
            }
        } catch (SQLException e) {
            System.err.println(" Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                if (config.isDebugMode()) {
                    System.out.println(" Database connection closed");
                }
            }
        } catch (SQLException e) {
            System.err.println(" Error closing database connection: " + e.getMessage());
        }
    }
    
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}