<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, com.aas.model.Appointment" %>
<%
    HttpSession sess = request.getSession(false);
    if (sess == null || !"student".equalsIgnoreCase((String)sess.getAttribute("role"))) {
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
    <title>My Appointments - Student Portal</title>
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

        <div class="card" style="margin-bottom: 2rem;">
            <div class="card-header">
                <h3 style="margin: 0;">My Appointment Bookings</h3>
                <a href="<%= request.getContextPath() %>/Appointment/bookAppointment.jsp" class="btn btn-primary btn-small">+ New Appointment</a>
            </div>

            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Appointment ID</th>
                            <th>Date & Time</th>
                            <th>Advisor</th>
                            <th>Venue</th>
                            <th>Type</th>
                            <th>Additional Notes</th> <%-- Kekalkan Th head kau --%>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Appointment> list = (List<Appointment>) request.getAttribute("appointments");
                            if (list == null || list.isEmpty()) {
                        %>
                            <tr>
                                <td colspan="8" class="text-center text-muted" style="padding: 3rem;">
                                    <p>No appointments yet. <a href="<%= request.getContextPath() %>/Appointment/bookAppointment.jsp">Book your first appointment now!</a></p>
                                </td>
                            </tr>
                        <%
                            } else {
                                for (Appointment apt : list) {
                                    String statusClass = "status-pending";
                                    if("Approved".equalsIgnoreCase(apt.getStatus())) statusClass = "status-approved";
                                    else if("Rejected".equalsIgnoreCase(apt.getStatus())) statusClass = "status-rejected";
                        %>
                            <tr>
                                <td><strong>#<%= apt.getAppointmentId() %></strong></td>
                                <td><%= apt.getAppointmentDate() %><br><small style="color: var(--gray-500);"><%= apt.getStartTime() %> - <%= apt.getEndTime() %></small></td>
                                <td><%= apt.getAdvisorName() != null ? apt.getAdvisorName() : apt.getAdvisorId() %></td>
                                <td><%= apt.getLocation() %></td>
                                <td><strong><%= apt.getAppointmentType() %></strong></td>
                                <td><%= apt.getAdditionalNotes() %></td> <%-- FIX: Tukar function panggil data huraian baru --%>
                                <td>
                                    <span class="status-badge <%= statusClass %>"><%= apt.getStatus() %></span>
                                    <%-- FIX: Tambah suntikan sebab reject di bawah status badge --%>
                                    <% if ("Rejected".equalsIgnoreCase(apt.getStatus()) && apt.getRejectionReason() != null) { %>
                                        <div style="font-size: 11px; color: var(--danger); margin-top: 4px; font-style: italic;">
                                            Reason: <%= apt.getRejectionReason() %>
                                        </div>
                                    <% } %>
                                </td>
                                <td>
                                    <% if ("Pending".equalsIgnoreCase(apt.getStatus())) { %>
                                        <a href="<%= request.getContextPath() %>/Appointment/editAppointment.jsp?id=<%= apt.getAppointmentId() %>" class="btn btn-small btn-ghost" style="color: var(--primary);">Edit</a>
                                        <form action="<%= request.getContextPath() %>/DeleteAppointmentServlet" method="POST" style="display:inline;" onsubmit="return confirm('Are you sure you want to cancel this appointment?');">
                                            <input type="hidden" name="appointmentId" value="<%= apt.getAppointmentId() %>">
                                            <button type="submit" class="btn btn-small btn-danger">Cancel</button>
                                        </form>
                                    <% } else { %>
                                        <span class="text-muted text-small">Locked</span>
                                    <% } %>
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
<jsp:include page="/includes/footer.jsp" />
</body>
</html>