package controllers;

import models.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import database.DatabaseSeeder;

import java.util.List;

public class UserControllerTest {

    static UserController userController;

    @BeforeAll
    static void setup() {
        // Seed database agar test konsisten
        DatabaseSeeder seeder = new DatabaseSeeder();
        assertTrue(seeder.runSeeder(), "Seeder harus sukses dijalankan");
        userController = new UserController();
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userController.getAllUsers();
        assertNotNull(users, "List user tidak boleh null");
        assertTrue(users.size() >= 4, "Harus ada minimal 4 user dari seeder");
    }

    @Test
    void testGetUserByUsernameSuccess() {
        User user = userController.getUserByUsername("admin");
        assertNotNull(user, "User admin harus ditemukan");
        assertEquals("admin", user.getUsername());
        assertEquals("Administrator", user.getFullname());
    }

    @Test
    void testGetUserByUsernameFail() {
        User user = userController.getUserByUsername("tidakada");
        assertNull(user, "User yang tidak ada harus null");
    }

    @Test
    void testUpdateUserRoleSuccess() {
        boolean result = userController.updateUserRole("testuser", User.ROLE_MANAGER);
        assertTrue(result, "Update role user harus berhasil");
        User user = userController.getUserByUsername("testuser");
        assertEquals(User.ROLE_MANAGER, user.getRole());
    }

    @Test
    void testUpdateUserRoleFailWithInvalidRole() {
        boolean result = userController.updateUserRole("testuser", "INVALID_ROLE");
        assertFalse(result, "Update role dengan role tidak valid harus gagal");
    }

    @Test
    void testUpdateUserRoleFailUserNotFound() {
        boolean result = userController.updateUserRole("tidakada", User.ROLE_ADMIN);
        assertFalse(result, "Update role user yang tidak ada harus gagal");
    }

    @Test
    void testDeleteUserSuccess() {
        // Tambah user dummy dulu
        String username = "hapususer";
        boolean add = userController.addUser(username, "User Hapus", "123", "Jakarta", User.ROLE_STAFF);
        assertTrue(add, "Tambah user untuk delete test harus berhasil");

        boolean result = userController.deleteUser(username);
        assertTrue(result, "Delete user harus berhasil");

        User user = userController.getUserByUsername(username);
        assertNull(user, "User yang dihapus tidak boleh ditemukan lagi");
    }

    @Test
    void testDeleteUserFail() {
        boolean result = userController.deleteUser("tidakada");
        assertFalse(result, "Delete user yang tidak ada harus gagal");
    }

    @Test
    void testAddUserSuccess() {
        String username = "userbaru";
        boolean result = userController.addUser(username, "User Baru", "pwbaru", "Bandung", User.ROLE_STAFF);
        assertTrue(result, "Tambah user baru harus berhasil");

        User user = userController.getUserByUsername(username);
        assertNotNull(user, "User baru harus bisa ditemukan");
        assertEquals(username, user.getUsername());
    }

    @Test
    void testAddUserFailDuplicateUsername() {
        boolean result = userController.addUser("admin", "Si Admin", "pw", "Jakarta", User.ROLE_ADMIN);
        assertFalse(result, "Tambah user dengan username sudah ada harus gagal");
    }
}