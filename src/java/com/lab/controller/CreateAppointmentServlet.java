package com.lab.controller;

import com.lab.dao.AppointmentDAO;
import com.lab.model.Appointment;
import com.lab.model.UserRole;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

@WebServlet("/CreateAppointmentServlet")
public class CreateAppointmentServlet extends HttpServlet {
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
        
        // Check role permission (only advisor and admin can create)
        String role = (String) session.getAttribute("role");
        if (!UserRole.canWrite(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to create appointments");
            return;
        }
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Create Appointment Type</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }");
        out.println("form { background: white; padding: 30px; border-radius: 8px; max-width: 500px; }");
        out.println("label { display: block; margin-top: 15px; font-weight: bold; }");
        out.println("input, textarea { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; }");
        out.println("button { margin-top: 20px; padding: 10px 30px; background: #667eea; color: white; border: none; border-radius: 4px; cursor: pointer; }");
        out.println("button:hover { background: #764ba2; }");
        out.println("</style></head><body>");
        
        out.println("<h2>Create New Appointment Type</h2>");
        out.println("<form method='POST'>");
        out.println("<label>Title:</label>");
        out.println("<input type='text' name='title' required>");
        
        out.println("<label>Type:</label>");
        out.println("<select name='type' required>");
        out.println("<option value=''>-- Select Type --</option>");
        out.println("<option value='Academic Planning'>Academic Planning</option>");
        out.println("<option value='Course Selection'>Course Selection</option>");
        out.println("<option value='Mentoring'>Mentoring</option>");
        out.println("<option value='Career Guidance'>Career Guidance</option>");
        out.println("<option value='Other'>Other</option>");
        out.println("</select>");
        
        out.println("<label>Duration (minutes):</label>");
        out.println("<input type='number' name='duration' min='15' max='480' required>");
        
        out.println("<label>Available Slots:</label>");
        out.println("<input type='number' name='availableSlots' min='1' required>");
        
        out.println("<button type='submit'>Create Appointment</button>");
        out.println("</form>");
        out.println("</body></html>");
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("noMatric") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Check role permission (only advisor and admin can create)
        String role = (String) session.getAttribute("role");
        if (!UserRole.canWrite(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to create appointments");
            return;
        }
        
        String title = request.getParameter("title");
        String type = request.getParameter("type");
        int duration = Integer.parseInt(request.getParameter("duration"));
        int availableSlots = Integer.parseInt(request.getParameter("availableSlots"));

        Appointment newAppointment = new Appointment(title, type, duration, availableSlots);
        appointmentDAO.insertAppointment(newAppointment);
        
        response.sendRedirect("ViewAppointmentServlet");
    }
}
