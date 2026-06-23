<%@ page contentType="text/html;charset=UTF-8" %>
<%
    HttpSession sess = request.getSession(false);
    if (sess == null || !"student".equalsIgnoreCase((String)sess.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/Login/login.jsp");
        return;
    }
    String userName = (String) sess.getAttribute("name");
    int appointmentId = 0;
    try {
        appointmentId = Integer.parseInt(request.getParameter("id"));
    } catch (Exception e) {
        // Handle error
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Appointment - Academic Advisor System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="appointments" />
</jsp:include>

    <div class="container">
        <div class="card" style="max-width: 700px; margin: 0 auto;">
            <div class="card-header">
                <h3>Edit Appointment #<%= appointmentId %></h3>
            </div>

            <form action="<%= request.getContextPath() %>/UpdateAppointmentServlet" method="POST">
                <input type="hidden" name="appointmentId" value="<%= appointmentId %>">
                <input type="hidden" name="action" value="Edit">

                <div class="form-group">
                    <label for="appointmentType">Appointment Type <span>*</span></label>
                    <select id="appointmentType" name="appointmentType" class="form-select" required>
                        <option value="">-- Select Type --</option>
                        <option value="Academic Consultation">Academic Consultation</option>
                        <option value="Course Registration">Course Registration</option>
                        <option value="Academic Probation">Academic Probation</option>
                        <option value="Career Guidance">Career Guidance</option>
                        <option value="General Inquiry">General Inquiry</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="additionalNotes">Additional Notes / Details</label>
                    <%-- FIX: Tukar attribute id and name field kepada parameter baru --%>
                    <textarea id="additionalNotes" name="additionalNotes" class="form-control" placeholder="Provide any details or specific topics you'd like to discuss..." style="min-height: 120px;"></textarea>
                </div>

                <div class="form-group" style="display: flex; gap: 1rem; margin-top: 2rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Save Changes</button>
                    <a href="<%= request.getContextPath() %>/ViewAppointmentServlet" class="btn btn-ghost" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>

            <p class="text-small text-muted" style="margin-top: 1.5rem; text-align: center;">
                Note: You can only edit pending appointments. Once approved or rejected, the appointment is locked.
            </p>
        </div>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>