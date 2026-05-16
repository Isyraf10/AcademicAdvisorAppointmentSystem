package com.lab.controller;

import com.lab.dao.AppointmentDAO;
import com.lab.model.Appointment;
import com.lab.model.UserRole;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

@WebServlet("/ViewAppointmentServlet")
public class ViewAppointmentServlet extends HttpServlet {
    private AppointmentDAO appointmentDAO;

    public void init() {
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("noMatric") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Get user role for permission checks
        String role = (String) session.getAttribute("role");
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            List<Appointment> listAppointments = appointmentDAO.selectAllAppointments();
            
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Appointment Types</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("table { border-collapse: collapse; width: 100%; }");
            out.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
            out.println("th { background-color: #667eea; color: white; }");
            out.println("tr:nth-child(even) { background-color: #f9f9f9; }");
            out.println("a { color: #667eea; text-decoration: none; margin-right: 10px; }");
            out.println("a:hover { text-decoration: underline; }");
            out.println(".read-only { color: #999; }");
            out.println("</style></head><body>");
            
            out.println("<h2>Appointment Types</h2>");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Title</th><th>Type</th><th>Duration (min)</th><th>Available Slots</th><th>Actions</th></tr>");
            
            for (Appointment apt : listAppointments) {
                out.println("<tr>");
                out.println("<td>" + apt.getAppointmentId() + "</td>");
                out.println("<td>" + apt.getTitle() + "</td>");
                out.println("<td>" + apt.getType() + "</td>");
                out.println("<td>" + apt.getDuration() + "</td>");
                out.println("<td>" + apt.getAvailableSlots() + "</td>");
                out.println("<td>");
                
                // Show actions based on role
                if (UserRole.canWrite(role)) {
                    out.println("<a href='UpdateAppointmentServlet?id=" + apt.getAppointmentId() + "'>Edit</a>");
                }
                if (UserRole.canDelete(role)) {
                    out.println("<a href='DeleteAppointmentServlet?id=" + apt.getAppointmentId() + "' onclick=\"return confirm('Delete this appointment type?')\">Delete</a>");
                } else if (!UserRole.canWrite(role)) {
                    out.println("<span class='read-only'>View only</span>");
                }
                out.println("</td>");
                out.println("</tr>");
            }
            
            out.println("</table>");
            if (UserRole.canWrite(role)) {
                out.println("<br><a href='CreateAppointmentServlet'><button>Add New Appointment Type</button></a>");
            }
            out.println("</body></html>");
        } finally {
            out.close();
        }
    }
}
