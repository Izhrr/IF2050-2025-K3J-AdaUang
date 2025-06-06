package views;

import controllers.AuthController;
import controllers.UserController;
import models.User;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private AuthController authController;
    private UserController userController;
    private User currentUser;

    public MainView(AuthController authController) {
        this.authController = authController;
        this.userController = new UserController();
        this.currentUser = authController.getCurrentUser();

        setTitle("AdaUang - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        // Ganti dengan path icon Anda jika ada
        setIconImage(new ImageIcon(getClass().getResource("/assets/icon.png")).getImage());

        // Implementasi listener dengan method logoutRequested
        SidebarView.SidebarListener sidebarListener = new SidebarView.SidebarListener() {
            @Override
            public void menuSelected(String menuName) {
                switchPanel(menuName);
            }

            @Override
            public void logoutRequested() {
                handleLogout();
            }
        };

        SidebarView sidebar = new SidebarView(currentUser, sidebarListener);
        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new ContractView(authController), "kontrak");
        
        if (currentUser != null && "admin".equals(currentUser.getRole())) {
            mainPanel.add(new UserManagementView(userController, authController), "user_management");
        }

        add(mainPanel, BorderLayout.CENTER);

        if (currentUser != null && "admin".equals(currentUser.getRole())) {
            cardLayout.show(mainPanel, "user_management");
        } else {
            cardLayout.show(mainPanel, "kontrak");
        }
    }
    
    // Method baru untuk menangani proses logout
    private void handleLogout() {
        int response = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            authController.logout(); // Hapus sesi user saat ini
            new LoginView(authController).setVisible(true); // Tampilkan halaman login
            this.dispose(); // Tutup window MainView
        }
    }

    private void switchPanel(String menuName) {
        boolean panelExists = false;
        for (Component comp : mainPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(menuName)) {
                panelExists = true;
                break;
            }
        }
        if (panelExists) {
            cardLayout.show(mainPanel, menuName);
        } else {
            System.err.println("Panel dengan nama '" + menuName + "' tidak ditemukan.");
        }
    }
}