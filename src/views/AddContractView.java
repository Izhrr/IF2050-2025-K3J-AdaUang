package views;

import database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddContractView extends JPanel {
    private JTextField inputIDKontrak, inputTenor, inputStatus, inputTotalBayar, inputJumlahPinjaman, inputSisaCicilan, inputBranch, inputNamaCustomer;
    private JButton buttonTambahkan;
    private Runnable onSuccess;
    private Runnable onClose;

    public AddContractView(Runnable onClose, Runnable onSuccess) {
        setLayout(null);
        setBackground(Color.WHITE);
        this.onClose = onClose;
        this.onSuccess = onSuccess;

        // X button
        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(new Font("Montserrat", Font.BOLD, 28));
        closeBtn.setBorderPainted(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setForeground(new Color(39, 49, 157));
        closeBtn.setBounds(620, 20, 40, 40);
        closeBtn.addActionListener(e -> onClose.run());
        add(closeBtn);

        JLabel title = new JLabel("Tambah Kontrak");
        title.setFont(new Font("Montserrat", Font.BOLD, 32));
        title.setForeground(new Color(39, 49, 157));
        title.setBounds(56, 36, 400, 40);
        add(title);

        int y = 110, h = 48, gap = 32, width = 440;
        inputIDKontrak = addInput("ID Kontrak", "inputIDKontrak", y, h, width);
        inputTenor = addInput("Tenor", "inputTenor", y += h+gap, h, width);
        inputStatus = addInput("Status", "inputStatus", y += h+gap, h, width);
        inputTotalBayar = addInput("Total Bayar", "inputTotalBayar", y += h+gap, h, width);
        inputJumlahPinjaman = addInput("Jumlah Pinjaman", "inputJumlahPinjaman", y += h+gap, h, width);
        inputSisaCicilan = addInput("Sisa Cicilan", "inputSisaCicilan", y += h+gap, h, width);
        inputBranch = addInput("Branch", "inputBranch", y += h+gap, h, width); // Tambah field branch di sini
        inputNamaCustomer = addInput("Nama Customer", "inputNamaCustomer", y += h+gap, h, width);

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

    private JTextField addInput(String label, String id, int y, int height, int width) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Montserrat", Font.PLAIN, 15));
        lbl.setForeground(new Color(180, 180, 180));
        lbl.setBounds(56, y, 200, 20);
        add(lbl);

        JTextField field = new JTextField();
        field.setName(id);
        field.setFont(new Font("Montserrat", Font.PLAIN, 17));
        field.setBounds(56, y+24, width, height-20);
        field.setBorder(BorderFactory.createLineBorder(new Color(185,185,185), 1, true));
        add(field);
        return field;
    }

    private void handleSubmit() {
        String idKontrak = inputIDKontrak.getText().trim();
        String tenor = inputTenor.getText().trim();
        String status = inputStatus.getText().trim();
        String totalBayar = inputTotalBayar.getText().trim();
        String jumlahPinjaman = inputJumlahPinjaman.getText().trim();
        String sisaCicilan = inputSisaCicilan.getText().trim();
        String branch = inputBranch.getText().trim();
        String customerName = inputNamaCustomer.getText().trim();

        if (idKontrak.isEmpty() || tenor.isEmpty() || status.isEmpty() ||
            totalBayar.isEmpty() || jumlahPinjaman.isEmpty() ||
            sisaCicilan.isEmpty() || branch.isEmpty() || customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int staffId = Integer.parseInt(idKontrak);
            int loanTerm = Integer.parseInt(tenor);
            boolean statusVal = status.equalsIgnoreCase("active") || status.equalsIgnoreCase("1") || status.equalsIgnoreCase("true");
            int totalPayment = Integer.parseInt(totalBayar);
            int loanPayment = Integer.parseInt(jumlahPinjaman);
            int remainingInstallment = Integer.parseInt(sisaCicilan);

            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "INSERT INTO kontrak (staff_id, loan_term, status, total_payment, loan_payment, remaining_installment, branch, customer_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, staffId);
                stmt.setInt(2, loanTerm);
                stmt.setBoolean(3, statusVal);
                stmt.setInt(4, totalPayment);
                stmt.setInt(5, loanPayment);
                stmt.setInt(6, remainingInstallment);
                stmt.setString(7, branch);
                stmt.setString(8, customerName);

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
            JOptionPane.showMessageDialog(this, "Pastikan semua field numerik diisi dengan benar!", "Format Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}