package com.aas.controller;

import com.aas.dao.AppointmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/DeleteAppointmentServlet")
public class DeleteAppointmentServlet extends HttpServlet {
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() { appointmentDAO = new AppointmentDAO(); }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/Login/login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        boolean success = false;

        if ("admin".equals(role)) {
            success = appointmentDAO.hardDeleteAppointment(appointmentId);
        } else if ("student".equals(role)) {
            success = appointmentDAO.updateAppointmentStatus(appointmentId, "Cancelled");
        }

        if (success) {
            session.setAttribute("successMessage", "Appointment altered/removed successfully.");
        } else {
            session.setAttribute("errorMessage", "Action processing failed.");
        }
        response.sendRedirect(request.getContextPath() + "/ViewAppointmentServlet");
    }
}