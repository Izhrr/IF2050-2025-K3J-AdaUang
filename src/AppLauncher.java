import controllers.AuthController;
import database.DatabaseMigration;
import database.DatabaseSeeder;
import config.DatabaseConfig;
import views.LoginView;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        // 1. Print config untuk debug (opsional, bisa dihapus jika tidak perlu)
        DatabaseConfig.getInstance().printConfig();

        // 2. Jalankan migration & seeder (setup database otomatis di awal)
        new DatabaseMigration().runMigration();
        new DatabaseSeeder().runSeeder();

        // 3. Mulai aplikasi GUI di thread Swing
        SwingUtilities.invokeLater(() -> {
            AuthController authController = new AuthController();
            new LoginView(authController).setVisible(true);
        });
    }
}