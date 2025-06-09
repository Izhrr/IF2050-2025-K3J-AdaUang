package database;

import config.DatabaseConfig;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSeeder {
    private DatabaseConfig config;
    private DatabaseConnection dbConnection;
    private final List<Integer> insertedContractIds = new ArrayList<>();

    public DatabaseSeeder() {
        config = DatabaseConfig.getInstance();
        dbConnection = DatabaseConnection.getInstance();
    }

    public boolean runSeeder() {
        try {
            if (config.isDebugMode()) {
                System.out.println(" Starting database seeding...");
            }

            clearTestData();
            insertTestUsers();
            insertTestContracts();
            insertTestInstalment();

            if (config.isDebugMode()) {
                System.out.println(" Database seeding completed successfully!");
                printSeededData();
                printSeededInstalment();
                printSeededContract();
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

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM cicilan");
            stmt.executeUpdate("ALTER TABLE cicilan AUTO_INCREMENT = 1");
            stmt.executeUpdate("DELETE FROM kontrak");
            stmt.executeUpdate("ALTER TABLE kontrak AUTO_INCREMENT = 1");
            stmt.executeUpdate("DELETE FROM " + config.getUsersTableName());
            stmt.executeUpdate("ALTER TABLE " + config.getUsersTableName() + " AUTO_INCREMENT = 1");
        }
    }

    private void insertTestUsers() throws SQLException {
        Connection conn = dbConnection.getConnection();
        String sql = "INSERT INTO " + config.getUsersTableName() +
                " (username, fullname, password, branch, role) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Object[][] testUsers = {
                    {"admin", "Administrator", "admin123", "Jakarta", "admin"},
                    {"izhrr", "Izhrr - Project Lead", "izhrr123", "Bandung", "admin"},
                    {"testuser", "Test User", "password123", "Surabaya", "staff"},
                    {"demo", "Demo Account", "demo123", "Jakarta", "manager"}
            };

            for (Object[] user : testUsers) {
                stmt.setString(1, (String) user[0]);
                stmt.setString(2, (String) user[1]);
                stmt.setString(3, (String) user[2]);
                stmt.setString(4, (String) user[3]);
                stmt.setString(5, (String) user[4]);
                stmt.addBatch();
            }

            stmt.executeBatch();
        }
    }

    private void insertTestContracts() throws SQLException {
        Connection conn = dbConnection.getConnection();
        String sql = "INSERT INTO kontrak (nama_user, total, tenor, jumlah_bayar, jumlah_bayar_bunga, cicilan_per_bulan, status, tanggal_pinjam, id_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Object[][] testContracts = {
                    {"Andi Setiawan", 12000000, 12, 8800000, true, LocalDate.of(2024, 12, 1), 1},
                    {"Budi Santoso", 24000000, 24, 8800000, true, LocalDate.of(2024, 10, 15), 1},
                    {"Siti Aminah", 18000000, 18, 5940000, true, LocalDate.of(2024, 9, 1), 2},
                    {"Dedi Kurniawan", 15000000, 15, 4950000, true, LocalDate.of(2024, 7, 10), 2},
                    {"Eka Pratama", 6000000, 6, 6600000, false, LocalDate.of(2024, 6, 1), 3},
                    {"Farah Nabila", 9000000, 9, 5940000, true, LocalDate.of(2025, 4, 1), 3},
            };

            for (Object[] kontrak : testContracts) {
                int total = (Integer) kontrak[1];
                int tenor = (Integer) kontrak[2];
                int jumlah_bayar_bunga = (int) Math.round(total * 1.1);
                int cicilan_per_bulan = tenor != 0 ? jumlah_bayar_bunga / tenor : 0;
                int jumlah_bayar = (Integer) kontrak[3];

                stmt.setString(1, (String) kontrak[0]);
                stmt.setInt(2, total);
                stmt.setInt(3, tenor);
                stmt.setInt(4, jumlah_bayar);
                stmt.setInt(5, jumlah_bayar_bunga);
                stmt.setInt(6, cicilan_per_bulan);
                stmt.setBoolean(7, (Boolean) kontrak[4]);
                stmt.setDate(8, Date.valueOf((LocalDate) kontrak[5]));
                stmt.setInt(9, (Integer) kontrak[6]);

                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        insertedContractIds.add(rs.getInt(1));
                    }
                }
            }
        }
    }

    private void insertTestInstalment() throws SQLException {
        String sql = "INSERT INTO cicilan (id_kontrak, jumlah_cicilan, tanggal_cicilan, id_staff) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql)) {

        // Kontrak 1 (Andi) - Tunggakan 1 bulan
        // Kontrak mulai Des 2024, bayar sampai Apr 2025, current Juni 2025 = telat 1 bulan
        int[] cicilanAndi = {1100000, 1100000, 1100000, 1100000, 1100000}; // Des-Apr
        LocalDate[] tanggalAndi = {
            LocalDate.of(2024, 12, 1), LocalDate.of(2025, 1, 1), 
            LocalDate.of(2025, 2, 1), LocalDate.of(2025, 3, 1), 
            LocalDate.of(2025, 4, 1)
        };
        
        for (int i = 0; i < cicilanAndi.length; i++) {
            stmt.setInt(1, 1); // id_kontrak Andi
            stmt.setInt(2, cicilanAndi[i]);
            stmt.setDate(3, Date.valueOf(tanggalAndi[i]));
            stmt.setInt(4, 1); // id_staff
            stmt.executeUpdate();
        }

        // Kontrak 2 (Budi) - Tunggakan 2 bulan  
        // Kontrak mulai Okt 2024, bayar sampai Mar 2025, current Juni 2025 = telat 2 bulan
        int[] cicilanBudi = {1100000, 1100000, 1100000, 1100000, 1100000, 1100000}; // Okt-Mar
        LocalDate[] tanggalBudi = {
            LocalDate.of(2024, 10, 15), LocalDate.of(2024, 11, 15),
            LocalDate.of(2024, 12, 15), LocalDate.of(2025, 1, 15),
            LocalDate.of(2025, 2, 15), LocalDate.of(2025, 3, 15)
        };
        
        for (int i = 0; i < cicilanBudi.length; i++) {
            stmt.setInt(1, 2); // id_kontrak Budi
            stmt.setInt(2, cicilanBudi[i]);
            stmt.setDate(3, Date.valueOf(tanggalBudi[i]));
            stmt.setInt(4, 1); // id_staff
            stmt.executeUpdate();
        }

        // Kontrak 3 (Siti) - Tunggakan 3 bulan
        // Kontrak mulai Sep 2024, bayar sampai Feb 2025, current Juni 2025 = telat 3 bulan
        int[] cicilanSiti = {1100000, 1100000, 1100000, 1100000, 1100000, 1100000}; // Sep-Feb
        LocalDate[] tanggalSiti = {
            LocalDate.of(2024, 9, 1), LocalDate.of(2024, 10, 1),
            LocalDate.of(2024, 11, 1), LocalDate.of(2024, 12, 1),
            LocalDate.of(2025, 1, 1), LocalDate.of(2025, 2, 1)
        };
        
        for (int i = 0; i < cicilanSiti.length; i++) {
            stmt.setInt(1, 3); // id_kontrak Siti
            stmt.setInt(2, cicilanSiti[i]);
            stmt.setDate(3, Date.valueOf(tanggalSiti[i]));
            stmt.setInt(4, 2); // id_staff
            stmt.executeUpdate();
        }

        // Kontrak 4 (Dedi) - Tunggakan > 3 bulan
        // Kontrak mulai Jul 2024, bayar sampai Jan 2025, current Juni 2025 = telat 4 bulan
        int[] cicilanDedi = {1100000, 1100000, 1100000, 1100000, 1100000}; // Jul-Nov
        LocalDate[] tanggalDedi = {
            LocalDate.of(2024, 7, 10), LocalDate.of(2024, 8, 10),
            LocalDate.of(2024, 9, 10), LocalDate.of(2024, 10, 10),
            LocalDate.of(2024, 11, 10)
        };
        
        for (int i = 0; i < cicilanDedi.length; i++) {
            stmt.setInt(1, 4); // id_kontrak Dedi
            stmt.setInt(2, cicilanDedi[i]);
            stmt.setDate(3, Date.valueOf(tanggalDedi[i]));
            stmt.setInt(4, 2); // id_staff
            stmt.executeUpdate();
        }

        // Kontrak 5 (Eka) - Sudah lunas (semua cicilan dibayar)
        int[] cicilanEka = {1100000, 1100000, 1100000, 1100000, 1100000, 1100000}; // 6 cicilan penuh
        LocalDate[] tanggalEka = {
            LocalDate.of(2024, 6, 1), LocalDate.of(2024, 7, 1),
            LocalDate.of(2024, 8, 1), LocalDate.of(2024, 9, 1),
            LocalDate.of(2024, 10, 1), LocalDate.of(2024, 11, 1)
        };
        
        for (int i = 0; i < cicilanEka.length; i++) {
            stmt.setInt(1, 5); // id_kontrak Eka
            stmt.setInt(2, cicilanEka[i]);
            stmt.setDate(3, Date.valueOf(tanggalEka[i]));
            stmt.setInt(4, 3); // id_staff
            stmt.executeUpdate();
        }

        // Kontrak 6 (Farah) - Aktif tanpa tunggakan
        int[] cicilanFarah = {1100000, 1100000}; // Apr-Mei 2025 (current Juni, jadi tidak telat)
        LocalDate[] tanggalFarah = {
            LocalDate.of(2025, 4, 1), LocalDate.of(2025, 5, 1)
        };
        
        for (int i = 0; i < cicilanFarah.length; i++) {
            stmt.setInt(1, 6); // id_kontrak Farah
            stmt.setInt(2, cicilanFarah[i]);
            stmt.setDate(3, Date.valueOf(tanggalFarah[i]));
            stmt.setInt(4, 3); // id_staff
            stmt.executeUpdate();
            }
        }
    }

    private void printSeededData() throws SQLException {
        Connection conn = dbConnection.getConnection();
        String sql = "SELECT id_user, username, fullname, branch, role FROM " +
                config.getUsersTableName() + " ORDER BY id_user ASC LIMIT 10";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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
        }
    }

    private void printSeededInstalment() throws SQLException {
        Connection conn = dbConnection.getConnection();
        String sql = """
                SELECT c.id_cicilan, c.id_kontrak, k.nama_user, c.jumlah_cicilan, c.tanggal_cicilan
                FROM cicilan c
                JOIN kontrak k ON c.id_kontrak = k.id_kontrak
                ORDER BY c.id_cicilan ASC
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n SEEDED INSTALMENT DATA:");
            System.out.println("ID | ID Kontrak | Nama Peminjam       | Jumlah Cicilan | Tanggal Cicilan");
            System.out.println("------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-2d | %-10d | %-20s | Rp%-13d | %s%n",
                        rs.getInt("id_cicilan"),
                        rs.getInt("id_kontrak"),
                        rs.getString("nama_user"),
                        rs.getInt("jumlah_cicilan"), // kolom database tetap sama
                        rs.getDate("tanggal_cicilan").toString()
                );
            }
        }
    }

    private void printSeededContract() throws SQLException {
        Connection conn = dbConnection.getConnection();
        String sql = """
            SELECT k.id_kontrak, k.nama_user, k.total, k.tenor, k.jumlah_bayar_bunga, k.cicilan_per_bulan, k.status, k.tanggal_pinjam, u.fullname
            FROM kontrak k
            JOIN %s u ON k.id_user = u.id_user
            ORDER BY k.id_kontrak ASC
            """.formatted(config.getUsersTableName());

        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n SEEDED CONTRACT DATA:");
            System.out.println("ID | Nama Peminjam       | Total Pinjaman | Tenor | Total Bayar | Cicilan/Bulan | Status   | Tanggal Pinjam");
            System.out.println("-----------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-2d | %-20s | Rp%-13d | %-5d | Rp%-11d | Rp%-13d | %-8s | %s%n",
                    rs.getInt("id_kontrak"),
                    rs.getString("nama_user"),
                    rs.getInt("total"),
                    rs.getInt("tenor"),
                    rs.getInt("jumlah_bayar_bunga"),
                    rs.getInt("cicilan_per_bulan"),
                    rs.getBoolean("status") ? "LUNAS" : "BELUM",
                    rs.getDate("tanggal_pinjam").toString()
                );
            }
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

