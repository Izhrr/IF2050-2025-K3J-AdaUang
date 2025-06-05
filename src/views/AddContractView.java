package views;

import database.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddContractView extends JPanel {
    private JTextField inputLoanTerm, inputTotalPayment, inputLoanPayment, inputBranch, inputCustomerName;
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
        inputLoanTerm = addInput("Loan Term", "inputLoanTerm", y, h, width);
        inputTotalPayment = addInput("Total Payment", "inputTotalPayment", y += h + gap, h, width);
        inputLoanPayment = addInput("Loan Payment", "inputLoanPayment", y += h + gap, h, width);
        inputBranch = addInput("Branch", "inputBranch", y += h + gap, h, width);
        inputCustomerName = addInput("Customer Name", "inputCustomerName", y += h + gap, h, width);

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
        field.setBounds(56, y + 24, width, height - 20);
        field.setBorder(BorderFactory.createLineBorder(new Color(185,185,185), 1, true));
        add(field);
        return field;
    }

    private void handleSubmit() {
        String loanTerm = inputLoanTerm.getText().trim();
        String totalPayment = inputTotalPayment.getText().trim();
        String loanPayment = inputLoanPayment.getText().trim();
        String branch = inputBranch.getText().trim();
        String customerName = inputCustomerName.getText().trim();

        if (loanTerm.isEmpty() || totalPayment.isEmpty() || loanPayment.isEmpty() || branch.isEmpty() || customerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int staffId = 1; // default staff_id, bisa diganti sesuai kebutuhan user login
            int loanTermVal = Integer.parseInt(loanTerm);
            int totalPaymentVal = Integer.parseInt(totalPayment);
            int loanPaymentVal = Integer.parseInt(loanPayment);
            int statusVal = 1; // status selalu 1 (active)
            int remainingInstallmentVal = totalPaymentVal;

            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "INSERT INTO kontrak (staff_id, loan_term, status, total_payment, loan_payment, remaining_installment, branch, customer_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, staffId);
                stmt.setInt(2, loanTermVal);
                stmt.setInt(3, statusVal);
                stmt.setInt(4, totalPaymentVal);
                stmt.setInt(5, loanPaymentVal);
                stmt.setInt(6, remainingInstallmentVal);
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