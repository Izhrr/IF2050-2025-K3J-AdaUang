package database;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

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
                    " (username, fullname, password, branch, role) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Test accounts for development
            Object[][] testUsers = {
                {"admin", "Administrator", "admin123", "Jakarta", "admin"},
                {"izhrr", "Izhrr - Project Lead", "izhrr123", "Bandung", "admin"},
                {"testuser", "Test User", "password123", "Surabaya", "staff"}, 
                {"demo", "Demo Account", "demo123", "Jakarta", "manager"}
            };
            
            for (Object[] user : testUsers) {
                stmt.setString(1, (String) user[0]); // username
                stmt.setString(2, (String) user[1]); // fullname
                stmt.setString(3, (String) user[2]); // password
                stmt.setString(4, (String) user[3]); // branch
                stmt.setString(5, (String) user[4]); // role
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

        String sql = "INSERT INTO kontrak (nama_user, total, tenor, jumlah_bayar, jumlah_bayar_bunga, cicilan_per_bulan, status, tanggal_pinjam, id_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Object[][] testContracts = {
                {"Budi Santoso", 12000000, 12, 1000000, true, LocalDate.of(2024, 12, 1), 1},
                {"Siti Aminah", 48000000, 24, 2000000, false, LocalDate.of(2024, 11, 15), 2},
                {"Agus Wijaya", 6000000, 6, 1000000, true, LocalDate.of(2025, 1, 10), 3}
            };

            for (Object[] kontrak : testContracts) {
                int total = (Integer) kontrak[1];
                int tenor = (Integer) kontrak[2];
                int jumlah_bayar_bunga = (int) Math.round(total * 1.1);
                int cicilan_per_bulan = tenor != 0 ? jumlah_bayar_bunga / tenor : 0;
                int jumlah_bayar = (Integer) kontrak[3];

                stmt.setString(1, (String) kontrak[0]); // nama_user
                stmt.setInt(2, total); // total
                stmt.setInt(3, tenor); // tenor
                stmt.setInt(4, jumlah_bayar); // jumlah_bayar
                stmt.setInt(5, jumlah_bayar_bunga); // jumlah_bayar_bunga
                stmt.setInt(6, cicilan_per_bulan); // cicilan_per_bulan
                stmt.setBoolean(7, (Boolean) kontrak[4]); // status
                stmt.setDate(8, Date.valueOf((LocalDate) kontrak[5])); // tgl_pinjam
                stmt.setInt(9, (Integer) kontrak[6]); // id_user
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void insertTestInstalment() throws SQLException {
        Connection conn = dbConnection.getConnection();

        String sql = "INSERT INTO cicilan (id_cicilan, id_kontrak, jumlah_cicilan, tanggal_cicilan) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Object[][] testInstalment = {
                {1, 2, 1100000, LocalDate.of(2025, 5, 10), 1},
                {3, 4, 1100000, LocalDate.of(2025, 7, 15), 2},
                {5, 6, 1100000, LocalDate.of(2025, 9, 20), 3},
            };

            for (Object[] cicilan : testInstalment) {
                int id_cicilan = (Integer) cicilan[0];
                int id_kontrak = (Integer) cicilan[1];
                int jumlah_cicilan = (Integer) cicilan[2];
                LocalDate tanggal_cicilan = (LocalDate) cicilan[3];

                stmt.setInt(1, id_cicilan); // id cicilan
                stmt.setInt(2, id_kontrak); // id kontrak
                stmt.setInt(3, jumlah_cicilan); // cicilan
                stmt.setDate(4, Date.valueOf(tanggal_cicilan)); // tanggal_cicilan
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
    
    private void printSeededData() throws SQLException {
        Connection conn = dbConnection.getConnection();
        
        String sql = "SELECT id_user, username, fullname, branch, role FROM " + 
                    config.getUsersTableName() + " ORDER BY id_user ASC LIMIT 10";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            
            System.out.println("\n SEEDED TEST ACCOUNTS:");
            System.out.println("ID | Username\t| Full Name\t\t| Branch\t| Role\t| Login Info");
            System.out.println("-------------------------------------------------------------------------------");
            
            while (rs.next()) {
                System.out.printf("%-2d | %-12s| %-20s| %-10s| %-6s| %s/%s%n",
                    rs.getInt("id_user"),
                    rs.getString("username"),
                    rs.getString("fullname"), 
                    rs.getString("branch"),
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