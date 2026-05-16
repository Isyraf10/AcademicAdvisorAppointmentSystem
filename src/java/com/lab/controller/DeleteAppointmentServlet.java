package com.lab.controller;

import com.lab.dao.AppointmentDAO;
import com.lab.model.UserRole;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

@WebServlet("/DeleteAppointmentServlet")
public class DeleteAppointmentServlet extends HttpServlet {
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
        
        // Check role permission (only admin can delete)
        String role = (String) session.getAttribute("role");
        if (!UserRole.canDelete(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only administrators can delete appointment types");
            return;
        }
        
        int id = Integer.parseInt(request.getParameter("id"));
        appointmentDAO.deleteAppointment(id);
        
        response.sendRedirect("ViewAppointmentServlet");
    }
}
