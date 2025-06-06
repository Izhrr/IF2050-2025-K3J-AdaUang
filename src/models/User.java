package models;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import config.*;

public class User extends BaseModel {
    private int id_user;           // Primary key
    private String username;      // Login identifier
    private String fullname;      // Display name
    private String password;      // Authentication
    private String branch;        // Cabang
    private String role;          // user/admin

    private static DatabaseConfig config = DatabaseConfig.getInstance();

    public static final String ROLE_STAFF = "staff";
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_MANAGER = "manager";

    // Constructors
    public User() {
        this.role = ROLE_STAFF; // Default role
    }

    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }

    public User(String username, String fullname, String password, String branch, String role) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.branch = branch;
        this.role = (role != null) ? role : ROLE_STAFF;
    }

    // CRUD Operations

    @Override
    public boolean save() {
        try {
            if (isNewRecord()) {
                return insert();
            } else {
                return update();
            }
        } catch (SQLException e) {
            if (config.isDebugMode()) {
                System.err.println(" Error saving user: " + e.getMessage());
            }
            return false;
        }
    }

    private boolean insert() throws SQLException {
        String sql = "INSERT INTO " + config.getUsersTableName() +
                " (username, fullname, password, branch, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, fullname);
            stmt.setString(3, password);
            stmt.setString(4, branch);
            stmt.setString(5, role);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Get the generated id_user
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.id_user = generatedKeys.getInt(1);
                        this.id = this.id_user; // Keep BaseModel id in sync
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean update() throws SQLException {
        String sql = "UPDATE " + config.getUsersTableName() +
                " SET username = ?, fullname = ?, password = ?, branch = ?, role = ? WHERE id_user = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, fullname);
            stmt.setString(3, password);
            stmt.setString(4, branch);
            stmt.setString(5, role);
            stmt.setInt(6, id_user);

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete() {
        if (isNewRecord()) {
            return false;
        }

        try {
            String sql = "DELETE FROM " + config.getUsersTableName() + " WHERE id_user = ?";

            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id_user);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            if (config.isDebugMode()) {
                System.err.println(" Error deleting user: " + e.getMessage());
            }
            return false;
        }
    }

    // Static finder methods

    public static User findById(int id_user) {
        try {
            String sql = "SELECT * FROM " + config.getUsersTableName() + " WHERE id_user = ?";

            try (Connection conn = dbConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id_user);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return createUserFromResultSet(rs);
                    }
                }
            }
        } catch (SQLException e) {
            if (config.isDebugMode()) {
                System.err.println(" Error finding user by ID: " + e.getMessage());
            }
        }
        return null;
    }

    public static User findByUsername(String username) {
        try {
            String sql = "SELECT * FROM " + config.getUsersTableName() + " WHERE username = ?";

            try (Connection conn = dbConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return createUserFromResultSet(rs);
                    }
                }
            }
        } catch (SQLException e) {
            if (config.isDebugMode()) {
                System.err.println(" Error finding user by username: " + e.getMessage());
            }
        }
        return null;
    }

    // Main authentication method
    public static User authenticate(String username, String password) {
        try {
            String sql = "SELECT * FROM " + config.getUsersTableName() +
                    " WHERE username = ? AND password = ?";

            try (Connection conn = dbConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username);
                stmt.setString(2, password);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return createUserFromResultSet(rs);
                    }
                }
            }
        } catch (SQLException e) {
            if (config.isDebugMode()) {
                System.err.println(" Error authenticating user: " + e.getMessage());
            }
        }
        return null;
    }

    public static List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            String sql = "SELECT * FROM " + config.getUsersTableName() + " ORDER BY id_user ASC";

            try (Connection conn = dbConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(createUserFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            if (config.isDebugMode()) {
                System.err.println(" Error finding all users: " + e.getMessage());
            }
        }
        return users;
    }

    public static List<User> findByRole(String role) {
        List<User> users = new ArrayList<>();

        try {
            String sql = "SELECT * FROM " + config.getUsersTableName() +
                    " WHERE role = ? ORDER BY id_user DESC";

            try (Connection conn = dbConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, role);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        users.add(createUserFromResultSet(rs));
                    }
                }
            }
        } catch (SQLException e) {
            if (config.isDebugMode()) {
                System.err.println(" Error finding users by role: " + e.getMessage());
            }
        }

        return users;
    }

    // Helper method to create User object from ResultSet
    private static User createUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId_user(rs.getInt("id_user"));
        user.setId(rs.getInt("id_user")); // Keep BaseModel id in sync
        user.setUsername(rs.getString("username"));
        user.setFullname(rs.getString("fullname"));
        user.setPassword(rs.getString("password"));
        user.setBranch(rs.getString("branch"));
        user.setRole(rs.getString("role"));
        // created_at, updated_at jika diperlukan bisa disetting di sini juga.
        return user;
    }

    // Validation methods
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
                password != null && !password.trim().isEmpty() &&
                fullname != null && !fullname.trim().isEmpty() &&
                branch != null && !branch.trim().isEmpty() &&
                role != null && (role.equals(ROLE_STAFF) || role.equals(ROLE_ADMIN) || role.equals(ROLE_MANAGER)) &&
                username.length() >= 3 && username.length() <= 50 &&
                password.length() >= 6;
    }

    public static boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }

    // Check if this is a new record
    @Override
    public boolean isNewRecord() {
        return id_user == 0;
    }

    // Role checking methods
    public boolean isAdmin() {
        return ROLE_ADMIN.equals(role);
    }

    public boolean isStaff() {
        return ROLE_STAFF.equals(role);
    }

    public boolean isManager() {
        return ROLE_MANAGER.equals(role);
    }

    // Getters and Setters
    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
        this.id = id_user; // Keep BaseModel id in sync
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    // toString for debugging
    @Override
    public String toString() {
        return String.format("User{id_user=%d, username='%s', fullname='%s', branch='%s', role='%s'}",
                id_user, username, fullname, branch, role);
    }

    // Display name for UI
    public String getDisplayName() {
        return fullname != null && !fullname.trim().isEmpty() ? fullname : username;
    }
}