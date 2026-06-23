<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, com.aas.model.Appointment" %>
<%
    HttpSession sess = request.getSession(false);
    if (sess == null || !"admin".equalsIgnoreCase((String)sess.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/Login/login.jsp");
        return;
    }
    String userName = (String) sess.getAttribute("name");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Control Panel - All Appointments</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="appointments" />
</jsp:include>

    <div class="container">
        <div class="card">
            <div class="card-header">
                <h3 style="margin: 0;">System Appointment Records</h3>
                <span class="badge badge-primary">Master Control</span>
            </div>

            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Appointment ID</th>
                            <th>Student ID</th>
                            <th>Advisor ID</th>
                            <th>Type</th>
                            <th>Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Appointment> allList = (List<Appointment>) request.getAttribute("allAppointments");
                            if (allList == null || allList.isEmpty()) {
                        %>
                            <tr>
                                <td colspan="7" class="text-center text-muted" style="padding: 3rem;">
                                    <p>No appointment records in the system.</p>
                                </td>
                            </tr>
                        <%
                            } else {
                                for (Appointment apt : allList) {
                                    String statusClass = "status-pending";
                                    if("Approved".equalsIgnoreCase(apt.getStatus())) statusClass = "status-approved";
                                    else if("Rejected".equalsIgnoreCase(apt.getStatus())) statusClass = "status-rejected";
                        %>
                            <tr>
                                <td><strong>#<%= apt.getAppointmentId() %></strong></td>
                                <td><code style="background: var(--gray-100); padding: 0.25rem 0.5rem; border-radius: 4px;"><%= apt.getStudentName() != null ? apt.getStudentName() : ("User #" + apt.getStudentId()) %></code></td>
                                <td><code style="background: var(--gray-100); padding: 0.25rem 0.5rem; border-radius: 4px;"><%= apt.getAdvisorName() != null ? apt.getAdvisorName() : ("User #" + apt.getAdvisorId()) %></code></td>
                                <td><%= apt.getAppointmentType() %></td>
                                <td><%= apt.getAppointmentDate() %></td>
                                <td><span class="status-badge <%= statusClass %>"><%= apt.getStatus() %></span></td>
                                <td>
                                    <form action="<%= request.getContextPath() %>/DeleteAppointmentServlet" method="POST" onsubmit="return confirm('Are you sure? This will permanently delete the record.');" style="display: inline;">
                                        <input type="hidden" name="appointmentId" value="<%= apt.getAppointmentId() %>">
                                        <button type="submit" class="btn btn-small btn-danger" style="cursor: pointer;">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        <%
                            }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




