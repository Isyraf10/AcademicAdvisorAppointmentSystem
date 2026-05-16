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

@WebServlet("/UpdateAppointmentServlet")
public class UpdateAppointmentServlet extends HttpServlet {
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
        
        // Check role permission
        String role = (String) session.getAttribute("role");
        if (!UserRole.canWrite(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to edit appointments");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        int id = Integer.parseInt(request.getParameter("id"));

        Appointment existingAppointment = appointmentDAO.selectAppointment(id);

        if (existingAppointment != null) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Update Appointment Type</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }");
            out.println("form { background: white; padding: 30px; border-radius: 8px; max-width: 500px; }");
            out.println("label { display: block; margin-top: 15px; font-weight: bold; }");
            out.println("input, select { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; }");
            out.println("button { margin-top: 20px; padding: 10px 30px; background: #667eea; color: white; border: none; border-radius: 4px; cursor: pointer; }");
            out.println("button:hover { background: #764ba2; }");
            out.println("</style></head><body>");
            
            out.println("<h2>Update Appointment Type</h2>");
            out.println("<form action='UpdateAppointmentServlet' method='POST'>");
            out.println("<input type='hidden' name='id' value='" + existingAppointment.getAppointmentId() + "'>");
            
            out.println("<label>Title:</label>");
            out.println("<input type='text' name='title' value='" + existingAppointment.getTitle() + "' required>");
            
            out.println("<label>Type:</label>");
            out.println("<select name='type' required>");
            out.println("<option value='Academic Planning' " + ("Academic Planning".equals(existingAppointment.getType()) ? "selected" : "") + ">Academic Planning</option>");
            out.println("<option value='Course Selection' " + ("Course Selection".equals(existingAppointment.getType()) ? "selected" : "") + ">Course Selection</option>");
            out.println("<option value='Mentoring' " + ("Mentoring".equals(existingAppointment.getType()) ? "selected" : "") + ">Mentoring</option>");
            out.println("<option value='Career Guidance' " + ("Career Guidance".equals(existingAppointment.getType()) ? "selected" : "") + ">Career Guidance</option>");
            out.println("<option value='Other' " + ("Other".equals(existingAppointment.getType()) ? "selected" : "") + ">Other</option>");
            out.println("</select>");
            
            out.println("<label>Duration (minutes):</label>");
            out.println("<input type='number' name='duration' min='15' max='480' value='" + existingAppointment.getDuration() + "' required>");
            
            out.println("<label>Available Slots:</label>");
            out.println("<input type='number' name='availableSlots' min='1' value='" + existingAppointment.getAvailableSlots() + "' required>");

            out.println("<button type='submit'>Update Appointment</button>");
            out.println("</form>");
            out.println("</body></html>");
        } else {
            out.println("<p>Appointment not found.</p>");
        }
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
        
        // Check role permission
        String role = (String) session.getAttribute("role");
        if (!UserRole.canWrite(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to edit appointments");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String type = request.getParameter("type");
        int duration = Integer.parseInt(request.getParameter("duration"));
        int availableSlots = Integer.parseInt(request.getParameter("availableSlots"));

        Appointment appointment = new Appointment(id, title, type, duration, availableSlots);
        appointmentDAO.updateAppointment(appointment);

        response.sendRedirect("ViewAppointmentServlet");
    }
}
