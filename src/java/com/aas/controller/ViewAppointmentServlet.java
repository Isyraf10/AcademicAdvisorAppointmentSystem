package com.aas.controller;

import com.aas.dao.AppointmentDAO;
import com.aas.model.Appointment;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet("/ViewAppointmentServlet")
public class ViewAppointmentServlet extends HttpServlet {
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() { appointmentDAO = new AppointmentDAO(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/Login/login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if ("student".equalsIgnoreCase(role)) {
            List<Appointment> studentApts = appointmentDAO.getStudentAppointments(userId);
            request.setAttribute("appointments", studentApts);
            request.getRequestDispatcher("/Appointment/student/studentAppointmentView.jsp").forward(request, response);
        } else if ("advisor".equalsIgnoreCase(role)) {
            List<Appointment> pendingApts = appointmentDAO.getPendingAppointmentsForAdvisor(userId);
            request.setAttribute("pendingAppointments", pendingApts);
            request.getRequestDispatcher("/Appointment/advisor/advisorAppointmentView.jsp").forward(request, response);
        } else if ("admin".equalsIgnoreCase(role)) {
            List<Appointment> allApts = appointmentDAO.getAllAppointments();
            request.setAttribute("allAppointments", allApts);
            request.getRequestDispatcher("/Appointment/admin/adminAppointmentView.jsp").forward(request, response);
        } else {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/Login/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}