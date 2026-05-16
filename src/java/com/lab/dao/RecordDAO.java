package com.lab.dao;

import com.lab.model.Record;
import com.lab.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RecordDAO - Data Access Object for Record management
 * Handles CRUD operations for consultation records and academic tracking
 * 
 * @author isyra
 */
public class RecordDAO {

    /**
     * Create a new record (usually after appointment is completed)
     */
    public boolean createRecord(Record record) {
        String sql = "INSERT INTO record (appointment_id, student_id, advisor_id, meeting_notes, action_items, student_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, record.getAppointmentId());
            pstmt.setInt(2, record.getStudentId());
            pstmt.setInt(3, record.getAdvisorId());
            pstmt.setString(4, record.getMeetingNotes());
            pstmt.setString(5, record.getActionItems());
            pstmt.setString(6, record.getStudentStatus());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating record: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get record by ID
     */
    public Record getRecordById(int recordId) {
        String sql = "SELECT * FROM record WHERE record_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recordId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapRowToRecord(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving record: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get record by appointment ID
     */
    public Record getRecordByAppointmentId(int appointmentId) {
        String sql = "SELECT * FROM record WHERE appointment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapRowToRecord(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving record by appointment ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all records for a specific student
     */
    public List<Record> getStudentRecords(int studentId) {
        List<Record> records = new ArrayList<>();
        String sql = "SELECT * FROM record WHERE student_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                records.add(mapRowToRecord(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Get all records for a specific advisor
     */
    public List<Record> getAdvisorRecords(int advisorId) {
        List<Record> records = new ArrayList<>();
        String sql = "SELECT * FROM record WHERE advisor_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, advisorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                records.add(mapRowToRecord(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving advisor records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Get all records
     */
    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<>();
        String sql = "SELECT * FROM record ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                records.add(mapRowToRecord(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Update a record
     */
    public boolean updateRecord(Record record) {
        String sql = "UPDATE record SET meeting_notes = ?, feedback = ?, action_items = ?, " +
                     "student_status = ?, record_status = ? WHERE record_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, record.getMeetingNotes());
            pstmt.setString(2, record.getFeedback());
            pstmt.setString(3, record.getActionItems());
            pstmt.setString(4, record.getStudentStatus());
            pstmt.setString(5, record.getRecordStatus());
            pstmt.setInt(6, record.getRecordId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating record: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Add feedback to a record
     */
    public boolean addFeedback(int recordId, String feedback) {
        String sql = "UPDATE record SET feedback = ? WHERE record_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, feedback);
            pstmt.setInt(2, recordId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding feedback: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update student status in record
     */
    public boolean updateStudentStatus(int recordId, String studentStatus) {
        String sql = "UPDATE record SET student_status = ? WHERE record_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentStatus);
            pstmt.setInt(2, recordId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete a record
     */
    public boolean deleteRecord(int recordId) {
        String sql = "DELETE FROM record WHERE record_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recordId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting record: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map ResultSet row to Record object
     */
    private Record mapRowToRecord(ResultSet rs) throws SQLException {
        int recordId = rs.getInt("record_id");
        int appointmentId = rs.getInt("appointment_id");
        int studentId = rs.getInt("student_id");
        int advisorId = rs.getInt("advisor_id");
        String meetingNotes = rs.getString("meeting_notes");
        String feedback = rs.getString("feedback");
        String actionItems = rs.getString("action_items");
        String studentStatus = rs.getString("student_status");
        String recordStatus = rs.getString("record_status");
        String createdAt = rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toString() : "";
        String updatedAt = rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toString() : "";
        
        return new Record(recordId, appointmentId, studentId, advisorId, meetingNotes, feedback, 
                         actionItems, studentStatus, recordStatus, createdAt, updatedAt);
    }
}
