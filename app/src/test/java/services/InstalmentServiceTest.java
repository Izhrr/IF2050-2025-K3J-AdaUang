package services;

import models.Cicilan;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import database.DatabaseSeeder;

import java.time.LocalDate;
import java.util.List;
import java.sql.*;

public class InstalmentServiceTest {

    static InstalmentService instalmentService;

    @BeforeAll
    static void setup() {
        DatabaseSeeder seeder = new DatabaseSeeder();
        assertTrue(seeder.runSeeder(), "Seeder harus sukses dijalankan");
        instalmentService = new InstalmentService();
    }

    @Test
    void testGetAllCicilan() {
        List<Cicilan> cicilanList = instalmentService.getAllCicilan();
        assertNotNull(cicilanList, "Cicilan list tidak boleh null");
    }

    @Test
    void testAddCicilanSuccess() throws Exception {
        // Ambil kontrak yang belum lunas
        int idKontrak = 2; // sesuaikan dengan seeder
        int idStaff = 2;
        int tenorTerakhir = 0;
        int cicilanPerBulan = 0;
        Connection conn = database.DatabaseConnection.getInstance().getConnection();

        // Ambil tenor terakhir
        try (PreparedStatement stmt = conn.prepareStatement("SELECT COALESCE(MAX(tenor), 0) FROM cicilan WHERE id_kontrak = ?")) {
            stmt.setInt(1, idKontrak);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tenorTerakhir = rs.getInt(1);
            }
        }
        int nextTenor = tenorTerakhir + 1;

        // Ambil jumlah cicilan per bulan
        try (PreparedStatement stmt = conn.prepareStatement("SELECT cicilan_per_bulan FROM kontrak WHERE id_kontrak = ?")) {
            stmt.setInt(1, idKontrak);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cicilanPerBulan = rs.getInt("cicilan_per_bulan");
            }
        }

        boolean result = instalmentService.addCicilan(idKontrak, cicilanPerBulan, nextTenor, LocalDate.now(), idStaff);
        assertTrue(result, "Tambah cicilan harus berhasil");
    }

    @Test
    void testAddCicilanFailWrongAmount() throws Exception {
        int idKontrak = 2;
        int idStaff = 2;
        int tenorTerakhir = 0;
        Connection conn = database.DatabaseConnection.getInstance().getConnection();

        // Ambil tenor terakhir
        try (PreparedStatement stmt = conn.prepareStatement("SELECT COALESCE(MAX(tenor), 0) FROM cicilan WHERE id_kontrak = ?")) {
            stmt.setInt(1, idKontrak);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tenorTerakhir = rs.getInt(1);
            }
        }
        int nextTenor = tenorTerakhir + 1;

        // Ambil jumlah cicilan per bulan
        int cicilanPerBulan = 0;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT cicilan_per_bulan FROM kontrak WHERE id_kontrak = ?")) {
            stmt.setInt(1, idKontrak);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cicilanPerBulan = rs.getInt("cicilan_per_bulan");
            }
        }

        // pakai jumlah salah
        boolean result = instalmentService.addCicilan(idKontrak, cicilanPerBulan + 1000, nextTenor, LocalDate.now(), idStaff);
        assertFalse(result, "Tambah cicilan dengan jumlah salah harus gagal");
    }

    @Test
    void testAddCicilanFailWrongTenor() throws Exception {
        int idKontrak = 2;
        int idStaff = 2;
        int cicilanPerBulan = 0;

        // Ambil jumlah cicilan per bulan
        try (PreparedStatement stmt = database.DatabaseConnection.getInstance().getConnection().prepareStatement("SELECT cicilan_per_bulan FROM kontrak WHERE id_kontrak = ?")) {
            stmt.setInt(1, idKontrak);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cicilanPerBulan = rs.getInt("cicilan_per_bulan");
            }
        }

        // pakai tenor salah (misal: lompat)
        boolean result = instalmentService.addCicilan(idKontrak, cicilanPerBulan, 99, LocalDate.now(), idStaff);
        assertFalse(result, "Tambah cicilan dengan tenor salah harus gagal");
    }
}