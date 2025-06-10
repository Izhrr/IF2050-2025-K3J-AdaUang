package services;

import database.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import models.Cicilan; 

public class InstalmentService {
    private final DatabaseConnection dbConnection;

    public InstalmentService() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public boolean addCicilan(int idKontrak, int jumlah, int tenor, LocalDate tanggal, int idStaff) {
        Connection conn = dbConnection.getConnection();

        try {
            // 1. Validasi kontrak dan ambil data
            String queryKontrak = "SELECT jumlah_bayar, jumlah_bayar_bunga, total, cicilan_per_bulan, tenor FROM kontrak WHERE id_kontrak = ?";
            int jumlahBayarSebelumnya = 0;
            int jumlahBayarTotal = 0;
            int totalPinjaman = 0;
            int cicilanPerBulan = 0;
            int tenorMaksimal = 0; // Tambahan untuk tenor maksimal

            try (PreparedStatement stmt = conn.prepareStatement(queryKontrak)) {
                stmt.setInt(1, idKontrak);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    jumlahBayarSebelumnya = rs.getInt("jumlah_bayar");
                    jumlahBayarTotal = rs.getInt("jumlah_bayar_bunga");
                    totalPinjaman = rs.getInt("total");
                    cicilanPerBulan = rs.getInt("cicilan_per_bulan");
                    tenorMaksimal = rs.getInt("tenor"); // Ambil tenor maksimal dari kontrak
                } else {
                    System.err.println("Tidak ditemukan kontrak dengan ID: " + idKontrak);
                    return false;
                }
            }

            // 2. Validasi tenor tidak melebihi tenor maksimal kontrak
            if (tenor > tenorMaksimal) {
                System.err.println("INVALID: Tenor melebihi batas maksimal!");
                System.err.println("Tenor maksimal untuk kontrak ini: " + tenorMaksimal);
                System.err.println("Tenor yang dimasukkan: " + tenor);
                return false;
            }

            // 3. Validasi apakah tenor sudah pernah dibayar
            String queryTenor = "SELECT COUNT(*) FROM cicilan WHERE id_kontrak = ? AND tenor = ?";
            try (PreparedStatement stmt = conn.prepareStatement(queryTenor)) {
                stmt.setInt(1, idKontrak);
                stmt.setInt(2, tenor);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.err.println("INVALID: Tenor ke-" + tenor + " untuk kontrak ID " + idKontrak + " sudah pernah dibayar!");
                    return false;
                }
            }

            // 4. Validasi urutan tenor (harus tenor terakhir + 1)
            String queryTenorTerakhir = "SELECT COALESCE(MAX(tenor), 0) FROM cicilan WHERE id_kontrak = ?";
            int tenorTerakhir = 0;
            try (PreparedStatement stmt = conn.prepareStatement(queryTenorTerakhir)) {
                stmt.setInt(1, idKontrak);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    tenorTerakhir = rs.getInt(1);
                }
            }

            int tenorYangHarusDibayar = tenorTerakhir + 1;
            if (tenor != tenorYangHarusDibayar) {
                System.err.println("INVALID: Tenor harus dibayar secara berurutan!");
                System.err.println("Tenor terakhir yang dibayar: " + tenorTerakhir);
                System.err.println("Tenor yang harus dibayar sekarang: " + tenorYangHarusDibayar);
                System.err.println("Tenor yang dimasukkan: " + tenor);
                return false;
            }

            // 5. Validasi apakah kontrak sudah lunas (semua tenor telah dibayar)
            if (tenorTerakhir >= tenorMaksimal) {
                System.err.println("INVALID: Kontrak sudah lunas!");
                System.err.println("Semua tenor (1-" + tenorMaksimal + ") sudah dibayar.");
                return false;
            }

            // 6. Validasi jumlah cicilan
            if (jumlah != cicilanPerBulan) {
                System.err.println("INVALID: Jumlah cicilan tidak sesuai!");
                System.err.println("Jumlah yang dimasukkan: " + jumlah);
                System.err.println("Jumlah cicilan yang seharusnya: " + cicilanPerBulan);
                return false;
            }

            // 7. Update jumlah_bayar di kontrak (MENAMBAHKAN jumlah cicilan)
            int jumlahBaru = jumlahBayarSebelumnya + jumlah;
            
            // Cek status: jika jumlah bayar >= total pinjaman maka lunas (1), jika belum maka aktif (0)
            boolean statusLunas = jumlahBaru >= totalPinjaman;
            int statusValue = statusLunas ? 1 : 0;
            
            String updateKontrak = "UPDATE kontrak SET jumlah_bayar = ?, status = ? WHERE id_kontrak = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateKontrak)) {
                stmt.setInt(1, jumlahBaru);
                stmt.setInt(2, statusValue); // 1 = lunas, 0 = belum lunas
                stmt.setInt(3, idKontrak);
                stmt.executeUpdate();
            }

            // 8. Insert ke tabel cicilan
            String insertCicilan = "INSERT INTO cicilan (id_kontrak, tenor, jumlah_cicilan, tanggal_cicilan, id_staff) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertCicilan)) {
                stmt.setInt(1, idKontrak);
                stmt.setInt(2, tenor);
                stmt.setInt(3, jumlah);
                stmt.setDate(4, Date.valueOf(tanggal));
                stmt.setInt(5, idStaff);
                stmt.executeUpdate();
            }

            if (statusLunas) {
                System.out.println("Selamat! Kontrak telah LUNAS. Total terbayar: " + jumlahBaru + " dari total pinjaman: " + totalPinjaman);
            } else {
                System.out.println("Cicilan berhasil ditambahkan. Jumlah bayar sekarang: " + jumlahBaru + " dari total pinjaman: " + totalPinjaman);
            }
            
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Cicilan> getAllCicilan() {
        List<Cicilan> cicilanList = new ArrayList<>();
        Connection conn = dbConnection.getConnection();
        
        try {
            // Coba query sederhana dulu tanpa JOIN
            String sql = "SELECT * FROM cicilan ORDER BY id_kontrak ASC, tanggal_cicilan DESC";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                
                while (rs.next()) {
                    Cicilan cicilan = new Cicilan(
                        rs.getInt("id_cicilan"),
                        rs.getInt("id_kontrak"),
                        rs.getInt("tenor"),
                        rs.getInt("jumlah_cicilan"),
                        rs.getDate("tanggal_cicilan").toLocalDate(),
                        rs.getInt("id_staff")
                    );
                    cicilanList.add(cicilan);
                    System.out.println("Added cicilan: ID=" + rs.getInt("id_cicilan") + ", Kontrak=" + rs.getInt("id_kontrak"));
                }
            }
            
            System.out.println("Found " + cicilanList.size() + " cicilan records in database");
            
        } catch (SQLException e) {
            System.err.println("Error mengambil data cicilan: " + e.getMessage());
            e.printStackTrace();
        }
        
        return cicilanList;
    }
}
