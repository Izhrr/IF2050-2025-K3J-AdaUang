package views;

import controllers.AgingReportController;
import controllers.AuthController;
import models.AgingReport;
import models.User;

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
    private DefaultTableModel tableModel;
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

        JPanel tableContainer = createTableContainer();
        panel.add(tableContainer, BorderLayout.CENTER);

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
    }

    private JPanel createTableContainer() {
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        
        String[] columns = {"Cabang", "Total Nasabah", "1-30 Hari", "31-60 Hari", "61-90 Hari", ">90 Hari", "Total Outstanding"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        agingTable = new JTable(tableModel);
        setupTableStyle();
        
        JScrollPane scrollPane = new JScrollPane(agingTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tableContainer.add(agingTable.getTableHeader(), BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        return tableContainer;
    }

    private void setupTableStyle() {
        Color borderColor = new Color(220, 220, 220);
        agingTable.setRowHeight(45);
        agingTable.setFont(new Font("Montserrat", Font.PLAIN, 14));
        agingTable.setSelectionBackground(new Color(235, 240, 255));
        agingTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        agingTable.setShowGrid(true);
        agingTable.setGridColor(borderColor);
        agingTable.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = agingTable.getTableHeader();
        header.setPreferredSize(new Dimension(100, 50));
        header.setFont(new Font("Montserrat", Font.BOLD, 14));
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(100, 100, 100));
        header.setBorder(BorderFactory.createLineBorder(borderColor));

        // Right-align currency columns
        DefaultTableCellRenderer currencyRenderer = new DefaultTableCellRenderer();
        currencyRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        agingTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Cabang
        agingTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Total Nasabah
        
        for(int i = 2; i < agingTable.getColumnCount(); i++){
            agingTable.getColumnModel().getColumn(i).setCellRenderer(currencyRenderer);
        }
    }

    private void loadTableData() {
        try {
            // Debug data terlebih dahulu
            AgingReport.debugAgingData();
            
            tableModel.setRowCount(0);
            
            // Get selected month and year
            int selectedMonth = monthComboBox.getSelectedIndex() + 1;
            int selectedYear = (Integer) yearComboBox.getSelectedItem();
            
            List<AgingReport> reports = agingReportController.getAgingReportByMonthYear(selectedMonth, selectedYear);
            
            System.out.println("Found " + reports.size() + " aging reports for " + selectedMonth + "/" + selectedYear);
            
            for (AgingReport report : reports) {
                long totalOutstanding = report.getAging1to30() + report.getAging31to60() + 
                                      report.getAging61to90() + report.getAgingOver90();
                
                tableModel.addRow(new Object[]{
                    report.getBranch(),
                    report.getTotalNasabah(),
                    formatCurrency(report.getAging1to30()),
                    formatCurrency(report.getAging31to60()),
                    formatCurrency(report.getAging61to90()),
                    formatCurrency(report.getAgingOver90()),
                    formatCurrency(totalOutstanding)
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatCurrency(long amount) {
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(amount);
    }

    public void refreshData() {
        System.out.println("\n🔄 Refreshing aging report data...");
        loadTableData();
    }
}