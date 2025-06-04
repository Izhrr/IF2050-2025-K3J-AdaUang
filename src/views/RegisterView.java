package views;

import controllers.AuthController;
import config.AppConstants;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends BaseView {
    private final JTextField fullNameField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JButton registerButton;
    private final AuthController authController;

    public RegisterView(AuthController authController) {
        super("AdaUang - Register");
        this.authController = authController;

        setSize(AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
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

        // RIGHT PANEL (PUTIH, FORM REGISTER)
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(leftPanelWidth, 0, rightPanelWidth, AppConstants.WINDOW_HEIGHT);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);
        add(rightPanel);

        // Judul
        JLabel titleLabel = new JLabel("Buat Akun Baru");
        titleLabel.setFont(AppConstants.getMontserrat(36f, Font.BOLD));
        titleLabel.setForeground(new Color(43, 70, 191));
        titleLabel.setBounds(40, 100, rightPanelWidth - 80, 48);
        rightPanel.add(titleLabel);

        // Subjudul
        JLabel subtitleLabel = new JLabel("Silahkan isi detail akun anda");
        subtitleLabel.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        subtitleLabel.setForeground(new Color(44, 62, 80));
        subtitleLabel.setBounds(40, 150, rightPanelWidth - 80, 24);
        rightPanel.add(subtitleLabel);

        // Nama
        JLabel fullNameLabel = new JLabel("Nama");
        fullNameLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        fullNameLabel.setForeground(new Color(180, 180, 180));
        fullNameLabel.setBounds(40, 200, rightPanelWidth - 80, 18);
        rightPanel.add(fullNameLabel);

        fullNameField = new JTextField();
        fullNameField.setBounds(40, 222, rightPanelWidth - 80, 38);
        fullNameField.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        fullNameField.setBackground(new Color(250, 250, 250));
        fullNameField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 2));
        rightPanel.add(fullNameField);

        // Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        usernameLabel.setForeground(new Color(180, 180, 180));
        usernameLabel.setBounds(40, 272, rightPanelWidth - 80, 18);
        rightPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(40, 294, rightPanelWidth - 80, 38);
        usernameField.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        usernameField.setBackground(new Color(250, 250, 250));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 2));
        rightPanel.add(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        passwordLabel.setForeground(new Color(180, 180, 180));
        passwordLabel.setBounds(40, 344, rightPanelWidth - 80, 18);
        rightPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 366, rightPanelWidth - 80, 38);
        passwordField.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        passwordField.setBackground(new Color(250, 250, 250));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 2));
        rightPanel.add(passwordField);

        // Ulangi Password
        JLabel confirmPasswordLabel = new JLabel("Ulangi Password");
        confirmPasswordLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        confirmPasswordLabel.setForeground(new Color(180, 180, 180));
        confirmPasswordLabel.setBounds(40, 416, rightPanelWidth - 80, 18);
        rightPanel.add(confirmPasswordLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(40, 438, rightPanelWidth - 80, 38);
        confirmPasswordField.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        confirmPasswordField.setBackground(new Color(250, 250, 250));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 2));
        rightPanel.add(confirmPasswordField);

        // Tombol Register
        registerButton = new JButton("Register");
        registerButton.setBounds(40, 498, rightPanelWidth - 80, 42);
        registerButton.setBackground(new Color(43, 70, 191));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(AppConstants.getMontserrat(18f, Font.BOLD));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder());
        rightPanel.add(registerButton);

        registerButton.addActionListener(e -> onRegister());

        setVisible(true);
    }

    private void onRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Silakan isi semua field!");
            return;
        }
        boolean success = authController.register(username, fullName, password, confirmPassword, "user");
        if (success) {
            showMessage("Registrasi berhasil! Selamat datang " + authController.getCurrentUser().getDisplayName());
            // TODO: lanjut ke dashboard/main menu
            dispose();
        } else {
            showMessage("Registrasi gagal! Username mungkin sudah dipakai atau data tidak valid.");
        }
    }
}