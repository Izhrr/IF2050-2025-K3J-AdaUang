package views;

import controllers.AuthController;
import config.AppConstants;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterView extends BaseView {
    private final JTextField fullNameField;
    private final JTextField usernameField;
    // --- PERUBAHAN 1: Mengganti JTextField dengan JComboBox ---
    private final JComboBox<String> branchComboBox;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JButton registerButton;
    // --- PERUBAHAN 2: Menambahkan JLabel untuk link login ---
    private final JLabel loginLabel;
    private final AuthController authController;

    public RegisterView(AuthController authController) {
        super("AdaUang - Register");
        this.authController = authController;

        setSize(AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        // Ganti dengan path icon Anda jika ada
        // setIconImage(new ImageIcon(getClass().getResource("/assets/icon.png")).getImage());

        int leftPanelWidth = 720;
        int rightPanelWidth = 414;

        // LEFT PANEL
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(43, 70, 191);
                Color color2 = new Color(20, 31, 104);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                // Ganti dengan path logo Anda jika ada
                // ImageIcon img = new ImageIcon(getClass().getResource("/assets/logo.png"));
                // g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        leftPanel.setBounds(0, 0, leftPanelWidth, AppConstants.WINDOW_HEIGHT);
        leftPanel.setLayout(null);
        add(leftPanel);

        // RIGHT PANEL
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(leftPanelWidth, 0, rightPanelWidth, AppConstants.WINDOW_HEIGHT);
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);
        add(rightPanel);

        // Judul
        JLabel titleLabel = new JLabel("Buat Akun Baru");
        titleLabel.setFont(AppConstants.getMontserrat(36f, Font.BOLD));
        titleLabel.setForeground(new Color(43, 70, 191));
        titleLabel.setBounds(40, 80, rightPanelWidth - 80, 48);
        rightPanel.add(titleLabel);

        // Subjudul
        JLabel subtitleLabel = new JLabel("Silahkan isi detail akun anda");
        subtitleLabel.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        subtitleLabel.setForeground(new Color(44, 62, 80));
        subtitleLabel.setBounds(40, 130, rightPanelWidth - 80, 24);
        rightPanel.add(subtitleLabel);

        int yPos = 180;
        int fieldHeight = 38;
        int labelHeight = 18;
        int gap = 12;

        // Nama
        JLabel fullNameLabel = new JLabel("Nama Lengkap");
        fullNameLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        fullNameLabel.setForeground(Color.GRAY);
        fullNameLabel.setBounds(40, yPos, rightPanelWidth - 80, labelHeight);
        rightPanel.add(fullNameLabel);
        yPos += labelHeight;

        fullNameField = new JTextField();
        fullNameField.setBounds(40, yPos, rightPanelWidth - 80, fieldHeight);
        fullNameField.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        fullNameField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        rightPanel.add(fullNameField);
        yPos += fieldHeight + gap;
        
        // Username
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        usernameLabel.setForeground(Color.GRAY);
        usernameLabel.setBounds(40, yPos, rightPanelWidth - 80, labelHeight);
        rightPanel.add(usernameLabel);
        yPos += labelHeight;
        
        usernameField = new JTextField();
        usernameField.setBounds(40, yPos, rightPanelWidth - 80, fieldHeight);
        usernameField.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        rightPanel.add(usernameField);
        yPos += fieldHeight + gap;

        // --- PERUBAHAN 3: Menggunakan JComboBox untuk Cabang ---
        JLabel branchLabel = new JLabel("Cabang");
        branchLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        branchLabel.setForeground(Color.GRAY);
        branchLabel.setBounds(40, yPos, rightPanelWidth - 80, labelHeight);
        rightPanel.add(branchLabel);
        yPos += labelHeight;

        String[] branches = {"Bandung", "Jakarta", "Surabaya"};
        branchComboBox = new JComboBox<>(branches);
        branchComboBox.setBounds(40, yPos, rightPanelWidth - 80, fieldHeight);
        branchComboBox.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        branchComboBox.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        branchComboBox.setBackground(Color.WHITE);
        rightPanel.add(branchComboBox);
        yPos += fieldHeight + gap;

        // Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        passwordLabel.setForeground(Color.GRAY);
        passwordLabel.setBounds(40, yPos, rightPanelWidth - 80, labelHeight);
        rightPanel.add(passwordLabel);
        yPos += labelHeight;

        passwordField = new JPasswordField();
        passwordField.setBounds(40, yPos, rightPanelWidth - 80, fieldHeight);
        passwordField.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        rightPanel.add(passwordField);
        yPos += fieldHeight + gap;

        // Ulangi Password
        JLabel confirmPasswordLabel = new JLabel("Ulangi Password");
        confirmPasswordLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        confirmPasswordLabel.setForeground(Color.GRAY);
        confirmPasswordLabel.setBounds(40, yPos, rightPanelWidth - 80, labelHeight);
        rightPanel.add(confirmPasswordLabel);
        yPos += labelHeight;

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(40, yPos, rightPanelWidth - 80, fieldHeight);
        confirmPasswordField.setFont(AppConstants.getMontserrat(16f, Font.PLAIN));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210), 1));
        rightPanel.add(confirmPasswordField);
        yPos += fieldHeight + 20;

        // Tombol Register
        registerButton = new JButton("Register");
        registerButton.setBounds(40, yPos, rightPanelWidth - 80, 42);
        registerButton.setBackground(new Color(43, 70, 191));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(AppConstants.getMontserrat(18f, Font.BOLD));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder());
        rightPanel.add(registerButton);
        yPos += 42 + 15;

        // --- PERUBAHAN 4: Menambahkan link kembali ke Login ---
        loginLabel = new JLabel("<html><span style='color:#888'>Sudah punya akun? </span><a href='#' style='color:#2b46bf'>Login</a></html>");
        loginLabel.setFont(AppConstants.getMontserrat(14f, Font.PLAIN));
        loginLabel.setBounds(40, yPos, rightPanelWidth - 80, 24);
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightPanel.add(loginLabel);
        
        // Action listeners
        registerButton.addActionListener(e -> onRegister());
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onLoginLinkClick();
            }
        });

        setVisible(true);
    }
    
    private void onLoginLinkClick() {
        new LoginView(authController).setVisible(true);
        dispose();
    }

    private void onRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        // --- PERUBAHAN 5: Mengambil data dari JComboBox ---
        String branch = (String) branchComboBox.getSelectedItem(); 
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("Silakan isi semua field!");
            return;
        }

        // Branch dari JComboBox tidak mungkin kosong, jadi pengecekan branch.isEmpty() bisa dihilangkan
        // Default role diset ke "staff", ini bisa diubah jika perlu
        boolean success = authController.register(username, fullName, password, confirmPassword, User.ROLE_STAFF, branch);
        
        if (success) {
            showMessage("Registrasi berhasil! Silakan login dengan akun Anda.");
            onLoginLinkClick(); // Langsung arahkan ke halaman login
        } else {
            showMessage("Registrasi gagal! Username mungkin sudah dipakai atau data tidak valid.");
        }
    }
}