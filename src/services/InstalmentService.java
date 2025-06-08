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

    public boolean addCicilan(int idKontrak, int jumlah, LocalDate tanggal) {
        Connection conn = dbConnection.getConnection();

        try {
            // 1. Ambil jumlah bayar dan total yang harus dibayar (dengan bunga)
            String queryKontrak = "SELECT jumlah_bayar, jumlah_bayar_bunga FROM kontrak WHERE id_kontrak = ?";
            int jumlahBayarSebelumnya = 0;
            int jumlahBayarBunga = 0;

            try (PreparedStatement stmt = conn.prepareStatement(queryKontrak)) {
                stmt.setInt(1, idKontrak);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    jumlahBayarSebelumnya = rs.getInt("jumlah_bayar");
                    jumlahBayarBunga = rs.getInt("jumlah_bayar_bunga");
                } else {
                    System.err.println("Tidak ditemukan kontrak dengan ID: " + idKontrak);
                    return false;
                }
            }

            // 2. Update jumlah_bayar
            int jumlahBaru = jumlahBayarSebelumnya + jumlah;
            String updateBayar = "UPDATE kontrak SET jumlah_bayar = ? WHERE id_kontrak = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateBayar)) {
                stmt.setInt(1, jumlahBaru);
                stmt.setInt(2, idKontrak);
                stmt.executeUpdate();
            }

            // 3. Tambahkan cicilan ke tabel cicilan
            String insertCicilan = "INSERT INTO cicilan (id_kontrak, jumlah_cicilan, tanggal_cicilan) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertCicilan)) {
                stmt.setInt(1, idKontrak);
                stmt.setInt(2, jumlah);
                stmt.setDate(3, Date.valueOf(tanggal));
                stmt.executeUpdate();
            }

            // 4. Jika lunas, update status kontrak jadi aktif -> tidak aktif (status = 1)
            if (jumlahBaru >= jumlahBayarBunga) {
                String updateStatus = "UPDATE kontrak SET status = 1 WHERE id_kontrak = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateStatus)) {
                    stmt.setInt(1, idKontrak);
                    stmt.executeUpdate();
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
