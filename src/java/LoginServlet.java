/*
 * LoginServlet - Handles user authentication for Academic Advisor Appointment System
 * Uses hash table-based user credentials with secure password hashing
 */

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * LoginServlet - Processes user login authentication
 * Validates credentials against a hash table of user accounts
 *
 * @author isyra
 */
@WebServlet(urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private static Hashtable<String, String> userDatabase;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize user database with sample credentials (password hashes)
        userDatabase = new Hashtable<>();
        
        // Sample users - in production, these would come from a database
        // Username: admin, Password: admin123
        userDatabase.put("admin", hashPassword("admin123"));
        // Username: advisor, Password: advisor123
        userDatabase.put("advisor", hashPassword("advisor123"));
        // Username: student, Password: student123
        userDatabase.put("student", hashPassword("student123"));
    }

    /**
     * Hash password using SHA-256 and Base64 encoding
     *
     * @param password the plain text password
     * @return hashed password as Base64 string
     */
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verify password against stored hash
     *
     * @param password the plain text password
     * @param hash the stored hash
     * @return true if password matches hash
     */
    private static boolean verifyPassword(String password, String hash) {
        String hashedPassword = hashPassword(password);
        return hashedPassword != null && hashedPassword.equals(hash);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward GET requests to login page
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String errorMessage = "";
        
        // Input validation
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            errorMessage = "Username and password are required.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        username = username.trim();
        
        // Check if user exists and password is correct
        if (userDatabase.containsKey(username) && 
            verifyPassword(password, userDatabase.get(username))) {
            
            // Create session and store user information
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("loginTime", System.currentTimeMillis());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes timeout
            
            // Redirect to dashboard or home page
            response.sendRedirect("dashboard.jsp");
            
        } else {
            // Authentication failed
            errorMessage = "Invalid username or password. Please try again.";
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    /**
     * Returns description of the servlet
     *
     * @return servlet description
     */
    @Override
    public String getServletInfo() {
        return "Academic Advisor Appointment System - Login Servlet";
    }
}
