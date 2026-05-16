package com.lab.dao;

import com.lab.model.Schedule;
import com.lab.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ScheduleDAO - Data Access Object for Schedule management
 * Handles CRUD operations for advisor consultation schedules
 * 
 * @author isyra
 */
public class ScheduleDAO {

    /**
     * Create a new schedule slot
     */
    public boolean createSchedule(Schedule schedule) {
        String sql = "INSERT INTO schedule (advisor_id, date, start_time, end_time, status, max_capacity, current_bookings, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, schedule.getAdvisorId());
            pstmt.setDate(2, java.sql.Date.valueOf(schedule.getDate()));
            pstmt.setTime(3, java.sql.Time.valueOf(schedule.getStartTime()));
            pstmt.setTime(4, java.sql.Time.valueOf(schedule.getEndTime()));
            pstmt.setString(5, schedule.getStatus());
            pstmt.setInt(6, schedule.getMaxCapacity());
            pstmt.setInt(7, schedule.getCurrentBookings());
            pstmt.setString(8, schedule.getNotes());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating schedule: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieve a schedule by ID
     */
    public Schedule getScheduleById(int scheduleId) {
        String sql = "SELECT * FROM schedule WHERE schedule_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, scheduleId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapRowToSchedule(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving schedule: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all schedules for a specific advisor
     */
    public List<Schedule> getAdvisorSchedules(int advisorId) {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedule WHERE advisor_id = ? ORDER BY date, start_time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, advisorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                schedules.add(mapRowToSchedule(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving advisor schedules: " + e.getMessage());
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Get available schedules for a specific date and advisor
     */
    public List<Schedule> getAvailableSchedules(int advisorId, LocalDate date) {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedule WHERE advisor_id = ? AND date = ? AND status = 'Available' " +
                     "AND current_bookings < max_capacity ORDER BY start_time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, advisorId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                schedules.add(mapRowToSchedule(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving available schedules: " + e.getMessage());
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Get all schedules within a date range
     */
    public List<Schedule> getSchedulesByDateRange(int advisorId, LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedule WHERE advisor_id = ? AND date BETWEEN ? AND ? ORDER BY date, start_time";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, advisorId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                schedules.add(mapRowToSchedule(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving schedules by date range: " + e.getMessage());
            e.printStackTrace();
        }
        return schedules;
    }

    /**
     * Update a schedule
     */
    public boolean updateSchedule(Schedule schedule) {
        String sql = "UPDATE schedule SET date = ?, start_time = ?, end_time = ?, status = ?, " +
                     "max_capacity = ?, notes = ? WHERE schedule_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(schedule.getDate()));
            pstmt.setTime(2, java.sql.Time.valueOf(schedule.getStartTime()));
            pstmt.setTime(3, java.sql.Time.valueOf(schedule.getEndTime()));
            pstmt.setString(4, schedule.getStatus());
            pstmt.setInt(5, schedule.getMaxCapacity());
            pstmt.setString(6, schedule.getNotes());
            pstmt.setInt(7, schedule.getScheduleId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating schedule: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update the status of a schedule
     */
    public boolean updateScheduleStatus(int scheduleId, String status) {
        String sql = "UPDATE schedule SET status = ? WHERE schedule_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, scheduleId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating schedule status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Increment current bookings for a schedule
     */
    public boolean incrementBookings(int scheduleId) {
        String sql = "UPDATE schedule SET current_bookings = current_bookings + 1 WHERE schedule_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, scheduleId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error incrementing bookings: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Decrement current bookings for a schedule
     */
    public boolean decrementBookings(int scheduleId) {
        String sql = "UPDATE schedule SET current_bookings = GREATEST(0, current_bookings - 1) WHERE schedule_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, scheduleId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error decrementing bookings: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete a schedule
     */
    public boolean deleteSchedule(int scheduleId) {
        String sql = "DELETE FROM schedule WHERE schedule_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, scheduleId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting schedule: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map ResultSet row to Schedule object
     */
    private Schedule mapRowToSchedule(ResultSet rs) throws SQLException {
        int scheduleId = rs.getInt("schedule_id");
        int advisorId = rs.getInt("advisor_id");
        LocalDate date = rs.getDate("date").toLocalDate();
        LocalTime startTime = rs.getTime("start_time").toLocalTime();
        LocalTime endTime = rs.getTime("end_time").toLocalTime();
        String status = rs.getString("status");
        int maxCapacity = rs.getInt("max_capacity");
        int currentBookings = rs.getInt("current_bookings");
        String notes = rs.getString("notes");
        String createdAt = rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toString() : "";
        String updatedAt = rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toString() : "";
        
        return new Schedule(scheduleId, advisorId, date, startTime, endTime, status, maxCapacity, currentBookings, notes, createdAt, updatedAt);
    }
}
