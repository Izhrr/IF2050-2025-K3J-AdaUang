package views;

import javax.swing.*;
import java.awt.*;

public class SidebarView extends JPanel {
    public SidebarView() {
        setBackground(new Color(26, 35, 80));
        setPreferredSize(new Dimension(265, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Logo & App name
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(26, 35, 80));
        logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 18));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Uncomment to use your logo image:
        JLabel logoIcon = new JLabel(new ImageIcon("src/assets/icon.png"));
        logoPanel.add(logoIcon);

        JLabel logoText = new JLabel("AdaUang");
        logoText.setFont(new Font("Montserrat", Font.BOLD, 25));
        logoText.setForeground(Color.WHITE);
        logoText.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        logoPanel.add(logoText);

        add(logoPanel);
        add(Box.createVerticalStrut(32));

        // Menu
        add(menuButton("\uD83D\uDCC4", "Manajemen Kontrak", true));
        add(Box.createVerticalStrut(6));
        add(menuButton("\uD83D\uDD59", "Histori Cicilan", false));

        add(Box.createVerticalGlue());

        // User panel (bottom)
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        userPanel.setBackground(new Color(26, 35, 80));
        userPanel.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        userPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Avatar (replace with image when available)
        JLabel avatar = new JLabel();
        avatar.setPreferredSize(new Dimension(44, 44));
        avatar.setMaximumSize(new Dimension(44, 44));
        avatar.setOpaque(true);
        avatar.setBackground(Color.LIGHT_GRAY); // Placeholder color
        avatar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        // // Uncomment for image avatar:
        avatar.setIcon(new ImageIcon("src/assets/icon.png"));

        userPanel.add(avatar);

        Box userInfo = Box.createVerticalBox();
        userInfo.setAlignmentY(Component.CENTER_ALIGNMENT);
        JLabel name = new JLabel("Izhar Alif Akbar");
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Montserrat", Font.BOLD, 15));
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel email = new JLabel("18232192@std.stei.itb.ac.id");
        email.setForeground(new Color(180, 180, 200));
        email.setFont(new Font("Montserrat", Font.PLAIN, 13));
        email.setAlignmentX(Component.LEFT_ALIGNMENT);
        userInfo.add(name);
        userInfo.add(email);

        userPanel.add(Box.createHorizontalStrut(9));
        userPanel.add(userInfo);

        add(userPanel);

        add(Box.createVerticalStrut(18));

        // Logout (bottom, left aligned, with icon)
        JPanel logoutPanel = new JPanel();
        logoutPanel.setBackground(new Color(26, 35, 80));
        logoutPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoutPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logoutIcon = new JLabel("\u21B6"); // Unicode for logout/return-left
        logoutIcon.setFont(new Font("Arial", Font.BOLD, 22));
        logoutIcon.setForeground(Color.WHITE);
        logoutIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        logoutPanel.add(logoutIcon);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Montserrat", Font.BOLD, 15));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(26, 35, 80));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setAlignmentY(Component.CENTER_ALIGNMENT);

        logoutPanel.add(logoutBtn);

        add(logoutPanel);
        add(Box.createVerticalStrut(16));
    }

    private JPanel menuButton(String icon, String text, boolean selected) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(selected ? new Color(36, 46, 110) : new Color(26, 35, 80));
        panel.setMaximumSize(new Dimension(250, 48));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 12));

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Montserrat", Font.BOLD, 15));
        textLabel.setForeground(Color.WHITE);

        panel.add(iconLabel);
        panel.add(textLabel);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        return panel;
    }
}