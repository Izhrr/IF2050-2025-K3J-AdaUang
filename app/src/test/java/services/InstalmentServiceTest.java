package services;

import models.Instalment;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import database.DatabaseSeeder;

import java.time.LocalDate;
import java.util.List;
import java.sql.*;

public class InstalmentServiceTest {

    static InstalmentService instalmentService;
    static int idKontrakAktif;
    static int idStaffAktif;
    static int cicilanPerBulan;

    @BeforeAll
    static void setup() throws Exception {
        DatabaseSeeder seeder = new DatabaseSeeder();
        assertTrue(seeder.runSeeder(), "Seeder harus sukses dijalankan");
        instalmentService = new InstalmentService();

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
        // Jika tidak ada kontrak aktif, test akan di-skip
    }

    @Test
    void testGetAllInstalments() {
        List<Instalment> instalments = instalmentService.getAllInstalments();
        assertNotNull(instalments, "Instalment list tidak boleh null");
    }

    @Test
    void testCreateInstalmentSuccess() throws Exception {
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

        boolean result = instalmentService.createInstalment(
            idKontrakAktif,
            cicilanPerBulan,
            nextTenor,
            tanggalCicilan,
            idStaffAktif
        );
        assertTrue(result, "Tambah cicilan harus berhasil (pastikan kontrak aktif dan tenor valid)");
    }

    @Test
    void testCreateInstalmentFailWrongAmount() throws Exception {
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

        boolean result = instalmentService.createInstalment(
            idKontrakAktif,
            cicilanPerBulan + 1234, // jumlah salah
            nextTenor,
            tanggalCicilan,
            idStaffAktif
        );
        assertFalse(result, "Tambah cicilan dengan jumlah salah harus gagal");
    }

    @Test
    void testCreateInstalmentFailWrongTenor() throws Exception {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");

        // Ambil jumlah cicilan per bulan saja, tenor lompat
        boolean result = instalmentService.createInstalment(
            idKontrakAktif,
            cicilanPerBulan,
            99, // tenor lompat
            LocalDate.now(),
            idStaffAktif
        );
        assertFalse(result, "Tambah cicilan dengan tenor salah harus gagal");
    }

    @Test
    void testGetInstalmentsByContract() {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");
        List<Instalment> list = instalmentService.getInstalmentsByContract(idKontrakAktif);
        assertNotNull(list, "Instalment by contract tidak boleh null");
    }

    @Test
    void testGetNextTenor() {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");
        int nextTenor = instalmentService.getNextTenor(idKontrakAktif);
        assertTrue(nextTenor >= 1, "Next tenor minimal 1");
    }

    @Test
    void testGetLastPaymentDate() {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");
        LocalDate last = instalmentService.getLastPaymentDate(idKontrakAktif);
        assertTrue(last == null || last.isBefore(LocalDate.now()) || last.isEqual(LocalDate.now()));
    }

    @Test
    void testCanPayInstalment() {
        assumeTrue(idKontrakAktif > 0, "Tidak ada kontrak aktif di DB");
        boolean canPay = instalmentService.canPayInstalment(idKontrakAktif, 1, LocalDate.now());
        assertNotNull(canPay);
    }
}