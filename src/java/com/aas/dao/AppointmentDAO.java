package com.aas.dao;

import com.aas.model.Appointment;
import com.aas.model.TimeSlot;
import com.aas.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public List<TimeSlot> getAvailableSlotsByStudentMentor(int studentId) {
        List<TimeSlot> list = new ArrayList<>();
        String sql = "SELECT s.schedule_id, s.advisor_id, u.name AS advisor_name, " +
                     "s.schedule_date, s.start_time, s.end_time, s.location, s.status " +
                     "FROM schedule s JOIN user u ON s.advisor_id = u.user_id " +
                     "JOIN mentor_assignment ma ON s.advisor_id = ma.advisor_id " +
                     "WHERE ma.student_id = ? AND s.status = 'Available' " +
                     "ORDER BY s.schedule_date, s.start_time";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TimeSlot slot = new TimeSlot();
                    slot.setScheduleId(rs.getInt("schedule_id"));
                    slot.setAdvisorId(rs.getInt("advisor_id"));
                    slot.setAdvisorName(rs.getString("advisor_name"));
                    slot.setDate(rs.getDate("schedule_date").toLocalDate());
                    slot.setStartTime(rs.getTime("start_time").toLocalTime());
                    slot.setEndTime(rs.getTime("end_time").toLocalTime());
                    slot.setLocation(rs.getString("location"));
                    slot.setStatus(rs.getString("status"));
                    list.add(slot);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean createAppointment(int studentId, int scheduleId, String appointmentType, String additionalNotes) {
        String selectScheduleSql = "SELECT advisor_id FROM schedule WHERE schedule_id = ? AND status = 'Available' FOR UPDATE";
        String insertSql = "INSERT INTO appointment (student_id, advisor_id, schedule_id, appointment_type, additional_notes, status) VALUES (?, ?, ?, ?, ?, 'Pending')";
        String updateScheduleSql = "UPDATE schedule SET status = 'Booked' WHERE schedule_id = ? AND status = 'Available'";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            int advisorId;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectScheduleSql)) {
                selectStmt.setInt(1, scheduleId);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (!rs.next()) { conn.rollback(); return false; }
                    advisorId = rs.getInt("advisor_id");
                }
            }
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, studentId);
                insertStmt.setInt(2, advisorId);
                insertStmt.setInt(3, scheduleId);
                insertStmt.setString(4, appointmentType);
                insertStmt.setString(5, additionalNotes);
                insertStmt.executeUpdate();
            }
            try (PreparedStatement updateStmt = conn.prepareStatement(updateScheduleSql)) {
                updateStmt.setInt(1, scheduleId);
                if (updateStmt.executeUpdate() == 0) { conn.rollback(); return false; }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace(); return false;
        } finally { if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); } } }
    }

    public List<Appointment> getStudentAppointments(int studentId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, s.schedule_date, s.start_time, s.end_time, s.location, u.name AS advisor_name " +
                     "FROM appointment a JOIN schedule s ON a.schedule_id = s.schedule_id " +
                     "JOIN user u ON a.advisor_id = u.user_id " +
                     "WHERE a.student_id = ? ORDER BY a.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { list.add(mapRow(rs, false)); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Appointment> getPendingAppointmentsForAdvisor(int advisorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, u.name AS student_name, s.schedule_date, s.start_time, s.end_time, s.location " +
                     "FROM appointment a JOIN user u ON a.student_id = u.user_id " +
                     "JOIN schedule s ON a.schedule_id = s.schedule_id " +
                     "WHERE a.advisor_id = ? AND a.status = 'Pending' ORDER BY a.created_at ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, advisorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { list.add(mapRow(rs, true)); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, s.schedule_date, s.start_time, s.end_time, s.location, " +
                     "student.name AS student_name, advisor.name AS advisor_name " +
                     "FROM appointment a JOIN schedule s ON a.schedule_id = s.schedule_id " +
                     "JOIN user student ON a.student_id = student.user_id " +
                     "JOIN user advisor ON a.advisor_id = advisor.user_id " +
                     "ORDER BY a.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) { list.add(mapRow(rs, false)); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean updateAppointmentStatus(int appointmentId, String newStatus) {
        String sql = "UPDATE appointment SET status = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, appointmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean rejectAppointmentWithReason(int appointmentId, String reason) {
        String updateSql = "UPDATE appointment SET status = 'Rejected', rejection_reason = ? WHERE appointment_id = ?";
        String releaseSql = "UPDATE schedule SET status = 'Available' WHERE schedule_id = (SELECT schedule_id FROM appointment WHERE appointment_id = ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt1 = conn.prepareStatement(updateSql)) {
                pstmt1.setString(1, reason);
                pstmt1.setInt(2, appointmentId);
                pstmt1.executeUpdate();
            }
            try (PreparedStatement pstmt2 = conn.prepareStatement(releaseSql)) {
                pstmt2.setInt(1, appointmentId);
                pstmt2.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace(); return false;
        } finally { if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); } } }
    }

    public boolean editAppointmentDetails(int appointmentId, String newType, String newNotes) {
        String sql = "UPDATE appointment SET appointment_type = ?, additional_notes = ? WHERE appointment_id = ? AND status = 'Pending'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newType);
            pstmt.setString(2, newNotes);
            pstmt.setInt(3, appointmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean hardDeleteAppointment(int appointmentId) {
        String releaseSql = "UPDATE schedule SET status = 'Available' WHERE schedule_id = (SELECT schedule_id FROM appointment WHERE appointment_id = ?)";
        String deleteSql = "DELETE FROM appointment WHERE appointment_id = ?";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(releaseSql)) { ps1.setInt(1, appointmentId); ps1.executeUpdate(); }
            try (PreparedStatement ps2 = conn.prepareStatement(deleteSql)) { ps2.setInt(1, appointmentId); ps2.executeUpdate(); }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace(); return false;
        } finally { if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); } } }
    }

    private Appointment mapRow(ResultSet rs, boolean includeStudentName) throws SQLException {
        Appointment apt = new Appointment();
        apt.setAppointmentId(rs.getInt("appointment_id"));
        apt.setStudentId(rs.getInt("student_id"));
        apt.setAdvisorId(rs.getInt("advisor_id"));
        apt.setScheduleId(rs.getInt("schedule_id"));
        apt.setAppointmentType(rs.getString("appointment_type"));
        apt.setAdditionalNotes(rs.getString("additional_notes"));
        apt.setStatus(rs.getString("status"));
        apt.setRejectionReason(rs.getString("rejection_reason"));
        apt.setAppointmentDate(rs.getDate("schedule_date").toLocalDate());
        apt.setStartTime(rs.getTime("start_time").toLocalTime());
        apt.setEndTime(rs.getTime("end_time").toLocalTime());
        apt.setLocation(rs.getString("location"));
        if (includeStudentName) { apt.setStudentName(rs.getString("student_name")); }
        try { apt.setAdvisorName(rs.getString("advisor_name")); } catch (SQLException ignored) {}
        return apt;
    }
}