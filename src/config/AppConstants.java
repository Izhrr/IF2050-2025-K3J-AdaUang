package config;

import java.awt.Color;
import java.awt.Font;
import java.io.InputStream;

public class AppConstants {
    // UI Color Constants (from original CommonConstants)
    public static final Color PRIMARY_COLOR = Color.decode("#191E29");
    public static final Color SECONDARY_COLOR = Color.decode("#132D46");
    public static final Color TEXT_COLOR = Color.decode("#01C38D");
    
    // UI Constants
    public static final int WINDOW_WIDTH = 1134;
    public static final int WINDOW_HEIGHT = 736;
    public static final int FONT_SIZE_LARGE = 32;
    public static final int FONT_SIZE_MEDIUM = 18;
    public static final int FONT_SIZE_SMALL = 16;
    
    // Application Constants
    public static final String APP_TITLE = "AdaUang - Login";
    public static final String VERSION = "1.0.0";
    
    // User Roles
    public static final String ROLE_STAFF = "staff";
    public static final String ROLE_MANAGER = "manager";
    public static final String ROLE_ADMIN = "admin";
    
    // Validation Constants
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MAX_PASSWORD_LENGTH = 255;
    public static final int MAX_FULLNAME_LENGTH = 100;
    
    // Success/Error Messages
    public static final String SUCCESS_REGISTER = "Registration successful!";
    public static final String SUCCESS_LOGIN = "Login successful!";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ERROR_USER_EXISTS = "Username already exists";
    public static final String ERROR_VALIDATION_FAILED = "Please check your input";
    public static final String ERROR_DATABASE_CONNECTION = "Database connection failed";
    public static final String ERROR_REQUIRED_FIELDS = "Please fill all required fields";
    
    // Info Messages
    public static final String INFO_WELCOME_USER = "Welcome, %s!";
    public static final String INFO_WELCOME_ADMIN = "Welcome, %s! (Administrator)";

    // Font loader: ukuran dan style bisa diatur saat pemakaian
    public static Font getMontserrat(float size, int style) {
        String fontPath = style == Font.BOLD ? "/assets/Montserrat-Bold.ttf" : "/assets/Montserrat.ttf";
        try (InputStream is = AppConstants.class.getResourceAsStream(fontPath)) {
            if (is == null) {
                System.out.println("Font resource NOT FOUND! Path: " + fontPath);
                return new Font("SansSerif", style, (int) size);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            return font.deriveFont(style, size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", style, (int) size);
        }
    }
}
