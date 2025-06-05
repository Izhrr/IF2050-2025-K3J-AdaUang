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
            insertTestContracts();
            
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

        // Hapus SEMUA data dari tabel kontrak (child) terlebih dahulu!
        String sqlDeleteKontrak = "DELETE FROM kontrak";
        try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteKontrak)) {
            stmt.executeUpdate();
        }
        // Reset AUTO_INCREMENT ke 1 untuk kontrak
        String sqlResetKontrak = "ALTER TABLE kontrak AUTO_INCREMENT = 1";
        try (PreparedStatement stmt = conn.prepareStatement(sqlResetKontrak)) {
            stmt.executeUpdate();
        }

        // Baru kemudian hapus users (parent)
        String sqlDelete = "DELETE FROM " + config.getUsersTableName();
        try (PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {
            stmt.executeUpdate();
        }

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

    private void insertTestContracts() throws SQLException {
        Connection conn = dbConnection.getConnection();

        // Asumsi staff_id 1, 2, 3 sudah ada dari insertTestUsers
        String sql = "INSERT INTO kontrak (staff_id, loan_term, status, total_payment, loan_payment, remaining_installment, branch, customer_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Object[][] testContracts = {
                {1, 12, true, 12000000, 1000000, 12, "Jakarta", "Budi Santoso"},
                {2, 24, false, 48000000, 2000000, 0, "Bandung", "Siti Aminah"},
                {3, 6, true, 6000000, 1000000, 6, "Surabaya", "Agus Wijaya"}
            };

            for (Object[] kontrak : testContracts) {
                stmt.setInt(1, (Integer) kontrak[0]);
                stmt.setInt(2, (Integer) kontrak[1]);
                stmt.setBoolean(3, (Boolean) kontrak[2]);
                stmt.setInt(4, (Integer) kontrak[3]);
                stmt.setInt(5, (Integer) kontrak[4]);
                stmt.setInt(6, (Integer) kontrak[5]);
                stmt.setString(7, (String) kontrak[6]);
                stmt.setString(8, (String) kontrak[7]);
                stmt.addBatch();
            }
            stmt.executeBatch();
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