package com.aas.controller;

import com.aas.dao.AppointmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
            int advisorId = Integer.parseInt(request.getParameter("advisorId"));
            int scheduleId = Integer.parseInt(request.getParameter("scheduleId"));
            String appointmentType = request.getParameter("appointmentType");
            String description = request.getParameter("description");

            boolean success = appointmentDAO.createAppointment(studentId, advisorId, scheduleId, appointmentType, description);
            if (success) {
                session.setAttribute("successMessage", "Appointment booked! Awaiting advisor validation.");
            } else {
                session.setAttribute("errorMessage", "Failed to lock time slot. It may have been taken.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "System error: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/ViewAppointmentServlet");
    }
}