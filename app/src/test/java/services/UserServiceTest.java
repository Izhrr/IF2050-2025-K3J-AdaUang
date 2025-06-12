package services;

import models.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import database.DatabaseSeeder;

import java.util.List;

public class UserServiceTest {

    static UserService userService;

    @BeforeAll
    static void setup() {
        DatabaseSeeder seeder = new DatabaseSeeder();
        assertTrue(seeder.runSeeder(), "Seeder harus sukses dijalankan");
        userService = new UserService();
    }

    @Test
    void testGetUserByUsernameSuccess() {
        User user = userService.getUserByUsername("admin");
        assertNotNull(user, "User admin harus ditemukan");
        assertEquals("admin", user.getUsername());
    }

    @Test
    void testGetUserByUsernameFail() {
        User user = userService.getUserByUsername("tidakada");
        assertNull(user, "User tidak ada harus null");
    }

    @Test
    void testGetUserByIdSuccess() {
        User user1 = userService.getUserByUsername("admin");
        assertNotNull(user1);

        User user2 = userService.getUserById(user1.getId_user());
        assertNotNull(user2);
        assertEquals(user1.getUsername(), user2.getUsername());
    }

    @Test
    void testGetUserByIdFail() {
        User user = userService.getUserById(-12345);
        assertNull(user, "User dengan id tidak valid harus null");
    }

    @Test
    void testGetUsersByRole() {
        List<User> admins = userService.getUsersByRole(User.ROLE_ADMIN);
        assertNotNull(admins);
        assertTrue(admins.stream().anyMatch(u -> "admin".equals(u.getUsername())));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertTrue(users.size() >= 1);
    }

    @Test
    void testUpdateUser() {
        User user = userService.getUserByUsername("admin");
        assertNotNull(user);
        String oldFullName = user.getFullname();
        user.setFullname("Nama Admin Baru");
        boolean updated = userService.updateUser(user);
        assertTrue(updated, "Update user harus berhasil");

        User updatedUser = userService.getUserByUsername("admin");
        assertNotNull(updatedUser);
        assertEquals("Nama Admin Baru", updatedUser.getFullname());

        // Kembalikan ke nama lama supaya test lain tetap valid
        user.setFullname(oldFullName);
        userService.updateUser(user);
    }

    @Test
    void testDeleteUserAndRestore() {
        String username = "hapususer";
        User newUser = new User(username, "User Hapus", "pw123456", "Jakarta", User.ROLE_STAFF);
        assertTrue(newUser.save(), "Buat user baru harus sukses");

        User created = userService.getUserByUsername(username);
        assertNotNull(created);

        boolean deleted = userService.deleteUser(created);
        assertTrue(deleted, "Delete user harus berhasil");
        assertNull(userService.getUserByUsername(username), "User sudah dihapus");

        // Restore agar database tetap stabil
        User restore = new User(username, "User Hapus", "pw123456", "Jakarta", User.ROLE_STAFF);
        assertTrue(restore.save(), "Restore user harus sukses");
    }
}