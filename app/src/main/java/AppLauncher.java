import controllers.AuthController;
import database.DatabaseMigration;
import database.DatabaseSeeder;
import config.DatabaseConfig;
import views.LoginView;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        DatabaseConfig.getInstance().printConfig();
        new DatabaseMigration().runMigration();
        new DatabaseSeeder().runSeeder();
        SwingUtilities.invokeLater(() -> {
            AuthController authController = new AuthController();
            new LoginView(authController).setVisible(true);
        });
    }
}