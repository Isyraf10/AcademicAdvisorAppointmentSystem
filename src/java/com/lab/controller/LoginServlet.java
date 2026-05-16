package com.lab.controller;

/*
 * LoginServlet - Handles user authentication for Academic Advisor Appointment System
 * Now with database-backed authentication and role support
 */

import com.lab.dao.UserDAO;
import com.lab.model.User;
import com.lab.model.UserRole;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * LoginServlet - Processes user login authentication
 * Authenticates users against database with role-based access
 *
 * @author isyra
 */
@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    /**
     * Handle GET requests - redirect to login page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    /**
     * Handle POST requests - process login
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String noMatric = request.getParameter("noMatric");
        String password = request.getParameter("password");
        
        // Validate input
        if (noMatric == null || noMatric.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            response.sendRedirect("login.jsp?error=empty");
            return;
        }
        
        // Authenticate user
        User user = userDAO.authenticateUser(noMatric, password);
        
        if (user != null && UserRole.isValidRole(user.getRoles())) {
            // Create session
            HttpSession session = request.getSession(true);
            session.setAttribute("noMatric", noMatric);
            session.setAttribute("role", user.getRoles());
            session.setAttribute("loginTime", System.currentTimeMillis());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            
            // Redirect to dashboard
            response.sendRedirect("dashboard.jsp");
        } else {
            // Login failed
            response.sendRedirect("login.jsp?error=invalid");
        }
    }
}
