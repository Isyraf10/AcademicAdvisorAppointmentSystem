package com.aas.controller;

import com.aas.dao.UserDAO;
import com.aas.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        User user = userDAO.authenticateUser(email, password);
        
        if (user != null) {
            // Instantiate User Session State Machine Context
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("name", user.getName());
            
            // Normalize role string to absolute lowercase to prevent comparison breaks
            String normalizedRole = user.getRole().toLowerCase().trim();
            session.setAttribute("role", normalizedRole);
            
            // Backward compatibility tracking attribute for legacy modules
            session.setAttribute("noMatric", user.getEmail()); 
            
            // Forward directly to the data loading hub controller
            response.sendRedirect(request.getContextPath() + "/ViewAppointmentServlet");
        } else {
            // Authentication Failure Handling Strategy
            request.setAttribute("errorMessage", "Invalid email or password. Please try again.");
            request.getRequestDispatcher("/Login/login.jsp").forward(request, response);
        }
    }
}
