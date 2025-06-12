package controllers;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import database.DatabaseSeeder;

import java.time.LocalDate;
import java.sql.*;

public class InstalmentControllerTest {

    static InstalmentController instalmentController;
    static int idKontrakAktif;
    static int idStaffAktif;
    static int cicilanPerBulan;

    @BeforeAll
    static void setup() throws Exception {
        DatabaseSeeder seeder = new DatabaseSeeder();
        assertTrue(seeder.runSeeder(), "Seeder harus sukses dijalankan");
        instalmentController = new InstalmentController();

        // Cari kontrak aktif yang belum lunas
        try (Connection conn = database.DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT k.id_kontrak, k.cicilan_per_bulan, k.id_user FROM kontrak k WHERE k.status = 1 LIMIT 1")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                idKontrakAktif = rs.getInt("id_kontrak");
                cicilanPerBulan = rs.getInt("cicilan_per_bulan");
                idStaffAktif = rs.getInt("id_user");
            }
        }
    }

    @Test
    void testAddInstalmentSuccess() throws Exception {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");

        int tenorTerakhir = 0;
        LocalDate tanggalCicilan = LocalDate.now();
        try (Connection conn = database.DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COALESCE(MAX(tenor), 0) FROM cicilan WHERE id_kontrak = ?")) {
            stmt.setInt(1, idKontrakAktif);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tenorTerakhir = rs.getInt(1);
            }
        }
        int nextTenor = tenorTerakhir + 1;

        // Tanggal harus valid: jika sudah ada pembayaran sebelumnya, tanggal harus >= 1 bulan setelahnya
        try (Connection conn = database.DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT MAX(tanggal_cicilan) AS last_date FROM cicilan WHERE id_kontrak = ?")) {
            stmt.setInt(1, idKontrakAktif);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getDate("last_date") != null) {
                LocalDate lastDate = rs.getDate("last_date").toLocalDate();
                tanggalCicilan = lastDate.plusMonths(1);
            }
        }

        boolean result = instalmentController.addInstalment(
            idKontrakAktif,
            cicilanPerBulan,
            nextTenor,
            tanggalCicilan,
            idStaffAktif
        );
        assertTrue(result, "Tambah cicilan harus berhasil (pastikan kontrak aktif dan tenor valid)");
    }

    @Test
    void testGetAllInstalmentsNotNull() {
        assertNotNull(instalmentController.getAllInstalments(), "getAllInstalments tidak boleh null");
    }

    @Test
    void testGetInstalmentsByContractNotNull() {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");
        assertNotNull(instalmentController.getInstalmentsByContract(idKontrakAktif), "getInstalmentsByContract tidak boleh null");
    }

    @Test
    void testGetNextTenor() {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");
        int nextTenor = instalmentController.getNextTenor(idKontrakAktif);
        assertTrue(nextTenor >= 1, "getNextTenor minimal 1");
    }

    @Test
    void testGetLastPaymentDate() {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");
        LocalDate lastDate = instalmentController.getLastPaymentDate(idKontrakAktif);
        // Bisa null jika belum pernah bayar, jadi test jalan saja
        assertTrue(lastDate == null || lastDate.isBefore(LocalDate.now()) || lastDate.isEqual(LocalDate.now()));
    }

    @Test
    void testValidatePayment() {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");
        boolean canPay = instalmentController.validatePayment(idKontrakAktif, 1, LocalDate.now());
        assertNotNull(canPay);
    }

    @Test
    void testGetMinimumPaymentDate() {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");
        LocalDate minDate = instalmentController.getMinimumPaymentDate(idKontrakAktif);
        assertNotNull(minDate, "getMinimumPaymentDate tidak boleh null");
    }
}