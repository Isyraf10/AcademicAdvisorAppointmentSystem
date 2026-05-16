package com.lab.model;

/**
 * UserRole Constants - Defines available user roles in the system
 * Used for role-based access control (RBAC)
 * 
 * @author isyra
 */
public class UserRole {
    // Role constants
    public static final String ADMIN = "admin";
    public static final String ADVISOR = "advisor";
    public static final String STUDENT = "student";

    /**
     * Validate if a role is valid
     */
    public static boolean isValidRole(String role) {
        if (role == null) {
            return false;
        }
        return role.equals(ADMIN) || role.equals(ADVISOR) || role.equals(STUDENT);
    }

    /**
     * Check if user has admin role
     */
    public static boolean isAdmin(String role) {
        return role != null && role.equals(ADMIN);
    }

    /**
     * Check if user has advisor role
     */
    public static boolean isAdvisor(String role) {
        return role != null && role.equals(ADVISOR);
    }

    /**
     * Check if user has student role
     */
    public static boolean isStudent(String role) {
        return role != null && role.equals(STUDENT);
    }

    /**
     * Check if user can perform write operations (update/delete)
     * Admin and Advisor can write
     */
    public static boolean canWrite(String role) {
        return role != null && (role.equals(ADMIN) || role.equals(ADVISOR));
    }

    /**
     * Check if user can delete
     * Only Admin can delete
     */
    public static boolean canDelete(String role) {
        return role != null && role.equals(ADMIN);
    }
}
