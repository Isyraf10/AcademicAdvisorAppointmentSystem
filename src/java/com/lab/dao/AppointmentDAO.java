package com.lab.dao;

import com.lab.model.Appointment;
import com.lab.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentDAO - Data Access Object for Appointment management
 * Handles CRUD operations for student appointment bookings
 * 
 * @author isyra
 */
public class AppointmentDAO {

    /**
     * Create a new appointment request
     */
    public boolean createAppointment(int studentId, int advisorId, int scheduleId, 
                                     String title, String description, LocalDate appointmentDate, 
                                     LocalTime startTime, LocalTime endTime, String appointmentType, 
                                     int duration, String reason) {
        String sql = "INSERT INTO appointment (student_id, advisor_id, schedule_id, title, description, " +
                     "appointment_date, start_time, end_time, status, appointment_type, duration, reason) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'Pending', ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, advisorId);
            pstmt.setInt(3, scheduleId);
            pstmt.setString(4, title);
            pstmt.setString(5, description);
            pstmt.setDate(6, java.sql.Date.valueOf(appointmentDate));
            pstmt.setTime(7, java.sql.Time.valueOf(startTime));
            pstmt.setTime(8, java.sql.Time.valueOf(endTime));
            pstmt.setString(9, appointmentType);
            pstmt.setInt(10, duration);
            pstmt.setString(11, reason);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating appointment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get appointment by ID
     */
    public Appointment getAppointmentById(int appointmentId) {
        String sql = "SELECT * FROM appointment WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapRowToAppointment(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving appointment: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all appointments for a specific student
     */
    public List<Appointment> getStudentAppointments(int studentId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE student_id = ? ORDER BY appointment_date DESC, start_time DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapRowToAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * Get all appointments for a specific advisor
     */
    public List<Appointment> getAdvisorAppointments(int advisorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE advisor_id = ? ORDER BY appointment_date DESC, start_time DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, advisorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapRowToAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving advisor appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * Get pending appointments for an advisor
     */
    public List<Appointment> getPendingAppointments(int advisorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE advisor_id = ? AND status = 'Pending' ORDER BY appointment_date, start_time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, advisorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapRowToAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving pending appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * Update appointment status
     */
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        String sql = "UPDATE appointment SET status = ? WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, appointmentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update appointment details
     */
    public boolean updateAppointment(int appointmentId, String title, String description, 
                                    LocalDate appointmentDate, LocalTime startTime, LocalTime endTime) {
        String sql = "UPDATE appointment SET title = ?, description = ?, appointment_date = ?, " +
                     "start_time = ?, end_time = ? WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setDate(3, java.sql.Date.valueOf(appointmentDate));
            pstmt.setTime(4, java.sql.Time.valueOf(startTime));
            pstmt.setTime(5, java.sql.Time.valueOf(endTime));
            pstmt.setInt(6, appointmentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete/Cancel an appointment
     */
    public boolean cancelAppointment(int appointmentId) {
        return updateAppointmentStatus(appointmentId, "Cancelled");
    }

    /**
     * Get all appointments
     */
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment ORDER BY appointment_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapRowToAppointment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * Map ResultSet row to Appointment object (basic model)
     */
    private Appointment mapRowToAppointment(ResultSet rs) throws SQLException {
        int appointmentId = rs.getInt("appointment_id");
        String title = rs.getString("title");
        String type = rs.getString("appointment_type");
        int duration = rs.getInt("duration");
        int availableSlots = 1; // For basic model compatibility
        
        return new Appointment(appointmentId, title, type, duration, availableSlots);
    }

    /**
     * BACKWARD COMPATIBILITY METHODS
     * Deprecated: Use new methods instead
     */
    
    public void insertAppointment(Appointment appointment) {
        // Stub for backward compatibility
    }

    public Appointment selectAppointment(int id) {
        return getAppointmentById(id);
    }

    public List<Appointment> selectAllAppointments() {
        return getAllAppointments();
    }

    public boolean updateAppointment(Appointment appointment) {
        // Stub for backward compatibility
        return false;
    }

    public boolean deleteAppointment(int id) {
        return cancelAppointment(id);
    }
}
