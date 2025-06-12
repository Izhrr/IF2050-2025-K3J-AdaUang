package views;

import controllers.AgingReportController;
import controllers.AuthController;
import models.AgingReport;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AgingReportView extends JPanel {
    private final AgingReportController agingReportController;
    private final AuthController authController;
    private JTable agingTable;
    private JTable summaryTable; // Tambahkan tabel summary
    private DefaultTableModel tableModel;
    private DefaultTableModel summaryTableModel; // Tambahkan model untuk summary
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;

    public AgingReportView(AuthController authController) {
        this.authController = authController;
        this.agingReportController = new AgingReportController();
        
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(new Color(248, 249, 251));
        setName("aging-report");

        JPanel mainContentPanel = createMainContentPanel();
        add(mainContentPanel, BorderLayout.CENTER);
        
        loadTableData();
    }

    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 32, 32, 32));

        // Header with title and date filters
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = new JLabel("Laporan Umur Piutang");
        title.setFont(new Font("Montserrat", Font.BOLD, 32));
        title.setForeground(new Color(39, 49, 157));
        headerPanel.add(title, BorderLayout.WEST);

        // Date filter panel
        JPanel filterPanel = createFilterPanel();
        headerPanel.add(filterPanel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // Create tables container dengan kedua tabel
        JPanel tablesContainer = createTablesContainer();
        panel.add(tablesContainer, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setOpaque(false);

        String[] months = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
                          "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setFont(new Font("Montserrat", Font.PLAIN, 14));
        monthComboBox.setSelectedIndex(java.time.LocalDate.now().getMonthValue() - 1);
        filterPanel.add(monthComboBox);

        JLabel yearLabel = new JLabel("Tahun:");
        yearLabel.setFont(new Font("Montserrat", Font.BOLD, 14));
        filterPanel.add(yearLabel);

        Integer[] years = {2023, 2024, 2025, 2026};
        yearComboBox = new JComboBox<>(years);
        yearComboBox.setFont(new Font("Montserrat", Font.PLAIN, 14));
        yearComboBox.setSelectedItem(java.time.LocalDate.now().getYear());
        filterPanel.add(yearComboBox);

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setFont(new Font("Montserrat", Font.BOLD, 14));
        refreshButton.setBackground(new Color(26, 35, 80));
        refreshButton.setForeground(new Color(39, 49, 157));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setPreferredSize(new Dimension(130, 40));
        refreshButton.addActionListener(e -> refreshData());
        filterPanel.add(refreshButton);

        return filterPanel;
... (232 lines left)
Collapse
message.txt
15 KB
instalmentview
package views;

import controllers.AuthController;
import controllers.ContractController;
import controllers.InstalmentController;
import models.Instalment;
Expand
message.txt
16 KB
package controllers;

import models.AgingReport;
import services.AgingReportService;
import java.util.List;

public class AgingReportController extends BaseController {

    private final AgingReportService agingReportService;

    public AgingReportController() {
        this.agingReportService = new AgingReportService();
    }
    
    public List<AgingReport> getAgingReport() {
        return agingReportService.getAgingReportByBranch();
    }

    public List<AgingReport> getAgingReportByMonthYear(int month, int year) {
        return agingReportService.getAgingReportByBranchAndDate(month, year);
    }
    
    // Tambahkan method baru untuk summary
    public AgingReport getAgingReportSummary() {
        return agingReportService.getAgingReportSummary();
    }
    
    public AgingReport getAgingReportSummaryByMonthYear(int month, int year) {
        return agingReportService.getAgingReportSummaryByDate(month, year);
    }
}
package controllers;

import models.Instalment;
import services.InstalmentService;
import java.time.LocalDate;
import java.util.List;

public class InstalmentController extends BaseController {
    
    private final InstalmentService instalmentService;

    public InstalmentController() {
        this.instalmentService = new InstalmentService();
    }


    public boolean addInstalment(int idKontrak, int jumlah, int tenor, LocalDate tanggal, int idStaff) {
        try {
            return instalmentService.createInstalment(idKontrak, jumlah, tenor, tanggal, idStaff);
        } catch (Exception e) {
            System.err.println("Controller error adding instalment: " + e.getMessage());
            return false;
        }
    }


    public List<Instalment> getAllInstalments() {
        return instalmentService.getAllInstalments();
    }


    public List<Instalment> getInstalmentsByContract(int idKontrak) {
        return instalmentService.getInstalmentsByContract(idKontrak);
    }


    public Instalment getInstalmentById(int id) {
        return instalmentService.getInstalmentById(id);
    }


    public int getNextTenor(int idKontrak) {
        return instalmentService.getNextTenor(idKontrak);
    }


    public LocalDate getLastPaymentDate(int idKontrak) {
        return instalmentService.getLastPaymentDate(idKontrak);
    }


    public boolean validatePayment(int idKontrak, int tenor, LocalDate tanggal) {
        return instalmentService.canPayInstalment(idKontrak, tenor, tanggal);
    }


    public LocalDate getMinimumPaymentDate(int idKontrak) {
        LocalDate lastDate = instalmentService.getLastPaymentDate(idKontrak);
        return lastDate != null ? lastDate.plusMonths(1) : LocalDate.now();
    }

}