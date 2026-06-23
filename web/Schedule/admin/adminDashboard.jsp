<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="schedules" />
</jsp:include>

    <div class="container">
        <a href="index.jsp" class="btn btn-danger" style="float: right; font-size: 0.9rem; padding: 5px 10px; background: #dc3545;">Switch Role</a>
        <h2>Master Dashboard (Faculty Overview)</h2>
        <hr>
        
        <div style="display: flex; gap: 20px; margin-bottom: 30px;">
            <div style="flex: 1; background: #e2f3e5; padding: 20px; border-radius: 8px; text-align: center; border: 1px solid #28a745;">
                <h3 style="color: #28a745; margin: 0;">${totalAvailable}</h3>
                <p style="margin: 5px 0 0 0;">Total Available (All Advisors)</p>
            </div>
            <div style="flex: 1; background: #fdeaea; padding: 20px; border-radius: 8px; text-align: center; border: 1px solid #dc3545;">
                <h3 style="color: #dc3545; margin: 0;">${totalBusy}</h3>
                <p style="margin: 5px 0 0 0;">Total Booked (All Advisors)</p>
            </div>
        </div>

        <h3>Administrative Actions</h3>
        <a href="ScheduleServlet?action=adminNew" class="btn" style="background: #343a40; color: white;">Assign Schedule to Advisor</a>
        <a href="ScheduleServlet?action=adminList" class="btn btn-warning">Master Schedule Database</a>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




