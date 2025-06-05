package database;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseMigration {
    private DatabaseConfig config;
    private DatabaseConnection dbConnection;
    
    public DatabaseMigration() {
        config = DatabaseConfig.getInstance();
        dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean runMigration() {
        try {
            if (config.isDebugMode()) {
                System.out.println(" Starting database migration...");
            }
            
            // Create database if not exists
            createDatabase();
            
            // Create tables
            createUsersTable();
            createContractsTable();
            
            // // Create indexes
            // createIndexes();
            
            if (config.isDebugMode()) {
                System.out.println(" Database migration completed successfully!");
            }
            
            return true;
            
        } catch (SQLException e) {
            System.err.println(" Migration failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private void createDatabase() throws SQLException {
        // Connect without specifying database first
        String baseUrl = String.format("jdbc:mysql://%s:%s/", 
            "127.0.0.1", "3306");
            
        try (Connection conn = java.sql.DriverManager.getConnection(
                baseUrl, config.getDbUsername(), config.getDbPassword());
             Statement stmt = conn.createStatement()) {
            
            String dbName = "adauang_db";
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            
            if (config.isDebugMode()) {
                System.out.println(" Database '" + dbName + "' ready");
            }
        }
    }
    
    private void createUsersTable() throws SQLException {
        Connection conn = dbConnection.getConnection();
        try (Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS %s (
                    user_id INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    full_name VARCHAR(100) NOT NULL,
                    role VARCHAR(20) DEFAULT 'user',
                    password VARCHAR(255) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                )
                """.formatted(config.getUsersTableName());
            
            stmt.executeUpdate(sql);
            
            if (config.isDebugMode()) {
                System.out.println(" Users table ready");
                System.out.println("   - user_id (Primary Key)");
                System.out.println("   - username (Login)");
                System.out.println("   - full_name (Display Name)");
                System.out.println("   - role (user/admin)");
                System.out.println("   - password (Authentication)");
            }
        }
    }
    
    private void createContractsTable() throws SQLException {
        Connection conn = dbConnection.getConnection();
        try (Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS kontrak (
                    contract_id INT PRIMARY KEY AUTO_INCREMENT,
                    staff_id INT NOT NULL,
                    loan_term INT NOT NULL,
                    status BOOLEAN NOT NULL,
                    total_payment INT NOT NULL,
                    loan_payment INT NOT NULL,
                    remaining_installment INT NOT NULL,
                    branch VARCHAR(50) NOT NULL,
                    customer_name VARCHAR(50) NOT NULL,
                    FOREIGN KEY (staff_id) REFERENCES %s(user_id)
                )
                """.formatted(config.getUsersTableName());

            stmt.executeUpdate(sql);

            if (config.isDebugMode()) {
                System.out.println(" Kontrak table ready");
                System.out.println("   - contract_id (Primary Key)");
                System.out.println("   - staff_id (Foreign Key ke users)");
                System.out.println("   - loan_term, status, total_payment, loan_payment, remaining_installment, branch, customer_name");
            }
        }
    }
}