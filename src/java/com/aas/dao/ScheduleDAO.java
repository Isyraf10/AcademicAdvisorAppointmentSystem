package com.aas.dao;

import com.aas.model.Schedule;
import com.aas.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ScheduleDAO {

    public void addSchedule(Schedule schedule) {
        String query = "INSERT INTO schedule (advisor_id, schedule_date, start_time, end_time, status, location) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, schedule.getAdvisorId());
            ps.setDate(2, schedule.getScheduleDate());
            ps.setTime(3, schedule.getStartTime());
            ps.setTime(4, schedule.getEndTime());
            ps.setString(5, schedule.getStatus());
            ps.setString(6, schedule.getLocation());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Schedule> getAllSchedulesByAdvisor(int advisorId) {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM schedule WHERE advisor_id = ? ORDER BY schedule_date ASC, start_time ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, advisorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule(
                    rs.getInt("schedule_id"), rs.getInt("advisor_id"), rs.getDate("schedule_date"),
                    rs.getTime("start_time"), rs.getTime("end_time"), rs.getString("status"), rs.getString("location")
                );
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    public Schedule getScheduleById(int scheduleId) {
        Schedule schedule = null;
        String query = "SELECT * FROM schedule WHERE schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, scheduleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                schedule = new Schedule(
                    rs.getInt("schedule_id"), rs.getInt("advisor_id"), rs.getDate("schedule_date"),
                    rs.getTime("start_time"), rs.getTime("end_time"), rs.getString("status"), rs.getString("location")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    public void updateSchedule(Schedule schedule) {
        String query = "UPDATE schedule SET schedule_date=?, start_time=?, end_time=?, status=?, location=? WHERE schedule_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, schedule.getScheduleDate());
            ps.setTime(2, schedule.getStartTime());
            ps.setTime(3, schedule.getEndTime());
            ps.setString(4, schedule.getStatus());
            ps.setString(5, schedule.getLocation());
            ps.setInt(6, schedule.getScheduleId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSchedule(int scheduleId) {
        String query = "DELETE FROM schedule WHERE schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, scheduleId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isOverlap(int advisorId, Date date, Time start, Time end, int excludeScheduleId) {
        boolean overlap = false;
        String query = "SELECT COUNT(*) FROM schedule WHERE advisor_id = ? AND schedule_date = ? " +
                       "AND schedule_id != ? AND (start_time < ? AND end_time > ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, advisorId);
            ps.setDate(2, date);
            ps.setInt(3, excludeScheduleId);
            ps.setTime(4, end);
            ps.setTime(5, start);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) overlap = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return overlap;
    }

    public int countSchedulesByStatus(int advisorId, String status) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM schedule WHERE advisor_id = ? AND status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, advisorId);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void deleteBulkSchedules(String[] scheduleIds) {
        if (scheduleIds == null || scheduleIds.length == 0) return;
        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < scheduleIds.length; i++) {
            placeholders.append("?");
            if (i < scheduleIds.length - 1) placeholders.append(",");
        }
        String query = "DELETE FROM schedule WHERE schedule_id IN (" + placeholders + ")";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            for (int i = 0; i < scheduleIds.length; i++) {
                ps.setInt(i + 1, Integer.parseInt(scheduleIds[i]));
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // ADMIN FUNCTIONS
    // ==========================================
    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        // Fetch ALL schedules without filtering by advisor_id
        String query = "SELECT * FROM schedule ORDER BY schedule_date ASC, start_time ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Schedule schedule = new Schedule(
                    rs.getInt("schedule_id"), rs.getInt("advisor_id"), rs.getDate("schedule_date"),
                    rs.getTime("start_time"), rs.getTime("end_time"), rs.getString("status"), rs.getString("location")
                );
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    public int countAllSchedulesByStatus(String status) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM schedule WHERE status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps =conn.prepareStatement(query)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) count = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // ==========================================
    // AJAX LIVE CHECK FUNCTION
    // ==========================================
    public List<String> getBookedTimeSlots(int advisorId, Date date) {
        List<String> bookedSlots = new ArrayList<>();
        String query = "SELECT start_time, end_time FROM schedule WHERE advisor_id = ? AND schedule_date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, advisorId);
            ps.setDate(2, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String start = rs.getTime("start_time").toString();
                String end = rs.getTime("end_time").toString();
                // Combine time to match HTML dropdown values (e.g., 08:00:00-10:00:00)
                bookedSlots.add(start + "-" + end);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSlots;
    }
}