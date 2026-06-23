package com.aas.dao;

import com.aas.model.Record;
import com.aas.model.User;
import com.aas.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordDAO {

    private static final String SELECT_BASE =
        "SELECT r.record_id, r.appointment_id, r.summary, r.feedback, r.action_plan, " +
        "r.record_status, r.created_date, a.student_id, a.advisor_id, " +
        "s.name AS student_name, adv.name AS advisor_name " +
        "FROM records r " +
        "JOIN appointment a ON r.appointment_id = a.appointment_id " +
        "JOIN user s ON a.student_id = s.user_id " +
        "JOIN user adv ON a.advisor_id = adv.user_id ";

    public int createRecord(Record record) {
        final String sql = "INSERT INTO records (appointment_id, summary, feedback, action_plan, record_status, created_date) VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, record.getAppointmentId());
            ps.setString(2, record.getSummary());
            ps.setString(3, record.getFeedback());
            ps.setString(4, record.getActionPlan());
            ps.setString(5, record.getRecordStatus() != null ? record.getRecordStatus() : "Active");
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return -1;
    }

    public List<Record> getAllRecords() {
        return executeListQuery(SELECT_BASE + "ORDER BY r.created_date DESC");
    }

    public List<Record> getRecordsByAdvisor(int advisorId) {
        return executeListQueryWithIntParam(SELECT_BASE + "WHERE a.advisor_id = ? ORDER BY r.created_date DESC", advisorId);
    }

    public List<Record> getRecordsByStudent(int studentId) {
        return executeListQueryWithIntParam(SELECT_BASE + "WHERE a.student_id = ? ORDER BY r.created_date DESC", studentId);
    }

    public Record getRecordById(int recordId) {
        final String sql = SELECT_BASE + "WHERE r.record_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }

    public boolean updateRecordStatus(int recordId, String newStatus) {
        final String sql = "UPDATE records SET record_status = ? WHERE record_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, recordId);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    public boolean deleteRecord(int recordId) {
        final String sql = "DELETE FROM records WHERE record_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) { ex.printStackTrace(); }
        return false;
    }

    public int countSessionsByStudent(int studentId) {
        final String sql = "SELECT COUNT(*) AS total FROM records r JOIN appointments a ON r.appointment_id = a.appointmentid WHERE a.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("total");
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return 0;
    }

    public int countAllRecords() {
        final String sql = "SELECT COUNT(*) AS total FROM records";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException ex) { ex.printStackTrace(); }
        return 0;
    }

    private Record mapRow(ResultSet rs) throws SQLException {
        Record r = new Record();
        r.setRecordId(rs.getInt("record_id"));
        r.setAppointmentId(rs.getInt("appointment_id"));
        r.setSummary(rs.getString("summary"));
        r.setFeedback(rs.getString("feedback"));
        r.setActionPlan(rs.getString("action_plan"));
        r.setRecordStatus(rs.getString("record_status"));
        r.setCreatedDate(rs.getTimestamp("created_date"));
        r.setStudentId(rs.getInt("student_id"));
        r.setAdvisorId(rs.getInt("advisor_id"));
        r.setStudentName(rs.getString("student_name"));
        r.setAdvisorName(rs.getString("advisor_name"));
        return r;
    }

    private List<Record> executeListQuery(String sql) {
        List<Record> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    private List<Record> executeListQueryWithIntParam(String sql, int param) {
        List<Record> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, param);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // NEW METHODS FOR THE DYNAMIC DROPDOWN FLOW
    // ─────────────────────────────────────────────────────────────────────────

    /** Gets a unique list of students assigned to an advisor */
public List<User> getStudentsByAdvisor(int advisorId) {
    List<User> students = new ArrayList<>();
    
    // Using UPPER() ensures database consistency
    String sql = "SELECT DISTINCT u.user_id AS id, u.name, UPPER(u.roles) AS normalized_role " +
             "FROM appointment a " +
             "JOIN user u ON a.student_id = u.user_id " +
             "WHERE a.advisor_id = ?";
                 
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, advisorId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                // Java receives "STUDENT" even if the DB row says "student" or "Student"
                u.setRole(rs.getString("normalized_role")); 
                students.add(u);
            }
        }
    } catch (SQLException ex) { ex.printStackTrace(); }
    return students;
}

    /** Inner DTO for the AJAX dropdown */
    public static class AppointmentDTO {
        public int id;
    }

    /** Gets appointments for a student that do NOT have a record yet */
    public List<AppointmentDTO> getUnloggedAppointmentsByStudent(int studentId, int advisorId) {
        List<AppointmentDTO> appts = new ArrayList<>();
        String sql = "SELECT a.appointment_id AS id FROM appointment a LEFT JOIN records r ON a.appointment_id = r.appointment_id WHERE a.student_id = ? AND a.advisor_id = ? AND r.record_id IS NULL";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, advisorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AppointmentDTO dto = new AppointmentDTO();
                    dto.id = rs.getInt("id");
                    appts.add(dto);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return appts;
    }
}