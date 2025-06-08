package views;

import controllers.AuthController;
import controllers.InstalmentController;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import models.User;

public class InstalmentView extends JPanel {
    private final InstalmentController controller;
    private final AuthController authController;

    private JTextField inputIdKontrak, inputJumlah, inputTenor, inputTanggal;
    private JTable cicilanTable;

    public InstalmentView(AuthController authController) {
        this.authController = authController;
        this.controller = new InstalmentController();

        setLayout(null);
        setBackground(new Color(248, 249, 251));
        setPreferredSize(new Dimension(800, 600)); // Kurangi dari 1000x700
        setVisible(true);
        setName("cicilan");

        JLabel titleLabel = new JLabel("Pembayaran Cicilan");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 28));
        titleLabel.setForeground(new Color(43, 70, 191));
        titleLabel.setBounds(32, 24, 400, 40); // Kurangi width
        add(titleLabel);

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(32, 90, 750, 180); // Kurangi dari 936x200 ke 750x180
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 15, 15, 15) // Kurangi padding
        ));
        add(formPanel);

        int labelWidth = 120; // Kurangi dari 150
        int inputWidth = 200; // Kurangi dari 250
        int rowHeight = 30; // Kurangi dari 35

        JLabel idLabel = new JLabel("ID Kontrak");
        idLabel.setBounds(20, 10, labelWidth, rowHeight);
        formPanel.add(idLabel);

        inputIdKontrak = new JTextField();
        inputIdKontrak.setBounds(150, 10, inputWidth, rowHeight);
        formPanel.add(inputIdKontrak);

        JLabel jumlahLabel = new JLabel("Jumlah Cicilan");
        jumlahLabel.setBounds(20, 50, labelWidth, rowHeight);
        formPanel.add(jumlahLabel);

        inputJumlah = new JTextField();
        inputJumlah.setBounds(150, 50, inputWidth, rowHeight);
        formPanel.add(inputJumlah);

        JLabel tenorLabel = new JLabel("Tenor");
        tenorLabel.setBounds(20, 90, labelWidth, rowHeight);
        formPanel.add(tenorLabel);

        inputTenor = new JTextField();
        inputTenor.setBounds(150, 90, inputWidth, rowHeight);
        formPanel.add(inputTenor);

        JLabel tanggalLabel = new JLabel("Tanggal (yyyy-mm-dd)");
        tanggalLabel.setBounds(20, 130, labelWidth, rowHeight);
        formPanel.add(tanggalLabel);

        inputTanggal = new JTextField();
        inputTanggal.setBounds(150, 130, inputWidth, rowHeight);
        formPanel.add(inputTanggal);

        JButton buttonBayar = new JButton("Bayar");
        buttonBayar.setBounds(520, 50, 80, 30); // Posisi dan ukuran lebih kecil
        buttonBayar.setBackground(new Color(40, 167, 69));
        buttonBayar.setForeground(Color.WHITE);
        buttonBayar.setFont(new Font("Montserrat", Font.BOLD, 12));
        buttonBayar.setFocusPainted(false);
        buttonBayar.setOpaque(true);
        buttonBayar.setBorderPainted(false);
        formPanel.add(buttonBayar);

        JButton buttonCancel = new JButton("Batal");
        buttonCancel.setBounds(610, 50, 80, 30); // Posisi dan ukuran lebih kecil
        buttonCancel.setBackground(new Color(220, 53, 69));
        buttonCancel.setForeground(Color.WHITE);
        buttonCancel.setFont(new Font("Montserrat", Font.BOLD, 12));
        buttonCancel.setFocusPainted(false);
        buttonCancel.setOpaque(true);
        buttonCancel.setBorderPainted(false);
        formPanel.add(buttonCancel);

        String[] columnNames = {"ID Kontrak", "Jumlah", "Tenor", "Tanggal"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        cicilanTable = new JTable(model);

        JScrollPane tableScroll = new JScrollPane(cicilanTable);
        tableScroll.setBounds(32, 290, 750, 250); // Kurangi dari 936x320
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        add(tableScroll);

        buttonBayar.addActionListener(e -> handleBayar(model));
        buttonCancel.addActionListener(e -> clearForm(model));

        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    private void handleBayar(DefaultTableModel model) {
        try {
            int idKontrak = Integer.parseInt(inputIdKontrak.getText().trim());
            int jumlah = Integer.parseInt(inputJumlah.getText().trim());
            String tenor = inputTenor.getText().trim();
            LocalDate tanggal = LocalDate.parse(inputTanggal.getText().trim());

            User currentUser = authController.getCurrentUser();
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "User tidak dikenali.");
                return;
            }

            int idStaff = currentUser.getId_user();
            boolean success = controller.tambahCicilan(idKontrak, jumlah, tanggal, idStaff);
            if (success) {
                model.setRowCount(0);
                model.addRow(new Object[]{idKontrak, jumlah, tenor + " Bulan", tanggal});
                JOptionPane.showMessageDialog(this, "Cicilan berhasil disimpan.");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan cicilan. Periksa ID kontrak.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid. Pastikan semua field terisi dengan benar.");
        }
    }

    private void clearForm(DefaultTableModel model) {
        inputIdKontrak.setText("");
        inputJumlah.setText("");
        inputTenor.setText("");
        inputTanggal.setText("");
        model.setRowCount(0);
    }
}
