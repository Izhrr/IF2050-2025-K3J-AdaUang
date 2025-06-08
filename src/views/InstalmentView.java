package views;

import config.AppConstants;
import controllers.InstalmentController;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class InstalmentView extends JPanel {
    private final JTextField inputIdKontrak;
    private final JTextField inputJumlahCicilan;
    private final JTextField inputTenor;
    private final JTextField inputTanggal;
    private final JButton buttonBayar;
    private final JButton buttonCancelBayar;
    private final JTable rtfDetailPembayaran;

    private final InstalmentController controller;

    public InstalmentView() {
        this.controller = new InstalmentController();
        setName("cicilan");
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(900, 600));
        setBackground(Color.WHITE);

        // Panel isi
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        contentPanel.setOpaque(true);
        contentPanel.setBackground(Color.WHITE);

        JLabel header = new JLabel("Detail Pembayaran Cicilan");
        header.setFont(AppConstants.getMontserrat(24f, Font.BOLD));
        header.setForeground(new Color(43, 70, 191));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(header);
        contentPanel.add(Box.createVerticalStrut(20));

        // Form
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(600, 160));

        formPanel.add(new JLabel("ID Kontrak"));
        inputIdKontrak = new JTextField();
        formPanel.add(inputIdKontrak);

        formPanel.add(new JLabel("Jumlah Cicilan"));
        inputJumlahCicilan = new JTextField();
        formPanel.add(inputJumlahCicilan);

        formPanel.add(new JLabel("Tenor"));
        inputTenor = new JTextField();
        formPanel.add(inputTenor);

        formPanel.add(new JLabel("Tanggal (yyyy-mm-dd)"));
        inputTanggal = new JTextField();
        formPanel.add(inputTanggal);

        contentPanel.add(formPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Tabel ringkasan
        JLabel tabelLabel = new JLabel("Ringkasan Pembayaran");
        tabelLabel.setFont(AppConstants.getMontserrat(16f, Font.BOLD));
        tabelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(tabelLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        String[] columns = {"ID Kontrak", "Jumlah", "Tenor", "Tanggal"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        rtfDetailPembayaran = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(rtfDetailPembayaran);
        scrollPane.setPreferredSize(new Dimension(800, 100));
        contentPanel.add(scrollPane);
        contentPanel.add(Box.createVerticalStrut(20));

        // Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        buttonCancelBayar = new JButton("Cancel");
        buttonCancelBayar.setBackground(new Color(220, 53, 69));
        buttonCancelBayar.setForeground(Color.WHITE);

        buttonBayar = new JButton("Bayar");
        buttonBayar.setBackground(new Color(40, 167, 69));
        buttonBayar.setForeground(Color.WHITE);

        buttonPanel.add(buttonCancelBayar);
        buttonPanel.add(buttonBayar);
        contentPanel.add(buttonPanel);

        // Tambahkan contentPanel ke view utama
        add(contentPanel, BorderLayout.CENTER);

        // Event listener
        buttonBayar.addActionListener(e -> handleBayar());
        buttonCancelBayar.addActionListener(e -> clearForm());

        // Debug
        System.out.println("InstalmentView berhasil dimuat dengan komponen: " + contentPanel.getComponentCount());
    }

    private void handleBayar() {
        try {
            int idKontrak = Integer.parseInt(inputIdKontrak.getText().trim());
            int jumlah = Integer.parseInt(inputJumlahCicilan.getText().trim());
            String tenor = inputTenor.getText().trim();
            LocalDate tanggal = LocalDate.parse(inputTanggal.getText().trim());

            boolean success = controller.tambahCicilan(idKontrak, jumlah, tanggal);
            if (success) {
                DefaultTableModel model = (DefaultTableModel) rtfDetailPembayaran.getModel();
                model.setRowCount(0);
                model.addRow(new Object[]{idKontrak, jumlah, tenor + " Bulan", tanggal});
                JOptionPane.showMessageDialog(this, "Cicilan berhasil disimpan.");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan cicilan. Periksa ID Kontrak.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID kontrak dan jumlah harus berupa angka.");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format tanggal salah. Gunakan yyyy-mm-dd.");
        }
    }

    private void clearForm() {
        inputIdKontrak.setText("");
        inputJumlahCicilan.setText("");
        inputTenor.setText("");
        inputTanggal.setText("");
        ((DefaultTableModel) rtfDetailPembayaran.getModel()).setRowCount(0);
    }
}
