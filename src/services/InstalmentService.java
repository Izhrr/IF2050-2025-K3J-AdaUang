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
            String queryKontrak = "SELECT jumlah_bayar, jumlah_bayar_bunga FROM kontrak WHERE id_kontrak = ?";
            int jumlahBayarSebelumnya = 0;
            int jumlahBayarTotal = 0;

            try (PreparedStatement stmt = conn.prepareStatement(queryKontrak)) {
                stmt.setInt(1, idKontrak);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    jumlahBayarSebelumnya = rs.getInt("jumlah_bayar");
                    jumlahBayarTotal = rs.getInt("jumlah_bayar_bunga");
                } else {
                    System.err.println("Tidak ditemukan kontrak dengan ID: " + idKontrak);
                    return false;
                }
            }

            // 2. Update jumlah_bayar di kontrak
            int jumlahBaru = jumlahBayarSebelumnya + jumlah;
            String updateKontrak = "UPDATE kontrak SET jumlah_bayar = ?, status = ? WHERE id_kontrak = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateKontrak)) {
                boolean lunas = jumlahBaru >= jumlahBayarTotal;
                stmt.setInt(1, jumlahBaru);
                stmt.setBoolean(2, !lunas ? true : false); // jika lunas, maka status menjadi false (tidak aktif)
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

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
