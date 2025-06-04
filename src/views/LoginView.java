package views;

import controllers.AuthController;
import config.AppConstants;

import javax.swing.*;
import java.awt.*;

public class LoginView extends BaseView {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JLabel registerLabel;
    private final AuthController authController;

    public LoginView(AuthController authController) {
        super(AppConstants.APP_TITLE);
        this.authController = authController;

        setSize(AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null); // absolute layout supaya custom
        ImageIcon icon = new ImageIcon("src/assets/icon.png");
        setIconImage(icon.getImage());

        int leftPanelWidth = 720; // 2/3 dari 1134
        int rightPanelWidth = 414; // 1/3 dari 1134

        // LEFT PANEL (BIRU, BISA TARUH LOGO/GAMBAR DI SINI)
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Gradient biru
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(43, 70, 191); // biru muda
                Color color2 = new Color(20, 31, 104); // biru tua
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Gambar/logo custom (letakkan asset Anda di sini)
                // Contoh: src/assets/login-illustration.png
                ImageIcon img = new ImageIcon("src/assets/logo.png");
                g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        leftPanel.setBounds(0, 0, leftPanelWidth, AppConstants.WINDOW_HEIGHT);
        leftPanel.setLayout(null);
        add(leftPanel);

        // RIGHT PANEL (PUTIH, FORM LOGIN)
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(leftPanelWidth, 0, rightPanelWidth, AppConstants.WINDOW_HEIGHT);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);
        add(rightPanel);

        // Judul
        JLabel titleLabel = new JLabel("Selamat Datang");
        titleLabel.setFont(AppConstants.getMontserrat(36f, Font.BOLD));
        titleLabel.setForeground(new Color(43, 70, 191));
        titleLabel.setBounds(40, 120, rightPanelWidth - 80, 48);
        rightPanel.add(titleLabel);

        // Subjudul
        JLabel subtitleLabel = new JLabel("Silahkan isi detail akun anda");
        subtitleLabel.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        subtitleLabel.setForeground(new Color(44, 62, 80));
        subtitleLabel.setBounds(40, 170, rightPanelWidth - 80, 24);
        rightPanel.add(subtitleLabel);

        // Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        usernameLabel.setForeground(new Color(180, 180, 180));
        usernameLabel.setBounds(40, 220, rightPanelWidth - 80, 18);
        rightPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(40, 242, rightPanelWidth - 80, 38);
        usernameField.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        usernameField.setBackground(new Color(250, 250, 250));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 2));
        rightPanel.add(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        passwordLabel.setForeground(new Color(180, 180, 180));
        passwordLabel.setBounds(40, 292, rightPanelWidth - 80, 18);
        rightPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 314, rightPanelWidth - 80, 38);
        passwordField.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        passwordField.setBackground(new Color(250, 250, 250));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 2));
        rightPanel.add(passwordField);

        // Tombol Login
        loginButton = new JButton("Login");
        loginButton.setBounds(40, 374, rightPanelWidth - 80, 42);
        loginButton.setBackground(new Color(43, 70, 191));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(AppConstants.getMontserrat(18f, Font.BOLD));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder());
        rightPanel.add(loginButton);

        // Register link
        registerLabel = new JLabel("<html><span style='color:#888'>Belum punya akun? </span><a href='#' style='color:#2b46bf'>Register</a></html>");
        registerLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        registerLabel.setBounds(40, 430, rightPanelWidth - 80, 24);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightPanel.add(registerLabel);

        // Action listeners
        loginButton.addActionListener(e -> onLogin());
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onRegister();
            }
        });

        setVisible(true);
    }

    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Silakan isi username dan password.");
            return;
        }
        boolean success = authController.login(username, password);
        if (success) {
            showMessage("Login berhasil! Selamat datang, " + authController.getCurrentUser().getDisplayName());
            // TODO: lanjut ke dashboard/main menu
            dispose();
        } else {
            showMessage("Login gagal! Username atau password salah.");
        }
    }

    private void onRegister() {
        new RegisterView(authController).setVisible(true);
        dispose();
    }
}