package views;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.*;

public class SidebarView extends JPanel {

    public interface SidebarListener {
        void menuSelected(String menu);
        void logoutRequested();
    }

    private final SidebarListener listener;

    public SidebarView(User currentUser, SidebarListener listener) {
        this.listener = listener;
        setBackground(new Color(26, 35, 80));
        setPreferredSize(new Dimension(260, 0));
        setLayout(new BorderLayout());

        add(createTopPanel(currentUser), BorderLayout.NORTH);
        add(createBottomPanel(currentUser), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel(User currentUser) {
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        ImageIcon logoIcon = createIcon("/icon.png", 35, 28);
        JLabel logoIconLabel = new JLabel(logoIcon);

        JLabel appTitle = new JLabel("AdaUang");
        appTitle.setFont(new Font("Montserrat", Font.BOLD, 28));
        appTitle.setForeground(Color.WHITE);
        appTitle.setBorder(new EmptyBorder(0, 10, 0, 0));

        logoPanel.add(logoIconLabel);
        logoPanel.add(appTitle);
        topPanel.add(logoPanel);
        topPanel.add(Box.createVerticalStrut(30));

        if (currentUser != null) {
            switch (currentUser.getRole()) {
                case User.ROLE_ADMIN:
                    topPanel.add(menuButton(createIcon("/user_icon.png", 24, 24), "Manajemen User", "user_management"));
                    break;
                case User.ROLE_STAFF:
                    topPanel.add(menuButton(createIcon("/contract_icon.png", 24, 24), "Manajemen Kontrak", "kontrak"));
                    topPanel.add(menuButton(createIcon("/contract_icon.png", 24, 24), "Manajemen Cicilan", "cicilan"));
                    break;
                case User.ROLE_MANAGER:
                    topPanel.add(menuButton(createIcon("/contract_icon.png", 24, 24), "Manajemen Kontrak", "kontrak"));
                    topPanel.add(menuButton(createIcon("/contract_icon.png", 24, 24), "Manajemen Cicilan", "cicilan"));
                    topPanel.add(menuButton(createIcon("/contract_icon.png", 24, 24), "Laporan Umur Piutang", "aging-report"));
                    break;
            }
        }
        return topPanel;
    }

    // --- PERUBAHAN SIGNIFIKAN DI SINI UNTUK SIMETRI ---
    private JPanel createBottomPanel(User currentUser) {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(15, 25, 20, 25));

        // --- Baris Info Pengguna ---
        JPanel userInfoRow = new JPanel(new BorderLayout(15, 0)); // Gunakan BorderLayout
        userInfoRow.setOpaque(false);
        userInfoRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        ImageIcon avatarIcon = createIcon("/avatar_icon.png", 22, 22);
        JLabel avatarLabel = new JLabel(avatarIcon);

        JPanel userDetailsPanel = new JPanel();
        userDetailsPanel.setOpaque(false);
        userDetailsPanel.setLayout(new BoxLayout(userDetailsPanel, BoxLayout.Y_AXIS));
        
        JLabel fullNameLabel = new JLabel(currentUser != null ? currentUser.getFullname() : "Nama Pengguna");
        fullNameLabel.setFont(new Font("Montserrat", Font.BOLD, 16));
        fullNameLabel.setForeground(Color.WHITE);

        JLabel roleLabel = new JLabel(currentUser != null ? capitalizeFirst(currentUser.getRole()) : "ROLE");
        roleLabel.setFont(new Font("Montserrat", Font.PLAIN, 12));
        roleLabel.setForeground(Color.LIGHT_GRAY);

        userDetailsPanel.add(fullNameLabel);
        userDetailsPanel.add(Box.createVerticalStrut(4));
        userDetailsPanel.add(roleLabel);
        
        // Atur posisi dengan BorderLayout
        userInfoRow.add(avatarLabel, BorderLayout.WEST);
        userInfoRow.add(userDetailsPanel, BorderLayout.CENTER);


        // Logout
        JPanel logoutRow = new JPanel(new BorderLayout(15, 0)); // Gunakan BorderLayout
        logoutRow.setOpaque(false);
        logoutRow.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        ImageIcon logoutIcon = createIcon("/logout_icon.png", 24, 24);
        JLabel logoutIconLabel = new JLabel(logoutIcon);
        
        JLabel logoutText = new JLabel("Logout");
        logoutText.setFont(new Font("Montserrat", Font.BOLD, 16));
        logoutText.setForeground(Color.WHITE);

        // Atur posisi dengan BorderLayout
        logoutRow.add(logoutIconLabel, BorderLayout.WEST);
        logoutRow.add(logoutText, BorderLayout.CENTER);

        // Tambah listener untuk seluruh baris logout
        logoutRow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null) {
                    listener.logoutRequested();
                }
            }
        });

        // Komponen panel bawah
        bottomPanel.add(userInfoRow);
        bottomPanel.add(Box.createVerticalStrut(20)); // Jarak antara info user dan tombol logout
        bottomPanel.add(logoutRow);
        
        return bottomPanel;
    }

    private JPanel menuButton(ImageIcon icon, String text, String menuTag) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 10));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Montserrat", Font.BOLD, 15));
        textLabel.setForeground(Color.WHITE);

        panel.add(iconLabel);
        panel.add(textLabel);
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (listener != null) listener.menuSelected(menuTag);
            }
        });

        panel.setName("menu-" + menuTag);
        return panel;
    }
    
    private ImageIcon createIcon(String path, int width, int height) {
        URL resource = getClass().getResource(path);
        if (resource == null) {
            System.err.println("Error: Icon resource not found at path: " + path);
            return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
        }
        ImageIcon originalIcon = new ImageIcon(resource);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}