package com.aas.controller;

import com.aas.dao.RecordDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.aas.model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetAppointmentsServlet", urlPatterns = {"/api/appointments"})
public class GetAppointmentsServlet extends HttpServlet {

    private final RecordDAO recordDAO = new RecordDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Get current logged-in advisor
        HttpSession session = request.getSession(false);
        int advisorId = 9999; // Fallback
        
        if (session != null && session.getAttribute("user") instanceof User) {
            advisorId = ((User) session.getAttribute("user")).getId();
        } else {
            // Mock fallback matching your RecordDashboardController testing setup
            advisorId = 1; 
        }

        // 2. Get requested student ID from the JavaScript fetch call
        String studentIdParam = request.getParameter("studentId");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        if (studentIdParam == null || studentIdParam.isEmpty()) {
            out.write("[]");
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdParam);
            
            // 3. Fetch unlogged appointments
            List<RecordDAO.AppointmentDTO> appts = recordDAO.getUnloggedAppointmentsByStudent(studentId, advisorId);
            
            // 4. Build a simple JSON array manually (avoids needing Gson/Jackson dependencies)
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < appts.size(); i++) {
                RecordDAO.AppointmentDTO a = appts.get(i);
                json.append("{\"id\":").append(a.id).append("}");
                if (i < appts.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            
            out.write(json.toString());
            
        } catch (NumberFormatException e) {
            out.write("[]");
        }
    }
}