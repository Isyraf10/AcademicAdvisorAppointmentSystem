package com.lab.controller;

import com.lab.dao.AppointmentDAO;
import com.lab.dao.ScheduleDAO;
import com.lab.util.DBConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * ListAppointmentsServlet - Display all appointments
 * Accessible to: Student (own), Advisor (assigned), Admin (all)
 */
@WebServlet("/ListAppointmentsServlet")
public class ListAppointmentsServlet extends HttpServlet {
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() throws ServletException {
        appointmentDAO = new AppointmentDAO();
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
        String noMatric = (String) session.getAttribute("noMatric");
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><head><title>My Appointments</title>");
        response.getWriter().println("<style>");
        response.getWriter().println("body { font-family: Arial; margin: 20px; background: #f5f5f5; }");
        response.getWriter().println("table { width: 100%; border-collapse: collapse; background: white; }");
        response.getWriter().println("th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }");
        response.getWriter().println("th { background: #667eea; color: white; }");
        response.getWriter().println("tr:hover { background: #f0f0f0; }");
        response.getWriter().println(".btn { padding: 5px 10px; margin: 5px; border-radius: 4px; cursor: pointer; }");
        response.getWriter().println(".btn-edit { background: #4CAF50; color: white; text-decoration: none; }");
        response.getWriter().println(".btn-cancel { background: #f44336; color: white; text-decoration: none; }");
        response.getWriter().println("</style></head><body>");
        response.getWriter().println("<h2>My Appointments</h2>");
        response.getWriter().println("<a href='dashboard.jsp' class='btn' style='background: #667eea; color: white;'>← Back to Dashboard</a>");
        response.getWriter().println("</body></html>");
    }
}
