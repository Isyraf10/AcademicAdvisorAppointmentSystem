<%-- 
    Document   : logout
    Created on : 5 Apr 2026
    Author     : isyra
    Purpose    : Logout functionality for Academic Advisor System
--%>

<%
    // Invalidate the session
    session.invalidate();
    
    // Redirect to login page
    response.sendRedirect("login.jsp");
%>
