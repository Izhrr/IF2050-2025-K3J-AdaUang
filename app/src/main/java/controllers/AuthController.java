package controllers;

import services.AuthService;
import models.User;

public class AuthController {
    private AuthService authService;
    private User currentUser;

    public AuthController() {
        authService = new AuthService();
    }

    // Proses login, return true jika sukses
    public boolean login(String username, String password) {
        User user = authService.login(username, password);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }

    // Proses register, return true jika sukses
    public boolean register(String username, String fullname, String password, String confirmPassword, String role, String branch) {
        User user = authService.register(username, fullname, password, confirmPassword, role, branch);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }

    // Mendapatkan user yang sedang login
    public User getCurrentUser() {
        return currentUser;
    }

    // Logout user
    public void logout() {
        currentUser = null;
    }
}