package views;

import database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.sql.Date;

public class AddContractView extends JPanel {
    private JTextField inputTenor, inputTotal, inputNamaUser, inputTanggal, inputIdUser;
    private JButton buttonTambahkan;
    private Runnable onSuccess;
    private Runnable onClose;
    private int idUser; // id_user dari user login
    private LocalDate today;

    public AddContractView(Runnable onClose, Runnable onSuccess, int idUser) {
        setLayout(null);
        setBackground(Color.WHITE);
        this.onClose = onClose;
        this.onSuccess = onSuccess;
        this.idUser = idUser;
        this.today = LocalDate.now();

        // Panel harus "modal"
        setFocusable(true);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);

        // X button
        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(new Font("Montserrat", Font.BOLD, 28));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setForeground(new Color(39, 49, 157));
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setToolTipText("Tutup panel");
        add(closeBtn);

        JLabel title = new JLabel("Tambah Kontrak");
        title.setFont(new Font("Montserrat", Font.BOLD, 32));
        title.setForeground(new Color(39, 49, 157));
        title.setBounds(56, 36, 400, 40);
        add(title);

        int y = 110, h = 48, gap = 32, width = 440;
        inputNamaUser    = addInput("Nama User", "inputNamaUser", y, h, width, false);
        inputTotal       = addInput("Total Pinjaman", "inputTotal", y += h + gap, h, width, false);
        inputTenor       = addInput("Tenor (bulan)", "inputTenor", y += h + gap, h, width, false);
        inputTanggal     = addInput("Tanggal Pinjam", "inputTanggal", y += h + gap, h, width, true);
        inputTanggal.setText(today.toString());
        inputIdUser      = addInput("ID User", "inputIdUser", y += h + gap, h, width, true);
        inputIdUser.setText(String.valueOf(idUser));

        buttonTambahkan = new JButton("Tambahkan");
        buttonTambahkan.setName("buttonTambahkan");
        buttonTambahkan.setFont(new Font("Montserrat", Font.BOLD, 17));
        buttonTambahkan.setBounds(56, y + h + 36, 240, 46);
        buttonTambahkan.setBackground(new Color(39, 49, 157));
        buttonTambahkan.setForeground(Color.WHITE);
        buttonTambahkan.setFocusPainted(false);
        buttonTambahkan.setBorder(BorderFactory.createLineBorder(new Color(39,49,157), 1, true));
        add(buttonTambahkan);

        buttonTambahkan.addActionListener(e -> handleSubmit());
        setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, new Color(230,230,230)));

    }

    private JTextField addInput(String label, String id, int y, int height, int width, boolean readonly) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Montserrat", Font.PLAIN, 15));
        lbl.setForeground(new Color(180, 180, 180));
        lbl.setBounds(56, y, 200, 20);
        add(lbl);

        JTextField field = new JTextField();
        field.setName(id);
        field.setFont(new Font("Montserrat", Font.PLAIN, 17));
        field.setBounds(56, y + 24, width, height - 20);
        field.setBorder(BorderFactory.createLineBorder(new Color(185,185,185), 1, true));
        field.setEditable(!readonly);
        field.setBackground(readonly ? new Color(240,240,240) : Color.WHITE);
        add(field);
        return field;
    }

    // Modal protection: blok semua event
    @Override
    protected void processMouseEvent(MouseEvent e) {
        e.consume();
        super.processMouseEvent(e);
    }
    @Override
    protected void processKeyEvent(KeyEvent e) {
        e.consume();
        super.processKeyEvent(e);
    }
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            requestFocusInWindow();
        }
    }

    private void handleSubmit() {
        String namaUser = inputNamaUser.getText().trim();
        String total = inputTotal.getText().trim();
        String tenor = inputTenor.getText().trim();

        if (namaUser.isEmpty() || total.isEmpty() || tenor.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama, Total, dan Tenor wajib diisi!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int tenorVal = Integer.parseInt(tenor);
            int totalVal = Integer.parseInt(total);
            int jumlahBayarVal = 0; // default selalu 0
            boolean statusVal = true; // status default 1/true (aktif)

            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "INSERT INTO kontrak (nama_user, total, tenor, jumlah_bayar, status, tanggal_pinjam, id_user) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, namaUser);
                stmt.setInt(2, totalVal);
                stmt.setInt(3, tenorVal);
                stmt.setInt(4, jumlahBayarVal); // Always 0
                stmt.setBoolean(5, statusVal);
                stmt.setDate(6, Date.valueOf(today));
                stmt.setInt(7, idUser);

                int result = stmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Data kontrak berhasil ditambahkan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    if (onClose != null) onClose.run();
                    if (onSuccess != null) onSuccess.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambah kontrak.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Pastikan field numerik diisi dengan benar!", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}