package views;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContractView extends JPanel {
    private JTable contractTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton;
    private JPanel overlayPanel;
    private AddContractView addContractPanel;

    public ContractView() {
        setLayout(null);
        setBackground(new Color(245, 246, 250));

        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.setColor(new Color(0,0,0,24));
                g2.dispose();
            }
        };
        
        mainPanel.setOpaque(false);
        mainPanel.setBounds(80, 40, 900, 600); // Lebar tabel diperbesar agar muat kolom baru
        mainPanel.setBorder(new CompoundBorder(
            new EmptyBorder(0, 0, 0, 0),
            new DropShadowBorder()
        ));

        JLabel title = new JLabel("Kontrak Pembiayaan");
        title.setFont(new Font("Montserrat", Font.BOLD, 40));
        title.setForeground(new Color(39, 49, 157));
        title.setBounds(42, 32, 600, 48);

        searchField = new JTextField();
        searchField.setFont(new Font("Montserrat", Font.PLAIN, 17));
        searchField.setBounds(42, 100, 210, 38);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220,220,220), 1, true),
            new EmptyBorder(0, 10, 0, 0)
        ));
        searchField.setToolTipText("Cari Kontrak");
        searchField.putClientProperty("JTextField.placeholderText", "Cari Kontrak");

        addButton = new JButton("+ Tambah Kontrak");
        addButton.setFont(new Font("Montserrat", Font.BOLD, 16));
        addButton.setBackground(new Color(39, 49, 157));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(new RoundBorder(12));
        addButton.setBounds(270, 100, 180, 38);
        addButton.addActionListener(e -> showAddContractPanel());

        // Tabel: Tambah kolom Branch & Total Cicilan
        String[] columns = {"ID Kontrak", "Username", "Status", "Branch", "Total Cicilan", ""};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return col == 5; }
        };

        contractTable = new JTable(tableModel);
        contractTable.setRowHeight(44);
        contractTable.setFont(new Font("Montserrat", Font.PLAIN, 16));
        contractTable.setShowGrid(false);
        contractTable.setIntercellSpacing(new Dimension(0, 0));
        contractTable.setFillsViewportHeight(true);

        JTableHeader header = contractTable.getTableHeader();
        header.setFont(new Font("Montserrat", Font.BOLD, 16));
        header.setOpaque(false);
        header.setBackground(new Color(245, 246, 250));
        header.setForeground(new Color(39, 49, 157));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        // Status cell
        contractTable.getColumnModel().getColumn(2).setCellRenderer((table, value, isSelected, hasFocus, row, col) -> {
            JLabel lbl = new JLabel(value.toString(), SwingConstants.CENTER);
            lbl.setOpaque(true);
            lbl.setFont(new Font("Montserrat", Font.BOLD, 15));
            lbl.setBorder(new EmptyBorder(4, 18, 4, 18));
            if ("Active".equals(value)) {
                lbl.setBackground(new Color(212, 243, 223));
                lbl.setForeground(new Color(27, 156, 87));
            } else {
                lbl.setBackground(new Color(252, 228, 236));
                lbl.setForeground(new Color(214, 62, 89));
            }
            lbl.setBorder(new LineBorder(new Color(240,240,240), 1, true));
            lbl.setPreferredSize(new Dimension(80, 30));
            return lbl;
        });

        // Action (3 dots) button
        contractTable.getColumnModel().getColumn(5).setCellRenderer((table, value, isSelected, hasFocus, row, col) -> {
            JButton btn = new JButton("\u22EE");
            btn.setFont(new Font("Arial", Font.BOLD, 22));
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(new EmptyBorder(0, 0, 0, 0));
            btn.addActionListener(e -> showPopupMenu(btn, row));
            return btn;
        });

        // Table scroll
        JScrollPane scrollPane = new JScrollPane(contractTable);
        scrollPane.setBounds(32, 170, 820, 380); // lebar diperbesar agar muat kolom

        mainPanel.add(title);
        mainPanel.add(searchField);
        mainPanel.add(addButton);
        mainPanel.add(scrollPane);
        add(mainPanel);

        loadTableData();
    }

    class RoundBorder extends LineBorder {
        public RoundBorder(int radius) { super(new Color(39,49,157), 0, true); this.arc = radius; }
        private int arc;
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(lineColor);
            g.drawRoundRect(x, y, width-1, height-1, arc, arc);
        }
    }
    class DropShadowBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0,0,0,28));
            g2.fillRoundRect(x+4, y+8, width-8, height-8, 32, 32);
            g2.dispose();
        }
    }

    private void showPopupMenu(Component invoker, int row) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem detail = new JMenuItem("Lihat Detail");
        JMenuItem nonaktif = new JMenuItem("Nonaktifkan");
        JMenuItem hapus = new JMenuItem("Hapus");
        hapus.setForeground(new Color(214, 62, 89));

        menu.add(detail);
        menu.add(nonaktif);
        menu.add(hapus);
        menu.show(invoker, 0, invoker.getHeight());
    }

    // Load id, username, status, branch, total_payment dari kontrak table
    private void loadTableData() {
        List<Object[]> rows = new ArrayList<>();
        try (
            Connection conn = DriverManager.getConnection(
                config.DatabaseConfig.getInstance().getDbUrl(),
                config.DatabaseConfig.getInstance().getDbUsername(),
                config.DatabaseConfig.getInstance().getDbPassword()
            );
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT k.id_kontrak, u.username, k.status, u.branch, k.jumlah_bayar " +
                "FROM kontrak k JOIN users u ON k.id_user = u.id_user")
        ) {
            while (rs.next()) {
                int id = rs.getInt("id_kontrak");
                String username = rs.getString("username");
                boolean status = rs.getBoolean("status");
                String branch = rs.getString("branch");
                int jumlahBayar = rs.getInt("jumlah_bayar");
                rows.add(new Object[]{
                    id,
                    username,
                    status ? "Active" : "Inactive",
                    branch,
                    jumlahBayar,
                    ""
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data kontrak:\n" + ex.getMessage());
        }
        tableModel.setRowCount(0);
        for (Object[] row : rows) tableModel.addRow(row);
    }

    private void showAddContractPanel() {
        if (overlayPanel == null) {
            overlayPanel = new JPanel();
            overlayPanel.setOpaque(false);
            overlayPanel.setLayout(null);
            overlayPanel.setBounds(0, 0, getWidth(), getHeight());
            overlayPanel.setBackground(new Color(0,0,0,80));
            addContractPanel = new AddContractView(
                () -> { // onClose
                    hideAddContractPanel();
                }, 
                () -> { // onSuccess (reload + close)
                    hideAddContractPanel();
                    loadTableData();
                }
            );
            addContractPanel.setBounds(getWidth()/2, 0, getWidth()/2, getHeight());
            overlayPanel.add(addContractPanel);
        }
        addButton.setEnabled(false); // disable button saat AddContractView tampil
        add(overlayPanel, 0);
        overlayPanel.setVisible(true);
        revalidate();
        repaint();
    }

    private void hideAddContractPanel() {
        if (overlayPanel != null) {
            overlayPanel.setVisible(false);
            remove(overlayPanel);
            addButton.setEnabled(true); // enable lagi
            revalidate();
            repaint();
        }
    }
}