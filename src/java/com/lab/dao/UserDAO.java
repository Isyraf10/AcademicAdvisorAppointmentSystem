package com.lab.dao;

import com.lab.model.User;
import com.lab.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDAO - Data Access Object for User authentication
 * Handles database operations for user lookup and authentication
 * 
 * @author isyra
 */
public class UserDAO {

    /**
     * Authenticate user by noMatric and password
     * Returns User object if authentication successful, null otherwise
     */
    public User authenticateUser(String noMatric, String password) {
        String query = "SELECT noMatric, password, roles FROM user WHERE noMatric = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, noMatric);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    String roles = rs.getString("roles");

                    // Verify password (simple comparison for now - adjust if using hashing)
                    if (password.equals(storedPassword)) {
                        // Return authenticated user
                        return new User(noMatric, storedPassword, roles);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Get user by noMatric
     */
    public User getUserByNoMatric(String noMatric) {
        String query = "SELECT noMatric, password, roles FROM user WHERE noMatric = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, noMatric);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    String roles = rs.getString("roles");
                    
                    return new User(noMatric, password, roles);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Get user role by noMatric
     */
    public String getUserRole(String noMatric) {
        String query = "SELECT roles FROM user WHERE noMatric = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, noMatric);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("roles");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user role: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
