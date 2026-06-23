<%@ page contentType="text/html;charset=UTF-8" %>
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
                    <label for="advisorId">Select Advisor <span>*</span></label>
                    <select id="advisorId" name="advisorId" class="form-select" required>
                        <option value="">-- Choose an Advisor --</option>
                        <option value="1">Dr. Ahmad Rahman</option>
                        <option value="2">Prof. Noor Malik</option>
                        <option value="3">Dr. Sarah Johnson</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="scheduleDate">Preferred Date <span>*</span></label>
                    <input type="date" id="scheduleDate" name="scheduleDate" class="form-control" required>
                </div>

                <div class="form-group">
                    <label for="timeSlot">Time Slot <span>*</span></label>
                    <select id="timeSlot" name="timeSlot" class="form-select" required>
                        <option value="">-- Select Time --</option>
                        <option value="09:00-10:00">9:00 AM - 10:00 AM</option>
                        <option value="10:00-11:00">10:00 AM - 11:00 AM</option>
                        <option value="11:00-12:00">11:00 AM - 12:00 PM</option>
                        <option value="13:00-14:00">1:00 PM - 2:00 PM</option>
                        <option value="14:00-15:00">2:00 PM - 3:00 PM</option>
                        <option value="15:00-16:00">3:00 PM - 4:00 PM</option>
                    </select>
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
                    <label for="description">Additional Notes</label>
                    <textarea id="description" name="description" class="form-control" placeholder="Provide any details or specific topics you'd like to discuss..." style="min-height: 120px;"></textarea>
                </div>

                <div class="form-group" style="display: flex; gap: 1rem; margin-top: 2rem;">
                    <button type="submit" class="btn btn-primary" style="flex: 1;">Book Appointment</button>
                    <a href="<%= request.getContextPath() %>/ViewAppointmentServlet" class="btn btn-ghost" style="flex: 1; text-align: center;">Cancel</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        document.getElementById('scheduleDate').addEventListener('change', function() {
            const date = this.value;
            if (date) {
                console.log('Selected date:', date);
                // Future AJAX call to check availability
            }
        });
    </script>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>





