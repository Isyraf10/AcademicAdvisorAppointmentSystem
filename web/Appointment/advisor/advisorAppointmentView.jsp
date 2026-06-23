<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, com.aas.model.Appointment" %>
<%
    HttpSession sess = request.getSession(false);
    if (sess == null || !"advisor".equalsIgnoreCase((String)sess.getAttribute("role"))) {
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
    <title>Advisor Dashboard - Appointment Requests</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="appointments" />
</jsp:include>

    <div class="container">
        <%
            String sm = (String) sess.getAttribute("successMessage");
            String em = (String) sess.getAttribute("errorMessage");
            if (sm != null) { %> 
                <div class="alert alert-success"> <%= sm %></div> 
            <% sess.removeAttribute("successMessage"); }
            if (em != null) { %> 
                <div class="alert alert-error"> <%= em %></div> 
            <% sess.removeAttribute("errorMessage"); }
        %>

        <div class="card">
            <div class="card-header">
                <h3 style="margin: 0;">Pending Appointment Requests</h3>
            </div>

            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Appointment ID</th>
                            <th>Student Name</th>
                            <th>Requested Date & Time</th>
                            <th>Venue</th>
                            <th>Type</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Appointment> pendingList = (List<Appointment>) request.getAttribute("pendingAppointments");
                            if (pendingList == null || pendingList.isEmpty()) {
                        %>
                            <tr>
                                <td colspan="7" class="text-center text-muted" style="padding: 3rem;">
                                    <p> All caught up! No pending appointment requests.</p>
                                </td>
                            </tr>
                        <%
                            } else {
                                for (Appointment apt : pendingList) {
                        %>
                            <tr>
                                <td><strong>#<%= apt.getAppointmentId() %></strong></td>
                                <td><strong><%= apt.getStudentName() %></strong></td>
                                <td><%= apt.getAppointmentDate() %><br><small style="color: var(--gray-500);"><%= apt.getStartTime() %></small></td>
                                <td><%= apt.getLocation() %></td>
                                <td><strong><%= apt.getAppointmentType() %></strong></td>
                                <td><span class="status-badge status-pending">Pending</span></td>
                                <td>
                                    <div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
                                        <form action="<%= request.getContextPath() %>/UpdateAppointmentServlet" method="POST" style="display:inline;">
                                            <input type="hidden" name="appointmentId" value="<%= apt.getAppointmentId() %>">
                                            <input type="hidden" name="action" value="Approve">
                                            <button type="submit" class="btn btn-small btn-secondary" style="flex: 1; cursor: pointer;">Approve</button>
                                        </form>
                                        <form action="<%= request.getContextPath() %>/UpdateAppointmentServlet" method="POST" style="display:inline;" onsubmit="return handleReject(this);">
                                            <input type="hidden" name="appointmentId" value="<%= apt.getAppointmentId() %>">
                                            <input type="hidden" name="action" value="Reject">
                                            <input type="hidden" name="rejectionReason" value="">
                                            <button type="submit" class="btn btn-small btn-danger" style="flex: 1; cursor: pointer;">Reject</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        <%      }
                            }
                        %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        function handleReject(formInstance) {
            var reason = prompt("Please provide a reason for rejection:");
            if (reason == null) return false;
            if (reason.trim() === "") { 
                alert("Rejection reason is required!"); 
                return false; 
            }
            formInstance.querySelector('input[name="rejectionReason"]').value = reason;
            return true;
        }
    </script>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




