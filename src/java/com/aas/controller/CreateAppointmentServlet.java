package com.aas.controller;

import com.aas.dao.AppointmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/CreateAppointmentServlet")
public class CreateAppointmentServlet extends HttpServlet {
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() { appointmentDAO = new AppointmentDAO(); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null || !"student".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/Login/login.jsp");
            return;
        }

        try {
            int studentId = (Integer) session.getAttribute("userId");
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
            String appointmentType = request.getParameter("appointmentType");
            String additionalNotes = request.getParameter("additionalNotes");

            boolean success = appointmentDAO.createAppointment(studentId, scheduleId, appointmentType, additionalNotes);
            if (success) {
                session.setAttribute("successMessage", "Appointment booked! Awaiting advisor validation.");
            } else {
                session.setAttribute("errorMessage", "Failed to book schedule. It may have been taken.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "System error: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/ViewAppointmentServlet");
    }
}