<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Academic Advisor System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="schedules" />
</jsp:include>

    <div style="background: white; padding: 40px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1); text-align: center; width: 350px;">
        <h2>System Login</h2>
        <hr>
        <p style="color: #666; font-size: 0.9rem; margin-bottom: 20px;">Please select your role for system testing:</p>
        
        <a href="ScheduleServlet?action=setRole&role=Advisor" class="btn btn-primary" style="display: block; margin-bottom: 15px; padding: 15px; font-size: 1.1rem; text-decoration: none;">Login as Advisor</a>
        
        <a href="ScheduleServlet?action=setRole&role=Admin" class="btn" style="display: block; background: #343a40; color: white; padding: 15px; font-size: 1.1rem; text-decoration: none;">Login as Admin</a>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




