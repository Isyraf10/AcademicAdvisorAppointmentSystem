<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Master Schedule Database</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script>
        function toggleCheckboxes(source) {
            checkboxes = document.getElementsByName('scheduleIds');
            for(var i=0, n=checkboxes.length;i<n;i++) {
                checkboxes[i].checked = source.checked;
            }
        }
    </script>
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="schedules" />
</jsp:include>

    <div class="container" style="max-width: 1000px;">
        <h2>Master Schedule Database</h2>
        <hr>
        
        <p style="color: green; font-weight: bold;">${sessionScope.successMsg}</p>
        <p style="color: red; font-weight: bold;">${sessionScope.errorMsg}</p>
        <c:remove var="successMsg" scope="session" />
        <c:remove var="errorMsg" scope="session" />

        <form action="ScheduleServlet" method="post">
            <input type="hidden" name="action" value="deleteBulk">
            <input type="hidden" name="isAdmin" value="true"> <table class="table" style="width: 100%; border-collapse: collapse; margin-top: 15px;">
                <thead>
                    <tr style="background: #343a40; color: white;">
                        <th><input type="checkbox" onClick="toggleCheckboxes(this)"></th>
                        <th>Advisor ID</th>
                        <th>Date</th>
                        <th>Time Slot</th>
                        <th>Status</th>
                        <th>Location</th>
                        <th>Force Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${listSchedule}">
                        <tr style="border-bottom: 1px solid #ddd;">
                            <td style="text-align: center;"><input type="checkbox" name="scheduleIds" value="${s.scheduleId}"></td>
                            <td style="font-weight: bold; color: blue;">${s.advisorId}</td>
                            <td>${s.scheduleDate}</td>
                            <td>${s.startTime} - ${s.endTime}</td>
                            <td style="font-weight: bold; color: ${s.status == 'Available' ? 'green' : 'red'};">${s.status}</td>
                            <td>${s.location}</td>
                            <td>
                                <a href="ScheduleServlet?action=delete&id=${s.scheduleId}&isAdmin=true" onclick="return confirm('WARNING: Admin Force Delete. Proceed?');" class="btn btn-danger" style="padding: 5px 10px; font-size: 0.8rem;">Force Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            
            <br>
            <button type="submit" class="btn btn-danger" onclick="return confirm('WARNING: You are about to bulk delete records. Proceed?');">Force Delete Selected</button>
            <a href="ScheduleServlet?action=adminDashboard" class="btn btn-primary">Back to Dashboard</a>
        </form>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




