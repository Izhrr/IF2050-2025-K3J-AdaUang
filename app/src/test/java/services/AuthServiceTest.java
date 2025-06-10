package services;

import models.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import database.DatabaseSeeder;

public class AuthServiceTest {

    static AuthService authService;

    @BeforeAll
    static void setup() {
        // Jalankan seeder agar database konsisten
        DatabaseSeeder seeder = new DatabaseSeeder();
        assertTrue(seeder.runSeeder(), "Seeder harus sukses dijalankan");
        authService = new AuthService();
    }

    @Test
    void testLoginSuccess() {
        // Asumsi user 'admin' dengan password 'admin123' ada di seeder
        User user = authService.login("admin", "admin123");
        assertNotNull(user, "User admin harus bisa login");
        assertEquals("admin", user.getUsername());
    }

    @Test
    void testLoginFailWrongPassword() {
        User user = authService.login("admin", "salahpw");
        assertNull(user, "Login dengan password salah harus gagal");
    }

    @Test
    void testLoginFailUserNotFound() {
        User user = authService.login("tidakada", "pw");
        assertNull(user, "Login dengan username tidak ada harus gagal");
    }

    @Test
    void testLoginNullInput() {
        assertNull(authService.login(null, "pw"));
        assertNull(authService.login("admin", null));
        assertNull(authService.login(null, null));
    }

    @Test
    void testRegisterSuccess() {
        String username = "newuser";
        User user = authService.register(username, "New User", "passwordku", "passwordku", User.ROLE_STAFF, "Jakarta");
        assertNotNull(user, "Register user baru harus berhasil");
        assertEquals(username, user.getUsername());
    }

    @Test
    void testRegisterFailPasswordMismatch() {
        User user = authService.register("user2", "User Dua", "pw1", "pw2", User.ROLE_STAFF, "Jakarta");
        assertNull(user, "Register dengan password tidak sama harus gagal");
    }

    @Test
    void testRegisterFailUsernameExists() {
        // Asumsi "admin" sudah ada dari seeder
        User user = authService.register("admin", "Si Admin", "admin123", "admin123", User.ROLE_ADMIN, "Jakarta");
        assertNull(user, "Register dengan username sudah ada harus gagal");
    }

    @Test
    void testRegisterFailInvalidRole() {
        User user = authService.register("userrole", "User Role", "password", "password", "INVALID_ROLE", "Jakarta");
        assertNotNull(user, "Register dengan role salah tetap berhasil (role jadi staff)");
        assertEquals(User.ROLE_STAFF, user.getRole());
    }

    @Test
    void testRegisterNullInput() {
        assertNull(authService.register(null, "nama", "pw", "pw", User.ROLE_STAFF, "Jakarta"));
        assertNull(authService.register("user", null, "pw", "pw", User.ROLE_STAFF, "Jakarta"));
        assertNull(authService.register("user", "nama", null, "pw", User.ROLE_STAFF, "Jakarta"));
        assertNull(authService.register("user", "nama", "pw", null, User.ROLE_STAFF, "Jakarta"));
        assertNull(authService.register("user", "nama", "pw", "pw", User.ROLE_STAFF, null));
    }
}