package database;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
            createInstalmentTable();
            
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
                    id_user INT PRIMARY KEY AUTO_INCREMENT,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    fullname VARCHAR(100) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    branch VARCHAR(50) NOT NULL,
                    role VARCHAR(20) DEFAULT 'user'
                )
                """.formatted(config.getUsersTableName());
            
            stmt.executeUpdate(sql);
            
            if (config.isDebugMode()) {
                System.out.println(" Users table ready");
                System.out.println("   - id_user (Primary Key)");
                System.out.println("   - username (Login)");
                System.out.println("   - fullname (Nama Lengkap)");
                System.out.println("   - password (Authentication)");
                System.out.println("   - branch (Cabang)");
                System.out.println("   - role (user/admin)");
            }
        }
    }
    
    private void createContractsTable() throws SQLException {
        Connection conn = dbConnection.getConnection();
        try (Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS kontrak (
                    id_kontrak INT PRIMARY KEY AUTO_INCREMENT,
                    nama_user VARCHAR(100) NOT NULL,
                    total INT NOT NULL,
                    tenor INT NOT NULL,
                    jumlah_bayar INT NOT NULL,
                    jumlah_bayar_bunga INT NOT NULL,
                    cicilan_per_bulan INT NOT NULL,
                    status BOOLEAN NOT NULL,
                    tanggal_pinjam DATE NOT NULL,
                    id_user INT NOT NULL,
                    FOREIGN KEY (id_user) REFERENCES %s(id_user)
                )
                """.formatted(config.getUsersTableName());

            stmt.executeUpdate(sql);

            if (config.isDebugMode()) {
                System.out.println(" Kontrak table ready");
                System.out.println("   - id_kontrak (Primary Key)");
                System.out.println("   - nama_user");
                System.out.println("   - total");
                System.out.println("   - tenor");
                System.out.println("   - jumlah_bayar");
                System.out.println("   - jumlah_bayar_bunga");
                System.out.println("   - cicilan_per_bulan");
                System.out.println("   - status");
                System.out.println("   - tanggal_pinjam");
                System.out.println("   - id_user (Foreign Key ke users)");
            }
        }
    }

    private void createInstalmentTable() throws SQLException {
        Connection conn = dbConnection.getConnection();
        try (Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS cicilan (
                    id_cicilan INT PRIMARY KEY AUTO_INCREMENT,
                    id_kontrak INT NOT NULL,
                    jumlah_cicilan INT NOT NULL,
                    tanggal_cicilan DATE NOT NULL,
                    id_staff INT NOT NULL,
                    FOREIGN KEY (id_kontrak) REFERENCES kontrak(id_kontrak),
                    FOREIGN KEY (id_staff) REFERENCES users(id_user)
                )
                """;

            stmt.executeUpdate(sql);

            if (config.isDebugMode()) {
                System.out.println(" Cicilan table ready");
                System.out.println("   - id_cicilan (Primary Key)");
                System.out.println("   - id_kontrak (FK ke kontrak)");
                System.out.println("   - jumlah_cicilan");
                System.out.println("   - tanggal_cicilan");
                System.out.println("   - id_staff (FK ke users)");
            }
        }
    }


}