package controllers;

import models.User;
import services.UserService;
import java.util.List;

public class UserController extends BaseController {
    private final UserService userService;

    public UserController() {
        userService = new UserService();
    }

    // Mendapatkan semua user di db
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Ambil detail user
    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    // Update role user
    public boolean updateUserRole(String username, String newRole) {
        if (newRole == null || (!newRole.equals(User.ROLE_STAFF) && !newRole.equals(User.ROLE_ADMIN) && !newRole.equals(User.ROLE_MANAGER))) {
            return false;
        }

        User user = userService.getUserByUsername(username);
        if (user == null) {
            return false;
        }
        
        user.setRole(newRole);
        return userService.updateUser(user);
    }
    
    // Hapus user
    public boolean deleteUser(String username) {
        User user = userService.getUserByUsername(username);
        return user != null && userService.deleteUser(user);
    }

    // Tambah user baru
    public boolean addUser(String username, String fullname, String password, String branch, String role) {
        if (User.usernameExists(username)) return false;
        User user = new User(username, fullname, password, branch, role);
        return user.save();
    }
}