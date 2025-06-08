package models;

import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Instalment extends BaseModel {
    private int id_cicilan;
    private Date tanggal_cicilan;
    private int jumlah_cicilan;
    private int id_kontrak;

    public Instalment() {}

    @Override
    public boolean save() {
        try {
            if (isNewRecord()) {
                return insert();
            } else {
                // logika update jika diperlukan
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean insert() throws SQLException {
        String sql = "INSERT INTO cicilan (id_kontrak, jumlah_cicilan, tanggal_cicilan) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, this.id_kontrak);
            stmt.setInt(2, this.jumlah_cicilan);
            stmt.setDate(3, new java.sql.Date(this.tanggal_cicilan.getTime()));

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.id_cicilan = generatedKeys.getInt(1);
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete() {
        // Belum diimplementasi
        return false;
    }

    @Override
    public boolean isNewRecord() {
        return id_cicilan == 0;
    }

    public static List<Instalment> findAllWithContractDetails() {
        List<Instalment> instalments = new ArrayList<>();
        String sql = "SELECT c.*, k.nama_user FROM cicilan c " +
                     "JOIN kontrak k ON c.id_kontrak = k.id_kontrak ORDER BY c.id_cicilan ASC";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                instalments.add(createFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instalments;
    }

    public static Instalment findById(int id) {
        Instalment instalment = null;
        String sql = "SELECT c.* FROM cicilan c WHERE c.id_cicilan = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    instalment = createFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instalment;
    }

    private static Instalment createFromResultSet(ResultSet rs) throws SQLException {
        Instalment instalment = new Instalment();
        instalment.setid_cicilan(rs.getInt("id_cicilan"));
        instalment.settanggal_cicilan(rs.getDate("tanggal_cicilan"));
        instalment.setjumlah_cicilan(rs.getInt("jumlah_cicilan"));
        instalment.setid_kontrak(rs.getInt("id_kontrak"));
        return instalment;
    }

    // Getter dan Setter
    public String getFormattedTotal() {
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(this.jumlah_cicilan);
    }

    public int getid_cicilan() {
        return id_cicilan;
    }

    public void setid_cicilan(int id_cicilan) {
        this.id_cicilan = id_cicilan;
    }

    public int getjumlah_cicilan() {
        return jumlah_cicilan;
    }

    public void setjumlah_cicilan(int jumlah_cicilan) {
        this.jumlah_cicilan = jumlah_cicilan;
    }

    public Date gettanggal_cicilan() {
        return tanggal_cicilan;
    }

    public void settanggal_cicilan(Date tanggal_cicilan) {
        this.tanggal_cicilan = tanggal_cicilan;
    }

    public int getid_kontrak() {
        return id_kontrak;
    }

    public void setid_kontrak(int id_kontrak) {
        this.id_kontrak = id_kontrak;
    }
}
