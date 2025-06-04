package models;

import config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class User extends BaseModel {
    private int userId;           // Primary key
    private String username;      // Login identifier
    private String fullName;      // Display name
    private String role;          // user/admin
    private String password;      // Authentication
    
    private static DatabaseConfig config = DatabaseConfig.getInstance();
    
    // Role constants
    public static final String ROLE_USER = "user";
    public static final String ROLE_ADMIN = "admin";
    
    // Constructors
    public User() {
        this.role = ROLE_USER; // Default role
    }
    
    public User(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }
    
    public User(String username, String fullName, String role, String password) {
        this.username = username;
        this.fullName = fullName;
        this.role = role != null ? role : ROLE_USER;
        this.password = password;
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
                    " (username, full_name, role, password) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, username);
            stmt.setString(2, fullName);
            stmt.setString(3, role);
            stmt.setString(4, password);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get the generated user_id
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.userId = generatedKeys.getInt(1);
                        this.id = this.userId; // Keep BaseModel id in sync
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    private boolean update() throws SQLException {
        String sql = "UPDATE " + config.getUsersTableName() + 
                    " SET username = ?, full_name = ?, role = ?, password = ? WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, fullName);
            stmt.setString(3, role);
            stmt.setString(4, password);
            stmt.setInt(5, userId);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean delete() {
        if (isNewRecord()) {
            return false;
        }
        
        try {
            String sql = "DELETE FROM " + config.getUsersTableName() + " WHERE user_id = ?";
            
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, userId);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            if (config.isDebugMode()) {
                System.err.println(" Error deleting user: " + e.getMessage());
            }
            return false;
        }
    }
    
    // Static finder methods (equivalent to old MyJDBC methods)
    
    public static User findById(int userId) {
        try {
            String sql = "SELECT * FROM " + config.getUsersTableName() + " WHERE user_id = ?";
            
            try (Connection conn = dbConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, userId);
                
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
    
    // Main authentication method (replaces old MyJDBC.validateLogin)
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
            String sql = "SELECT * FROM " + config.getUsersTableName() + " ORDER BY created_at DESC";
            
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
                        " WHERE role = ? ORDER BY created_at DESC";
            
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
        user.setUserId(rs.getInt("user_id"));
        user.setId(rs.getInt("user_id")); // Keep BaseModel id in sync
        user.setUsername(rs.getString("username"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setPassword(rs.getString("password"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
    
    // Validation methods
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               fullName != null && !fullName.trim().isEmpty() &&
               role != null && (role.equals(ROLE_USER) || role.equals(ROLE_ADMIN)) &&
               username.length() >= 3 && username.length() <= 50 &&
               password.length() >= 6;
    }
    
    public static boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }
    
    // Check if this is a new record
    @Override
    public boolean isNewRecord() {
        return userId == 0;
    }
    
    // Role checking methods
    public boolean isAdmin() {
        return ROLE_ADMIN.equals(role);
    }
    
    public boolean isUser() {
        return ROLE_USER.equals(role);
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
        this.id = userId; // Keep BaseModel id in sync
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
    
    // toString for debugging
    @Override
    public String toString() {
        return String.format("User{userId=%d, username='%s', fullName='%s', role='%s'}", 
                           userId, username, fullName, role);
    }
    
    // Display name for UI
    public String getDisplayName() {
        return fullName != null && !fullName.trim().isEmpty() ? fullName : username;
    }
}