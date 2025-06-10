package views;

import controllers.AuthController;
import controllers.UserController;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import models.User;

public class UserManagementView extends JPanel {
    private final UserController userController;
    private final AuthController authController;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private ImageIcon optionIcon;

    // Komponen untuk fungsionalitas pop-up
    private JLayeredPane layeredPane;
    private JPanel editRolePanel;
    private JPanel viewDetailPanel;
    private JPanel overlayPanel;
    
    // Komponen di dalam panel edit
    private JLabel editUsernameLabel;
    private JComboBox<String> editRoleComboBox;
    private String userToEdit;
    
    // Komponen di dalam panel detail
    private JLabel detailIdLabel, detailUsernameLabel, detailFullNameLabel, detailBranchLabel, detailRoleLabel;

    public UserManagementView(UserController userController, AuthController authController) {
        this.userController = userController;
        this.authController = authController;
        this.optionIcon = createIcon("/assets/option_icon.png", 18, 18);

        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(new Color(248, 249, 251));

        layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        JPanel mainContentPanel = createMainContentPanel();
        layeredPane.add(mainContentPanel, JLayeredPane.DEFAULT_LAYER);

        overlayPanel = new JPanel();
        overlayPanel.setOpaque(false);
        overlayPanel.setBackground(new Color(0, 0, 0, 128));
        overlayPanel.setVisible(false);
        overlayPanel.addMouseListener(new MouseAdapter() {});
        layeredPane.add(overlayPanel, JLayeredPane.MODAL_LAYER);

        editRolePanel = createEditRolePanel();
        viewDetailPanel = createViewDetailPanel();
        layeredPane.add(editRolePanel, JLayeredPane.POPUP_LAYER);
        layeredPane.add(viewDetailPanel, JLayeredPane.POPUP_LAYER);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainContentPanel.setBounds(0, 0, getWidth(), getHeight());
            }
        });
    }
    
    private JPanel createMainContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(24, 32, 32, 32));

        JLabel title = new JLabel("Manajemen Pengguna");
        title.setFont(new Font("Montserrat", Font.BOLD, 32));
        title.setForeground(new Color(39, 49, 157));
        panel.add(title, BorderLayout.NORTH);

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        
        String[] columns = {"ID", "Username", "Nama Lengkap", "Role", "Aksi"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 4; }
        };
        userTable = new JTable(tableModel);
        setupTableStyle();
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tableContainer.add(userTable.getTableHeader(), BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(tableContainer, BorderLayout.CENTER);
        
        refreshTable();
        return panel;
    }

    private void setupTableStyle() {
        Color borderColor = new Color(220, 220, 220);
        userTable.setRowHeight(45);
        userTable.setFont(new Font("Montserrat", Font.PLAIN, 15));
        userTable.setSelectionBackground(new Color(235, 240, 255));
        userTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        userTable.setShowGrid(true);
        userTable.setGridColor(borderColor);
        userTable.setIntercellSpacing(new Dimension(0, 0));

        int actionColumnIndex = 4;
        userTable.getColumnModel().getColumn(actionColumnIndex).setCellRenderer(new ActionButtonRenderer());
        userTable.getColumnModel().getColumn(actionColumnIndex).setCellEditor(new ActionButtonEditor());
        userTable.getColumnModel().getColumn(actionColumnIndex).setPreferredWidth(60);
        userTable.getColumnModel().getColumn(actionColumnIndex).setMaxWidth(60);
        userTable.getColumnModel().getColumn(actionColumnIndex).setMinWidth(60);
        
        userTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        userTable.getColumnModel().getColumn(0).setMaxWidth(80);
        
        JTableHeader header = userTable.getTableHeader();
        header.setPreferredSize(new Dimension(100, 50));
        header.setFont(new Font("Montserrat", Font.BOLD, 14));
        header.setBackground(new Color(245, 247, 250));
        header.setForeground(new Color(100, 100, 100));
        header.setBorder(BorderFactory.createLineBorder(borderColor));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for(int i = 0; i < userTable.getColumnCount() - 1; i++){
            userTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<User> users = userController.getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{
                u.getId_user(), u.getUsername(), u.getFullname(), u.getRole(), ""
            });
        }
    }
    
    class ActionButtonRenderer extends DefaultTableCellRenderer {
        public ActionButtonRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setIcon(optionIcon); 
            setText("");
            return this;
        }
    }
    
    class ActionButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int currentRow;
        public ActionButtonEditor() {
            super(new JCheckBox()); 
            button = new JButton(optionIcon);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.addActionListener(e -> {
                fireEditingStopped();
                showPopupMenu(currentRow);
            });
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.currentRow = row;
            return button;
        }
        @Override public Object getCellEditorValue() { return ""; }
    }
    
    private void showPopupMenu(int row) {
        String username = (String) tableModel.getValueAt(row, 1);
        User currentUser = authController.getCurrentUser();
        JPopupMenu menu = new JPopupMenu();
        JMenuItem changeRoleItem = new JMenuItem("Ubah Role");
        JMenuItem viewDetailItem = new JMenuItem("Lihat Detail Info");
        if (currentUser != null && currentUser.getUsername().equals(username)) {
            changeRoleItem.setEnabled(false);
            changeRoleItem.setToolTipText("Anda tidak dapat mengubah role akun yang sedang digunakan.");
        }
        changeRoleItem.addActionListener(e -> showEditRolePanel(username));
        viewDetailItem.addActionListener(e -> showViewDetailPanel(username));
        menu.add(changeRoleItem);
        menu.add(viewDetailItem);
        Rectangle cellRect = userTable.getCellRect(row, 4, true);
        menu.show(userTable, cellRect.x - menu.getPreferredSize().width + cellRect.width, cellRect.y);
    }

    private void showEditRolePanel(String username) {
        this.userToEdit = username;
        String oldRole = userController.getUserByUsername(username).getRole();
        editUsernameLabel.setText(username);
        editRoleComboBox.setSelectedItem(oldRole);
        showSidePanel(editRolePanel);
    }
    
    private void showViewDetailPanel(String username) {
        User user = userController.getUserByUsername(username);
        if (user == null) return;
        
        detailIdLabel.setText(String.valueOf(user.getId_user()));
        detailUsernameLabel.setText(user.getUsername());
        detailFullNameLabel.setText(user.getFullname());
        detailBranchLabel.setText(user.getBranch());
        detailRoleLabel.setText(user.getRole());
        showSidePanel(viewDetailPanel);
    }

    private void showSidePanel(JPanel panelToShow) {
        hideSidePanels();
        Dimension size = layeredPane.getSize();
        overlayPanel.setBounds(0, 0, size.width, size.height);
        int panelWidth = 350;
        panelToShow.setBounds(size.width - panelWidth, 0, panelWidth, size.height);
        overlayPanel.setVisible(true);
        panelToShow.setVisible(true);
        layeredPane.moveToFront(overlayPanel);
        layeredPane.moveToFront(panelToShow);
    }

    private void hideSidePanels() {
        overlayPanel.setVisible(false);
        editRolePanel.setVisible(false);
        viewDetailPanel.setVisible(false);
    }
    
    private void saveRoleChanges() {
        String newRole = (String) editRoleComboBox.getSelectedItem();
        boolean success = userController.updateUserRole(this.userToEdit, newRole);
        if (success) {
            JOptionPane.showMessageDialog(this, "Role untuk " + this.userToEdit + " berhasil diubah.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
            hideSidePanels();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah role.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JPanel createViewDetailPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new MatteBorder(0, 1, 0, 0, new Color(220, 220, 220)));
        panel.setVisible(false);
        ImageIcon closeIcon = createIcon("/assets/close_icon.png", 20, 20);
        JButton closeButton = new JButton(closeIcon);
        closeButton.setBounds(305, 15, 30, 30);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> hideSidePanels());
        panel.add(closeButton);
        JLabel titleLabel = new JLabel("Detail Info User");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 22));
        titleLabel.setForeground(new Color(39, 49, 157));
        titleLabel.setBounds(30, 40, 250, 30);
        panel.add(titleLabel);
        int yPos = 90;
        detailIdLabel = addDetailRow(panel, "ID User", yPos); yPos += 55;
        detailUsernameLabel = addDetailRow(panel, "Username", yPos); yPos += 55;
        detailFullNameLabel = addDetailRow(panel, "Nama Lengkap", yPos); yPos += 55;
        detailBranchLabel = addDetailRow(panel, "Branch", yPos); yPos += 55;
        detailRoleLabel = addDetailRow(panel, "Role", yPos);
        return panel;
    }

    private JLabel addDetailRow(JPanel parent, String title, int y) {
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setBounds(30, y, 280, 20);
        parent.add(titleLabel);
        JLabel valueLabel = new JLabel("-");
        valueLabel.setFont(new Font("Montserrat", Font.BOLD, 16));
        valueLabel.setForeground(new Color(50, 50, 50));
        valueLabel.setBounds(30, y + 20, 280, 25);
        parent.add(valueLabel);
        return valueLabel;
    }
    
    private JPanel createEditRolePanel() { 
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new MatteBorder(0, 1, 0, 0, new Color(220, 220, 220)));
        panel.setVisible(false);
        ImageIcon closeIcon = createIcon("/assets/close_icon.png", 20, 20);

        JButton closeButton = new JButton(closeIcon);
        closeButton.setBounds(305, 15, 30, 30);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> hideSidePanels());
        panel.add(closeButton);
        
        JLabel titleLabel = new JLabel("Ganti Role User");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 22));
        titleLabel.setForeground(new Color(39, 49, 157));
        titleLabel.setBounds(30, 40, 250, 30);
        panel.add(titleLabel);

        JLabel usernameTitleLabel = new JLabel("Username");
        usernameTitleLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        usernameTitleLabel.setForeground(Color.GRAY);
        usernameTitleLabel.setBounds(30, 90, 100, 20);
        panel.add(usernameTitleLabel);
        editUsernameLabel = new JLabel("username.tobe.edited");
        editUsernameLabel.setFont(new Font("Montserrat", Font.BOLD, 16));
        editUsernameLabel.setForeground(new Color(50, 50, 50));
        editUsernameLabel.setBounds(30, 110, 280, 25);
        panel.add(editUsernameLabel);

        JLabel roleTitleLabel = new JLabel("Role");
        roleTitleLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        roleTitleLabel.setForeground(Color.GRAY);
        roleTitleLabel.setBounds(30, 160, 100, 20);
        panel.add(roleTitleLabel);
        editRoleComboBox = new JComboBox<>(new String[]{User.ROLE_STAFF, User.ROLE_MANAGER, User.ROLE_ADMIN});
        editRoleComboBox.setFont(new Font("Montserrat", Font.PLAIN, 16));
        editRoleComboBox.setBounds(30, 185, 280, 40);
        panel.add(editRoleComboBox);

        JButton saveButton = new JButton("Simpan Perubahan");
        saveButton.setFont(new Font("Montserrat", Font.BOLD, 16));
        saveButton.setBackground(new Color(39, 49, 157));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setBounds(30, 255, 280, 45);
        saveButton.setOpaque(true);
        saveButton.addActionListener(e -> saveRoleChanges());
        panel.add(saveButton);
        return panel;
    }
    
    private ImageIcon createIcon(String path, int width, int height) { 
        URL resource = getClass().getResource(path); 
        if (resource == null) { 
            System.err.println("Error: Icon resource not found at path: " + path); 
            return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)); 
        } 
        return new ImageIcon(new ImageIcon(resource).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH)); 
    }
}