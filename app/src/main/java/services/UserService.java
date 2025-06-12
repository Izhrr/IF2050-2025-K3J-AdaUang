package services;

import models.User;
import java.util.List;

public class UserService {
    // Cari user berdasarkan username
    public User getUserByUsername(String username) {
        return User.findByUsername(username);
    }

    // Cari user berdasarkan user_id
    public User getUserById(int userId) {
        return User.findById(userId);
    }

    // Cari semua user dengan role tertentu
    public List<User> getUsersByRole(String role) {
        return User.findByRole(role);
    }

    // Cari semua user
    public List<User> getAllUsers() {
        return User.findAll();
    }

    // Update profil user
    public boolean updateUser(User user) {
        return user != null && user.save();
    }

    // Delete user
    public boolean deleteUser(User user) {
        return user != null && user.delete();
    }
}