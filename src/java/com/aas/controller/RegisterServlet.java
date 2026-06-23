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
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Tangkap parameter dari borang pendaftaran
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phoneNumber = request.getParameter("phoneNumber");
        String role = request.getParameter("role");
        
        // 2. Masukkan ke dalam objek Model
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password); // Nota: Dalam real-world project, password kena di-hash (Bcrypt)
        newUser.setPhoneNumber(phoneNumber);
        newUser.setRole(role);
        
        // 3. Simpan ke database melalui DAO
        boolean success = userDAO.registerUser(newUser);
        
        // 4. Halakan (Routing) berdasarkan hasil pendaftaran
        if (success) {
            request.setAttribute("successMessage", "Registration successful! Please login with your new account.");
            request.getRequestDispatcher("/Login/login.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Registration failed. Email might already exist or there is a database error.");
            request.getRequestDispatcher("/Login/register.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Halakan pengguna yang sesat menggunakan GET ke borang pendaftaran semula
        response.sendRedirect(request.getContextPath() + "/Login/register.jsp");
    }
}