package views;

import controllers.InstalmentController;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class InstalmentView extends JPanel {
    private final InstalmentController controller;
    private JTextField inputIdKontrak, inputJumlah, inputTenor, inputTanggal;
    private JTable cicilanTable;

    public InstalmentView() {
        this.controller = new InstalmentController();

        setLayout(null);
        setBackground(new Color(248, 249, 251));
        setPreferredSize(new Dimension(1000, 700));

        // Header
        JLabel titleLabel = new JLabel("Pembayaran Cicilan");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 28));
        titleLabel.setForeground(new Color(43, 70, 191));
        titleLabel.setBounds(32, 24, 600, 40);
        add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(32, 90, 936, 200);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        add(formPanel);

        int labelWidth = 150;
        int inputWidth = 250;
        int rowHeight = 35;

        // ID Kontrak
        JLabel idLabel = new JLabel("ID Kontrak");
        idLabel.setBounds(20, 10, labelWidth, rowHeight);
        formPanel.add(idLabel);

        inputIdKontrak = new JTextField();
        inputIdKontrak.setBounds(180, 10, inputWidth, rowHeight);
        formPanel.add(inputIdKontrak);

        // Jumlah Cicilan
        JLabel jumlahLabel = new JLabel("Jumlah Cicilan");
        jumlahLabel.setBounds(20, 55, labelWidth, rowHeight);
        formPanel.add(jumlahLabel);

        inputJumlah = new JTextField();
        inputJumlah.setBounds(180, 55, inputWidth, rowHeight);
        formPanel.add(inputJumlah);

        // Tenor
        JLabel tenorLabel = new JLabel("Tenor");
        tenorLabel.setBounds(20, 100, labelWidth, rowHeight);
        formPanel.add(tenorLabel);

        inputTenor = new JTextField();
        inputTenor.setBounds(180, 100, inputWidth, rowHeight);
        formPanel.add(inputTenor);

        // Tanggal
        JLabel tanggalLabel = new JLabel("Tanggal (yyyy-mm-dd)");
        tanggalLabel.setBounds(20, 145, labelWidth, rowHeight);
        formPanel.add(tanggalLabel);

        inputTanggal = new JTextField();
        inputTanggal.setBounds(180, 145, inputWidth, rowHeight);
        formPanel.add(inputTanggal);

        // Tombol
        JButton buttonBayar = new JButton("Bayar");
        buttonBayar.setBounds(700, 145, 100, 35);
        buttonBayar.setBackground(new Color(40, 167, 69));
        buttonBayar.setForeground(Color.WHITE);
        buttonBayar.setFocusPainted(false);
        formPanel.add(buttonBayar);

        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.setBounds(810, 145, 100, 35);
        buttonCancel.setBackground(new Color(220, 53, 69));
        buttonCancel.setForeground(Color.WHITE);
        buttonCancel.setFocusPainted(false);
        formPanel.add(buttonCancel);

        // Tabel Ringkasan
        String[] columnNames = {"ID Kontrak", "Jumlah", "Tenor", "Tanggal"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        cicilanTable = new JTable(model);

        JScrollPane tableScroll = new JScrollPane(cicilanTable);
        tableScroll.setBounds(32, 320, 936, 320);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        add(tableScroll);

        // Event
        buttonBayar.addActionListener(e -> handleBayar(model));
        buttonCancel.addActionListener(e -> clearForm(model));
    }

    private void handleBayar(DefaultTableModel model) {
        try {
            int idKontrak = Integer.parseInt(inputIdKontrak.getText().trim());
            int jumlah = Integer.parseInt(inputJumlah.getText().trim());
            String tenor = inputTenor.getText().trim();
            LocalDate tanggal = LocalDate.parse(inputTanggal.getText().trim());

            boolean success = controller.tambahCicilan(idKontrak, jumlah, tanggal);
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
