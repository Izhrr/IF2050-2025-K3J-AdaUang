package services;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class InstalmentService {
    private final DatabaseConnection dbConnection;

    public InstalmentService() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public boolean addCicilan(int idKontrak, int jumlah, LocalDate tanggal, int idStaff) {
        Connection conn = dbConnection.getConnection();

        try {
            // 1. Validasi kontrak
            String queryKontrak = "SELECT jumlah_bayar, jumlah_bayar_bunga, total FROM kontrak WHERE id_kontrak = ?";
            int jumlahBayarSebelumnya = 0;
            int jumlahBayarTotal = 0;
            int totalPinjaman = 0;

            try (PreparedStatement stmt = conn.prepareStatement(queryKontrak)) {
                stmt.setInt(1, idKontrak);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    jumlahBayarSebelumnya = rs.getInt("jumlah_bayar");
                    jumlahBayarTotal = rs.getInt("jumlah_bayar_bunga");
                    totalPinjaman = rs.getInt("total");
                } else {
                    System.err.println("Tidak ditemukan kontrak dengan ID: " + idKontrak);
                    return false;
                }
            }

            // 2. Update jumlah_bayar di kontrak (MENAMBAHKAN jumlah cicilan)
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

            // 3. Insert ke tabel cicilan
            String insertCicilan = "INSERT INTO cicilan (id_kontrak, jumlah_cicilan, tanggal_cicilan, id_staff) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertCicilan)) {
                stmt.setInt(1, idKontrak);
                stmt.setInt(2, jumlah);
                stmt.setDate(3, Date.valueOf(tanggal));
                stmt.setInt(4, idStaff);
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
}
