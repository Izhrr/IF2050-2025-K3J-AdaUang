package services;

import models.User;

public class AuthService {
    // Login: return User jika sukses, null jika gagal
    public User login(String username, String password) {
        if (username == null || password == null) return null;
        return User.authenticate(username, password);
    }

    // Register: return User jika sukses, null jika gagal
    public User register(String username, String fullname, String password, String confirmPassword, String role, String branch) {
        if (username == null || fullname == null || password == null || confirmPassword == null || branch == null)
            return null;
        if (!password.equals(confirmPassword)) return null;
        if (User.usernameExists(username)) return null;
        if (role == null || (!role.equals(User.ROLE_USER) && !role.equals(User.ROLE_ADMIN))) {
            role = User.ROLE_USER;
        }
        User user = new User(username, fullname, password, branch, role);
        if (!user.isValid()) return null;
        boolean success = user.save();
        return success ? user : null;
    }
}