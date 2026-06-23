package com.aas.dao;
import com.aas.model.User;
import com.aas.util.DBConnection;
import java.sql.*;

public class UserDAO {

    public User authenticateUser(String email, String password) {
        User user = null;
        String sql = "SELECT * FROM `user` "
                + "WHERE LOWER(email) = LOWER(?) AND password = ? AND is_active = TRUE";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setPhoneNumber(rs.getString("phone_no"));
                    user.setRole(rs.getString("roles"));

                    updateLastLogin(conn, user.getId());
                }
            }
        } catch (SQLException e) {
            user = authenticateLegacyUser(email, password);
            if (user == null) {
                System.err.println("Login Error using current schema: " + e.getMessage());
            }
        }
        return user;
    }

    private User authenticateLegacyUser(String email, String password) {
        User user = null;
        String sql = "SELECT * FROM users "
                + "WHERE LOWER(email) = LOWER(?) AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setPhoneNumber(rs.getString("phone_number"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Legacy Login Error: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    private void updateLastLogin(Connection conn, int userId) throws SQLException {
        String sql = "UPDATE `user` SET last_login = NOW() WHERE user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        }
    }
    
    public boolean registerUser(User user) {
    String sql = "INSERT INTO `user` (name, email, password, phone_no, roles) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, user.getName());
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, user.getPassword());
        pstmt.setString(4, user.getPhoneNumber());
        pstmt.setString(5, user.getRole());
        
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) { e.printStackTrace(); }
    return false;
}

    private String buildNoMatric(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "USER" + System.currentTimeMillis();
        }

        int atIndex = email.indexOf('@');
        if (atIndex > 0) {
            return email.substring(0, atIndex).toUpperCase();
        }

        return email.toUpperCase();
    }
}