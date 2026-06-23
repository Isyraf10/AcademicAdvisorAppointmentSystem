<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Advisor Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="schedules" />
</jsp:include>

    <div class="container">
        <a href="index.jsp" class="btn btn-danger" style="float: right; font-size: 0.9rem; padding: 5px 10px;">Switch Role</a>
        <h2>Advisor Dashboard</h2>
        <hr>
        
        <div style="display: flex; gap: 20px; margin-bottom: 30px;">
            <div style="flex: 1; background: #e2f3e5; padding: 20px; border-radius: 8px; text-align: center; border: 1px solid #28a745;">
                <h3 style="color: #28a745; margin: 0;">${totalAvailable}</h3>
                <p style="margin: 5px 0 0 0;">Available Slots</p>
            </div>
            <div style="flex: 1; background: #fdeaea; padding: 20px; border-radius: 8px; text-align: center; border: 1px solid #dc3545;">
                <h3 style="color: #dc3545; margin: 0;">${totalBusy}</h3>
                <p style="margin: 5px 0 0 0;">Booked / Busy Slots</p>
            </div>
        </div>

        <h3>Quick Actions</h3>
        <a href="ScheduleServlet?action=new" class="btn btn-primary">Add New Schedule</a>
        <a href="ScheduleServlet?action=list" class="btn btn-warning">Manage My Schedules</a>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




