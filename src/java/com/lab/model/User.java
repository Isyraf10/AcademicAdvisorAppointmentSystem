package com.lab.model;

/**
 * User Model Class - Represents a user in the authentication system
 * 
 * @author isyra
 */
public class User {
    private int userId;
    private String noMatric;      // Student/Advisor matric number
    private String passwordHash;  // Hashed password
    private String roles;         // Role: admin, advisor, student
    private boolean isActive;
    private long lastLogin;

    /**
     * Full constructor
     */
    public User(int userId, String noMatric, String passwordHash, String roles, boolean isActive, long lastLogin) {
        this.userId = userId;
        this.noMatric = noMatric;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.isActive = isActive;
        this.lastLogin = lastLogin;
    }

    /**
     * Constructor for authentication (without ID and lastLogin)
     */
    public User(String noMatric, String passwordHash, String roles) {
        this.noMatric = noMatric;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.isActive = true;
    }

    // Getters and Setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNoMatric() {
        return noMatric;
    }

    public void setNoMatric(String noMatric) {
        this.noMatric = noMatric;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", noMatric='" + noMatric + '\'' +
                ", roles='" + roles + '\'' +
                ", isActive=" + isActive +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
