package views;

import controllers.InstalmentController;
import models.Instalment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class InstalmentView extends JPanel {
    private final InstalmentController instalmentController;
    private JTable table;
    private DefaultTableModel tableModel;
    private int currentContractId = -1;

    private JTextField jumlahField;
    private JTextField tenorField;
    private JTextField tanggalField;
    private JLabel namaPeminjamLabel;

    public InstalmentView(int contractId, String namaPeminjam) {
        this.currentContractId = contractId;
        this.instalmentController = new InstalmentController();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(24, 32, 32, 32));
        mainPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Detail Pembayaran Cicilan");
        title.setFont(new Font("Montserrat", Font.BOLD, 26));
        title.setForeground(new Color(39, 49, 157));

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createTablePanel(namaPeminjam), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        loadTableData();
    }

    private JPanel createFormPanel() {
        JPanel form = new JPanel(null);
        form.setPreferredSize(new Dimension(750, 150));
        form.setBackground(Color.WHITE);

        JLabel jumlahLabel = new JLabel("Jumlah Cicilan");
        jumlahLabel.setBounds(20, 10, 200, 25);
        form.add(jumlahLabel);

        jumlahField = new JTextField();
        jumlahField.setBounds(20, 35, 200, 30);
        form.add(jumlahField);

        JLabel tenorLabel = new JLabel("Tenor");
        tenorLabel.setBounds(240, 10, 200, 25);
        form.add(tenorLabel);

        tenorField = new JTextField();
        tenorField.setBounds(240, 35, 200, 30);
        form.add(tenorField);

        JLabel tanggalLabel = new JLabel("Tanggal");
        tanggalLabel.setBounds(460, 10, 200, 25);
        form.add(tanggalLabel);

        tanggalField = new JTextField();
        tanggalField.setBounds(460, 35, 200, 30);
        form.add(tanggalField);

        JButton bayarButton = new JButton("Bayar");
        bayarButton.setBounds(680, 35, 80, 30);
        bayarButton.setBackground(new Color(46, 204, 113));
        bayarButton.setForeground(Color.WHITE);
        bayarButton.setFocusPainted(false);
        bayarButton.addActionListener(e -> handleAddInstalment());
        form.add(bayarButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(680, 75, 80, 30);
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> clearForm());
        form.add(cancelButton);

        return form;
    }

    private JPanel createTablePanel(String namaPeminjam) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        namaPeminjamLabel = new JLabel("Nama Peminjam: " + namaPeminjam);
        namaPeminjamLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        namaPeminjamLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        panel.add(namaPeminjamLabel, BorderLayout.NORTH);

        String[] columns = {"Jumlah Cicilan", "Tenor", "Tanggal", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Montserrat", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Instalment> list = instalmentController.getCicilanByKontrak(currentContractId);
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID"));

        for (Instalment i : list) {
            tableModel.addRow(new Object[]{
                format.format(i.getJumlah_membayar()),
                "-", // Placeholder tenor
                sdf.format(i.getTanggal_membayar()),
                format.format(i.getJumlah_membayar())
            });
        }
    }

    private void handleAddInstalment() {
        try {
            int jumlah = Integer.parseInt(jumlahField.getText().trim());
            boolean success = instalmentController.tambahCicilan(currentContractId, jumlah);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cicilan berhasil ditambahkan.");
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan cicilan. Kontrak mungkin sudah lunas.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Masukkan jumlah yang valid.");
        }
    }

    private void clearForm() {
        jumlahField.setText("");
        tenorField.setText("");
        tanggalField.setText("");
    }
}
