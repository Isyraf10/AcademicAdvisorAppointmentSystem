package com.aas.controller;

import com.aas.dao.AppointmentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UpdateAppointmentServlet")
public class UpdateAppointmentServlet extends HttpServlet {
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
        String action = request.getParameter("action");
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        boolean success = false;

        try {
            if ("advisor".equals(role)) {
                if ("Approve".equals(action)) {
                    success = appointmentDAO.updateAppointmentStatus(appointmentId, "Approved");
                } else if ("Reject".equals(action)) {
                    String reason = request.getParameter("rejectionReason");
                    success = appointmentDAO.rejectAppointmentWithReason(appointmentId, reason);
                }
            } else if ("student".equals(role) && "Edit".equals(action)) {
                String description = request.getParameter("description");
                success = appointmentDAO.editAppointmentDetails(appointmentId, description);
            }

            if (success) {
                session.setAttribute("successMessage", "Record synchronized successfully.");
            } else {
                session.setAttribute("errorMessage", "Failed to update record details.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/ViewAppointmentServlet");
    }
}