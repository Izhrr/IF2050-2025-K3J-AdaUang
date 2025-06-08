package models;

import java.sql.*;
import java.util.*;

public class Instalment extends BaseModel {
    private int id_cicilan;
    private int id_kontrak;
    private int jumlah_membayar;
    private Date tanggal_membayar;

    public Instalment() {}

    @Override
    public boolean save() {
        String sql = "INSERT INTO cicilan (id_kontrak, jumlah_membayar, tanggal_membayar) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, id_kontrak);
            stmt.setInt(2, jumlah_membayar);
            stmt.setDate(3, new java.sql.Date(tanggal_membayar.getTime()));

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.id_cicilan = rs.getInt(1);
                        this.id = this.id_cicilan;
                    }
                }
                updateContractPembayaran();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateContractPembayaran() {
        Contract kontrak = Contract.findById(this.id_kontrak);
        if (kontrak != null) {
            int totalBaru = kontrak.getJumlah_bayar() + this.jumlah_membayar;
            kontrak.setJumlah_bayar(totalBaru);
            if (totalBaru >= kontrak.getJumlah_bayar_bunga()) {
                kontrak.setStatus(false); // Lunas
            }
            kontrak.save();
        }
    }

    public static List<Instalment> findByContractId(int id_kontrak) {
        List<Instalment> list = new ArrayList<>();
        String sql = "SELECT * FROM cicilan WHERE id_kontrak = ? ORDER BY tanggal_membayar ASC";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_kontrak);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Instalment i = new Instalment();
                    i.setId_cicilan(rs.getInt("id_cicilan"));
                    i.setId_kontrak(rs.getInt("id_kontrak"));
                    i.setJumlah_membayar(rs.getInt("jumlah_membayar"));
                    i.setTanggal_membayar(rs.getDate("tanggal_membayar"));
                    list.add(i);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Instalment findById(int id) {
        String sql = "SELECT * FROM cicilan WHERE id_cicilan = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Instalment i = new Instalment();
                    i.setId_cicilan(rs.getInt("id_cicilan"));
                    i.setId_kontrak(rs.getInt("id_kontrak"));
                    i.setJumlah_membayar(rs.getInt("jumlah_membayar"));
                    i.setTanggal_membayar(rs.getDate("tanggal_membayar"));
                    return i;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Getters & Setters
    public int getId_cicilan() { return id_cicilan; }
    public void setId_cicilan(int id_cicilan) { this.id_cicilan = id_cicilan; }

    public int getId_kontrak() { return id_kontrak; }
    public void setId_kontrak(int id_kontrak) { this.id_kontrak = id_kontrak; }

    public int getJumlah_membayar() { return jumlah_membayar; }
    public void setJumlah_membayar(int jumlah_membayar) { this.jumlah_membayar = jumlah_membayar; }

    public Date getTanggal_membayar() { return tanggal_membayar; }
    public void setTanggal_membayar(Date tanggal_membayar) { this.tanggal_membayar = tanggal_membayar; }
}