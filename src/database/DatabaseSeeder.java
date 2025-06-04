package database;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSeeder {
    private DatabaseConfig config;
    private DatabaseConnection dbConnection;
    
    public DatabaseSeeder() {
        config = DatabaseConfig.getInstance();
        dbConnection = DatabaseConnection.getInstance();
    }
    
    public boolean runSeeder() {
        try {
            if (config.isDebugMode()) {
                System.out.println(" Starting database seeding...");
            }
            
            // Clear existing test data
            clearTestData();
            
            // Insert test users
            insertTestUsers();
            
            if (config.isDebugMode()) {
                System.out.println(" Database seeding completed successfully!");
                printSeededData();
            }
            
            return true;
            
        } catch (SQLException e) {
            System.err.println(" Seeding failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private void clearTestData() throws SQLException {
        Connection conn = dbConnection.getConnection();

        // Hapus SEMUA data dari tabel users
        String sqlDelete = "DELETE FROM " + config.getUsersTableName();
        try (PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {
            stmt.executeUpdate();
        }

        // Reset AUTO_INCREMENT ke 1
        String sqlReset = "ALTER TABLE " + config.getUsersTableName() + " AUTO_INCREMENT = 1";
        try (PreparedStatement stmt = conn.prepareStatement(sqlReset)) {
            stmt.executeUpdate();
        }
    }
    
    private void insertTestUsers() throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        String sql = "INSERT INTO " + config.getUsersTableName() + 
                    " (username, full_name, role, password) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Test accounts for development
            String[][] testUsers = {
                {"admin", "Administrator", "admin", "admin123"},
                {"izhrr", "Izhrr - Project Lead", "admin", "izhrr123"},
                {"testuser", "Test User", "user", "password123"}, 
                {"demo", "Demo Account", "user", "demo123"}
            };
            
            for (String[] user : testUsers) {
                stmt.setString(1, user[0]); // username
                stmt.setString(2, user[1]); // full_name
                stmt.setString(3, user[2]); // role
                stmt.setString(4, user[3]); // password
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            
            if (config.isDebugMode()) {
                System.out.println(" Inserted " + results.length + " test users");
            }
        }
    }
    
    private void printSeededData() throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        String sql = "SELECT user_id, username, full_name, role, created_at FROM " + 
                    config.getUsersTableName() + " ORDER BY created_at DESC LIMIT 10";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            
            System.out.println("\n SEEDED TEST ACCOUNTS:");
            System.out.println("ID | Username\t| Full Name\t\t| Role\t| Login Info");
            System.out.println("----------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-2d | %-12s| %-20s| %-6s| %s/%s%n",
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("full_name"), 
                    rs.getString("role"),
                    rs.getString("username"),
                    getPasswordHint(rs.getString("username")));
            }
            
            System.out.println("\n LOGIN CREDENTIALS:");
            System.out.println("   admin/admin123 - Administrator account");
            System.out.println("   izhrr/izhrr123 - Your personal admin account");
            System.out.println("   testuser/password123 - Regular user account");
            System.out.println("   demo/demo123 - Demo account");
            System.out.println();
        }
    }
    
    private String getPasswordHint(String username) {
        return switch (username) {
            case "admin" -> "admin123";
            case "izhrr" -> "izhrr123";
            case "testuser" -> "password123";
            case "demo" -> "demo123";
            default -> "***";
        };
    }
}