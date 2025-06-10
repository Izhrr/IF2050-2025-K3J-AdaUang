package models;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.Timestamp;

public abstract class BaseModel {
    protected int id;
    protected Timestamp createdAt;
    protected Timestamp updatedAt;
    
    // Database connection
    protected static DatabaseConnection dbConnection = DatabaseConnection.getInstance();
    
    // Abstract methods that child classes must implement
    public abstract boolean save();
    public abstract boolean delete();
    
    // Common getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper method to get database connection
    protected Connection getConnection() {
        return dbConnection.getConnection();
    }
    
    // Helper method to check if this is a new record
    public boolean isNewRecord() {
        return id == 0;
    }
}