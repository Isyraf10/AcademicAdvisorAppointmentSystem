<%-- 
    Document   : dashboard
    Created on : 5 Apr 2026
    Author     : isyra
    Purpose    : Dashboard page for authenticated users with role display
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Check if user is logged in
    String noMatric = (String) session.getAttribute("noMatric");
    String role = (String) session.getAttribute("role");
    
    if (noMatric == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    // Set default role if not present (for backward compatibility)
    if (role == null) {
        role = "student";
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard - Academic Advisor</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <div class="navbar">
            <h1>Academic Advisor System</h1>
            <div class="nav-right">
                <div class="user-info">
                    <div class="user-details">
                        <span class="username"><%= noMatric %></span>
                        <span class="user-role"><%= role %></span>
                    </div>
                </div>
                <a href="logout.jsp" class="logout-btn">Logout</a>
            </div>
        </div>

        <div class="container">
            <div class="welcome-card">
                <h2>Welcome to Academic Advisor Appointment System</h2>
                <p>You have successfully logged in to your account. From here, you can manage your appointments, schedule sessions with your academic advisor, and view your academic progress.</p>
                <div class="role-badge"><%= role.toUpperCase() %></div>
                
                <% if ("admin".equals(role)) { %>
                    <p style="margin-top: 10px;"><strong>Admin Access:</strong> You have full system access including all operations.</p>
                <% } else if ("advisor".equals(role)) { %>
                    <p style="margin-top: 10px;"><strong>Advisor Access:</strong> You can view and manage appointments with students.</p>
                <% } else if ("student".equals(role)) { %>
                    <p style="margin-top: 10px;"><strong>Student Access:</strong> You can view your information but cannot make changes.</p>
                <% } %>
            </div>

            <div class="dashboard-grid">
                <div class="dashboard-card">
                    <h3>Manage Schedule</h3>
                    <p>Set your consultation availability and manage your time slots.</p>
                    <% if ("admin".equals(role) || "advisor".equals(role)) { %>
                        <a href="ManageScheduleServlet" class="btn">→ Manage Schedule</a>
                    <% } else { %>
                        <button class="btn" disabled>Available to Advisors</button>
                    <% } %>
                </div>

                <div class="dashboard-card">
                    <h3>My Appointments</h3>
                    <p>View and manage your scheduled appointments and bookings.</p>
                    <a href="ListAppointmentsServlet" class="btn">→ View Appointments</a>
                </div>

                <div class="dashboard-card">
                    <h3>Consultation Records</h3>
                    <p>Access meeting notes and track your academic progress.</p>
                    <a href="ManageRecordServlet" class="btn">→ View Records</a>
                </div>

                <% if ("admin".equals(role)) { %>
                    <div class="dashboard-card">
                        <h3> System Overview</h3>
                        <p>Administrative dashboard with system-wide statistics.</p>
                        <a href="#" class="btn" disabled>Coming Soon</a>
                    </div>

                    <div class="dashboard-card">
                        <h3>Reports</h3>
                        <p>View system statistics and appointment analytics.</p>
                        <a href="#" class="btn" disabled>Coming Soon</a>
                    </div>
                <% } %>
        </div>
    </body>
</html>

