package controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import database.DatabaseSeeder;

import java.time.LocalDate;

import java.sql.*;

public class InstalmentControllerTest {

    static InstalmentController instalmentController;

    @BeforeAll
    static void setup() {
        DatabaseSeeder seeder = new DatabaseSeeder();
        assertTrue(seeder.runSeeder(), "Seeder harus sukses dijalankan");
        instalmentController = new InstalmentController();
    }

    @Test
    void testTambahCicilanSuccess() throws Exception {
        // Cari kontrak yang BELUM LUNAS (misal: Siti Aminah id_kontrak=2, tenor seeding: 3/24)
        int idKontrak = 2; // Pastikan ini id kontrak Siti Aminah dari seeder
        int idStaff = 2;   // Staff sesuai seeder
        
        // Ambil tenor terakhir
        int tenorTerakhir = 0;
        int cicilanPerBulan = 0;
        Connection conn = database.DatabaseConnection.getInstance().getConnection();

        // Dapatkan tenor terakhir
        try (PreparedStatement stmt = conn.prepareStatement("SELECT COALESCE(MAX(tenor), 0) FROM cicilan WHERE id_kontrak = ?")) {
            stmt.setInt(1, idKontrak);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tenorTerakhir = rs.getInt(1);
            }
        }
        int nextTenor = tenorTerakhir + 1;

        // Dapatkan jumlah cicilan per bulan
        try (PreparedStatement stmt = conn.prepareStatement("SELECT cicilan_per_bulan FROM kontrak WHERE id_kontrak = ?")) {
            stmt.setInt(1, idKontrak);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cicilanPerBulan = rs.getInt("cicilan_per_bulan");
            }
        }

        // Test tambah cicilan di tenor berikutnya
        boolean result = instalmentController.tambahCicilan(idKontrak, cicilanPerBulan, nextTenor, LocalDate.now(), idStaff);
        assertTrue(result, "Tambah cicilan harus berhasil");
    }
}