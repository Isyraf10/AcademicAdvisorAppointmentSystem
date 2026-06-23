<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List, java.time.format.DateTimeFormatter, com.aas.dao.AppointmentDAO, com.aas.model.TimeSlot" %>
<%
    HttpSession sess = request.getSession(false);
    if (sess == null || !"student".equalsIgnoreCase((String)sess.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/Login/login.jsp");
        return;
    }
    String userName = (String) sess.getAttribute("name");
    int studentId = (Integer) sess.getAttribute("userId"); // Ambil id dari session
    
    // FIX: Tukar kaedah muat data dari general slot ke slot khusus MENTOR sahaja
    List<TimeSlot> availableSlots = new AppointmentDAO().getAvailableSlotsByStudentMentor(studentId);
    DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
    DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book an Appointment - Academic Advisor System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="appointments" />
</jsp:include>

    <div class="container">
        <div class="card" style="max-width: 700px; margin: 0 auto;">
            <div class="card-header">
                <h3>Book an Appointment</h3>
            </div>

            <%
                String errorMsg = (String) request.getAttribute("errorMessage");
                if (errorMsg != null) {
            %>
                <div class="alert alert-error"><%= errorMsg %></div>
            <%  } %>

            <form action="<%= request.getContextPath() %>/CreateAppointmentServlet" method="POST">
                <div class="form-group">
                    <label for="scheduleId">Preferred Schedule <span>*</span></label>
                    <select id="scheduleId" name="scheduleId" class="form-select" required>
                        <option value="">-- Choose Date, Time Slot and Venue --</option>
                        <%
                            for (TimeSlot slot : availableSlots) {
                        %>
                            <option value="<%= slot.getScheduleId() %>">
                                <%= slot.getAdvisorName() %> - <%= slot.getDate().format(dateFmt) %>,
                                <%= slot.getStartTime().format(timeFmt) %> - <%= slot.getEndTime().format(timeFmt) %>
                                (<%= slot.getLocation() %>)
                            </option>
                        <%
                            }
                        %>
                    </select>
                    <% if (availableSlots.isEmpty()) { %>
                        <small class="text-muted">No available schedules from your assigned mentor right now.</small>
                    <% } %>
                </div>

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
                    <label for="additionalNotes">Additional Notes</label>
                    <%-- FIX: Tukar attribute id and name field kepada parameter baru --%>
                    <textarea id="additionalNotes" name="additionalNotes" class="form-control" placeholder="Provide any details or specific topics you'd like to discuss..." style="min-height: 120px;"></textarea>
                </div>

                <div class="form-group" style="display: flex; gap: 1rem; margin-top: 2rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;" <%= availableSlots.isEmpty() ? "disabled" : "" %>>Book Appointment</button>
                    <a href="<%= request.getContextPath() %>/ViewAppointmentServlet" class="btn btn-ghost" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>