<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="jakarta.servlet.http.HttpSession" %>
<%
    // 1. Retrieve the existing active session safely
    HttpSession sharedSession = request.getSession(false);
    boolean signedIn = (sharedSession != null && sharedSession.getAttribute("userId") != null);
    
    String sharedName = "";
    String sharedRole = "";
    String displayRole = "";
    
    if (signedIn) {
        sharedName = (String) sharedSession.getAttribute("name");
        sharedRole = (String) sharedSession.getAttribute("role");
        
        // Format the role to Title Case (e.g., "student" -> "Student")
        if (sharedRole != null && !sharedRole.isEmpty()) {
            displayRole = sharedRole.substring(0, 1).toUpperCase() + sharedRole.substring(1).toLowerCase();
        }
    }
    
    String context = request.getContextPath();
    String activePage = request.getParameter("activePage");
    if (activePage == null) activePage = "";
%>
<header class="site-header">
    <div class="site-shell site-header__inner">
        
        <a href="<%= context %>/ViewAppointmentServlet" class="brand">
            Academic Advisor System
        </a>
        
        <nav class="site-nav">
            <% if (signedIn) { %>
                
                <span class="user-pill" style="background: var(--primary-soft); color: var(--primary-dark); padding: 4px 12px; border-radius: 12px; font-size: 12px; font-weight: 600; margin-right: 8px; border: 1px solid var(--line);">
                    Hi <strong><%= sharedName %></strong>! [<%= displayRole %>]
                </span>

                <a href="<%= context %>/ViewAppointmentServlet" class="<%= "appointments".equals(activePage) ? "active" : "" %>">Appointments</a>
                
                <%-- Role Isolation: Keeps students out of advisor configuration panels --%>
                <% if (!"student".equalsIgnoreCase(sharedRole)) { %>
                    <a href="<%= context %>/ScheduleServlet" class="<%= "schedules".equals(activePage) ? "active" : "" %>">Schedules</a>
                <% } %>
                
                <a href="<%= context %>/records/dashboard" class="<%= "records".equals(activePage) ? "active" : "" %>">Records</a>
                
                <a href="<%= context %>/LogoutServlet" style="color: var(--danger); margin-left: 10px; font-weight: 700;">Logout</a>
            
            <% } else { %>
                <a href="<%= context %>/Login/login.jsp" class="<%= "login".equals(activePage) ? "active" : "" %>">Login</a>
                <a href="<%= context %>/Login/register.jsp" class="<%= "register".equals(activePage) ? "active" : "" %>">Register</a>
            <% } %>
        </nav>
    </div>
</header>