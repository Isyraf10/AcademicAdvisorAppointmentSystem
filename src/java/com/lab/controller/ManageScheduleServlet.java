package com.lab.controller;

import com.lab.dao.ScheduleDAO;
import com.lab.model.Schedule;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * ManageScheduleServlet - Manage advisor consultation schedules
 * Accessible to: Advisor, Admin
 */
@WebServlet("/ManageScheduleServlet")
public class ManageScheduleServlet extends HttpServlet {
    private ScheduleDAO scheduleDAO;

    @Override
    public void init() throws ServletException {
        scheduleDAO = new ScheduleDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("noMatric") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");
        String action = request.getParameter("action");

        if (action == null) action = "list";

        switch (action) {
            case "list":
                listSchedules(request, response, session);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            default:
                listSchedules(request, response, session);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("noMatric") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createSchedule(request, response, session);
        } else if ("update".equals(action)) {
            updateSchedule(request, response);
        } else if ("delete".equals(action)) {
            deleteSchedule(request, response);
        }
    }

    private void listSchedules(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        
        int advisorId = 2; // Placeholder - get from session/user table mapping
        List<Schedule> schedules = scheduleDAO.getAdvisorSchedules(advisorId);
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><head><title>My Schedule</title>");
        response.getWriter().println(getCss());
        response.getWriter().println("</head><body>");
        response.getWriter().println("<h2>My Consultation Schedule</h2>");
        response.getWriter().println("<a href='ManageScheduleServlet?action=create' class='btn' style='background: #4CAF50; color: white;'>+ Create New Slot</a>");
        response.getWriter().println("<table><tr><th>Date</th><th>Time</th><th>Status</th><th>Bookings</th><th>Actions</th></tr>");
        
        for (Schedule schedule : schedules) {
            response.getWriter().println("<tr><td>" + schedule.getDate() + "</td><td>" + 
                schedule.getStartTime() + " - " + schedule.getEndTime() + "</td><td>" + 
                schedule.getStatus() + "</td><td>" + schedule.getCurrentBookings() + "/" + 
                schedule.getMaxCapacity() + "</td><td>" +
                "<a href='ManageScheduleServlet?action=edit&id=" + schedule.getScheduleId() + "' class='btn btn-edit'>Edit</a> " +
                "<a href='ManageScheduleServlet?action=delete&id=" + schedule.getScheduleId() + "' class='btn btn-delete' onclick='return confirm(\"Delete this slot?\")'>Delete</a>" +
                "</td></tr>");
        }
        
        response.getWriter().println("</table>");
        response.getWriter().println("<a href='dashboard.jsp' class='btn' style='background: #667eea; color: white;'>← Back</a>");
        response.getWriter().println("</body></html>");
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><head><title>Create Schedule</title>");
        response.getWriter().println(getCss());
        response.getWriter().println("</head><body>");
        response.getWriter().println("<h2>Create New Consultation Slot</h2>");
        response.getWriter().println("<form method='POST'>");
        response.getWriter().println("<input type='hidden' name='action' value='create'>");
        response.getWriter().println("<label>Date:</label>");
        response.getWriter().println("<input type='date' name='date' required>");
        response.getWriter().println("<label>Start Time:</label>");
        response.getWriter().println("<input type='time' name='startTime' required>");
        response.getWriter().println("<label>End Time:</label>");
        response.getWriter().println("<input type='time' name='endTime' required>");
        response.getWriter().println("<label>Max Capacity:</label>");
        response.getWriter().println("<input type='number' name='maxCapacity' min='1' value='1' required>");
        response.getWriter().println("<label>Notes:</label>");
        response.getWriter().println("<textarea name='notes' rows='3'></textarea>");
        response.getWriter().println("<button type='submit' class='btn' style='background: #4CAF50; color: white;'>Create Slot</button>");
        response.getWriter().println("</form>");
        response.getWriter().println("</body></html>");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int scheduleId = Integer.parseInt(request.getParameter("id"));
        Schedule schedule = scheduleDAO.getScheduleById(scheduleId);
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><head><title>Edit Schedule</title>");
        response.getWriter().println(getCss());
        response.getWriter().println("</head><body>");
        response.getWriter().println("<h2>Edit Consultation Slot</h2>");
        response.getWriter().println("<form method='POST'>");
        response.getWriter().println("<input type='hidden' name='action' value='update'>");
        response.getWriter().println("<input type='hidden' name='id' value='" + schedule.getScheduleId() + "'>");
        response.getWriter().println("<label>Date:</label>");
        response.getWriter().println("<input type='date' name='date' value='" + schedule.getDate() + "' required>");
        response.getWriter().println("<label>Status:</label>");
        response.getWriter().println("<select name='status'>");
        response.getWriter().println("<option " + ("Available".equals(schedule.getStatus()) ? "selected" : "") + ">Available</option>");
        response.getWriter().println("<option " + ("Booked".equals(schedule.getStatus()) ? "selected" : "") + ">Booked</option>");
        response.getWriter().println("<option " + ("Unavailable".equals(schedule.getStatus()) ? "selected" : "") + ">Unavailable</option>");
        response.getWriter().println("</select>");
        response.getWriter().println("<button type='submit' class='btn' style='background: #4CAF50; color: white;'>Update</button>");
        response.getWriter().println("</form>");
        response.getWriter().println("</body></html>");
    }

    private void createSchedule(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        try {
            LocalDate date = LocalDate.parse(request.getParameter("date"));
            LocalTime startTime = LocalTime.parse(request.getParameter("startTime"));
            LocalTime endTime = LocalTime.parse(request.getParameter("endTime"));
            int maxCapacity = Integer.parseInt(request.getParameter("maxCapacity"));
            String notes = request.getParameter("notes");
            int advisorId = 2; // Placeholder
            
            Schedule schedule = new Schedule(advisorId, date, startTime, endTime, "Available", maxCapacity, notes);
            if (scheduleDAO.createSchedule(schedule)) {
                response.sendRedirect("ManageScheduleServlet?action=list");
            }
        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    private void updateSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Implementation for update
        response.sendRedirect("ManageScheduleServlet?action=list");
    }

    private void deleteSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int scheduleId = Integer.parseInt(request.getParameter("id"));
        scheduleDAO.deleteSchedule(scheduleId);
        response.sendRedirect("ManageScheduleServlet?action=list");
    }

    private String getCss() {
        return "<style>" +
            "body { font-family: Arial; margin: 20px; background: #f5f5f5; }" +
            "table { width: 100%; border-collapse: collapse; background: white; margin-top: 20px; }" +
            "th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }" +
            "th { background: #667eea; color: white; }" +
            "form { background: white; padding: 20px; border-radius: 8px; max-width: 500px; margin-top: 20px; }" +
            "label { display: block; margin-top: 15px; font-weight: bold; }" +
            "input, select, textarea { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; }" +
            ".btn { padding: 8px 15px; margin: 5px; border-radius: 4px; cursor: pointer; text-decoration: none; }" +
            ".btn-edit { background: #FFC107; color: black; }" +
            ".btn-delete { background: #f44336; color: white; }" +
            "</style>";
    }
}
