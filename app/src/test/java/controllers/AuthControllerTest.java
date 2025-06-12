package controllers;
import models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import database.DatabaseSeeder;

public class AuthControllerTest {

    @BeforeAll
    static void setup() {
        // Seed database sebelum semua test
        DatabaseSeeder seeder = new DatabaseSeeder();
        assertTrue(seeder.runSeeder(), "Seeder harus sukses dijalankan");
    }

    @Test
    void testLoginSuccess() {
        AuthController authController = new AuthController();
        boolean loginResult = authController.login("admin", "admin123");
        assertTrue(loginResult, "Login admin seharusnya berhasil");

        User currentUser = authController.getCurrentUser();
        assertNotNull(currentUser, "Current user tidak boleh null setelah login");
        assertEquals("admin", currentUser.getUsername());
    }

    @Test
    void testLoginFailure() {
        AuthController authController = new AuthController();
        boolean loginResult = authController.login("unknown", "wrongpass");
        assertFalse(loginResult, "Login dengan user salah harus gagal");
        assertNull(authController.getCurrentUser(), "Current user harus null jika login gagal");
    }

    @Test
    void testRegisterSuccess() {
        AuthController authController = new AuthController();
        String username = "barutest";
        boolean regResult = authController.register(
            username, "Baru Test", "testpass", "testpass", "staff", "Jakarta"
        );
        assertTrue(regResult, "Register user baru harus berhasil");
        User currentUser = authController.getCurrentUser();
        assertNotNull(currentUser, "Current user tidak null sesudah register");
        assertEquals(username, currentUser.getUsername());
    }

    @Test
    void testRegisterPasswordMismatch() {
        AuthController authController = new AuthController();
        boolean regResult = authController.register(
            "failuser", "Fail User", "pass1", "pass2", "staff", "Jakarta"
        );
        assertFalse(regResult, "Register dengan password tidak sama harus gagal");
        assertNull(authController.getCurrentUser(), "Current user harus null jika register gagal");
    }

    @Test
    void testLogout() {
        AuthController authController = new AuthController();
        authController.login("izhrr", "izhrr123");
        assertNotNull(authController.getCurrentUser(), "Current user tidak null setelah login");
        authController.logout();
        assertNull(authController.getCurrentUser(), "Current user harus null setelah logout");
    }
}