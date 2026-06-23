<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<%@ taglib prefix="fn"  uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Advisory Group Logs - Faculty Workstation</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>

<%-- Shared Header Fragment Include --%>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="records" />
</jsp:include>

<main class="container">

    <%-- Page Title and Profile Header --%>
    <div class="page-header">
        <div>
            <h1 class="page-title">Academic Record &amp; Progress Tracking</h1>
            <p class="page-subtitle">Historical group consultation logs and student execution mapping arrays</p>
        </div>
        <span class="role-badge role-badge--advisor">
            ADVISOR - <c:out value="${userName}" />
        </span>
    </div>

    <%-- Operations Feedback Alerts --%>
    <c:if test="${not empty flashMsg}">
        <c:choose>
            <c:when test="${flashMsg eq 'created'}">
                <div class="alert alert--success">Consultation log created successfully.</div>
            </c:when>
            <c:when test="${flashMsg eq 'denied'}">
                <div class="alert alert--error">Operational Access Denied: Restricted action pathway.</div>
            </c:when>
        </c:choose>
    </c:if>

    <%-- Top Section: Student Session metrics grid bars --%>
    <c:if test="${not empty progressList}">
        <section class="card">
            <h2 class="section-title">Student Progress Tracking</h2>
            <p class="section-desc">Session counts for all students assigned to your advisory group.</p>
            <div class="progress-grid">
                <c:forEach var="prog" items="${progressList}">
                    <div class="progress-card">
                        <div class="progress-student"><c:out value="${prog.studentName}" /></div>
                        <div class="progress-metric">
                            <span class="progress-count"><c:out value="${prog.sessionCount}" /></span>
                            <span class="progress-label">session<c:if test="${prog.sessionCount != 1}">s</c:if> completed</span>
                        </div>
                        <div class="progress-bar-wrap">
                            <div class="progress-bar" data-sessions="${prog.sessionCount}"></div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </section>
    </c:if>

    <%-- Middle Section: Logging form inputs --%>
    <section class="card">
        <h2 class="section-title">Log New Consultation Session</h2>
        <p class="section-desc">Select a student to view their unlogged appointments, then add consultation notes.</p>
        
        <form action="${pageContext.request.contextPath}/records/dashboard" method="post" class="record-form">
            <input type="hidden" name="action" value="create" />
            
            <div class="form-grid">
                <div class="form-group">
                    <label for="studentSelect" class="form-label">Select Student <span class="required">*</span></label>
                    <select id="studentSelect" class="form-control" required>
                        <option value="">-- Choose a Student --</option>
                        <c:forEach var="student" items="${myStudents}">
                            <%-- Corrected: changed student.Id to student.id --%>
                            <option value="${student.id}"><c:out value="${student.name}" /></option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="appointmentId-adv" class="form-label">Target Appointment <span class="required">*</span></label>
                    <select id="appointmentId-adv" name="appointmentId" class="form-control" disabled required>
                        <option value="">-- Choose a Student First --</option>
                    </select>
                </div>

                <div class="form-group form-group--full">
                    <label for="summary-adv" class="form-label">Meeting Summary <span class="required">*</span></label>
                    <textarea id="summary-adv" name="summary" rows="4" class="form-control" required></textarea>
                </div>
                <div class="form-group form-group--full">
                    <label for="feedback-adv" class="form-label">Advisor Feedback</label>
                    <textarea id="feedback-adv" name="feedback" rows="3" class="form-control"></textarea>
                </div>
                <div class="form-group form-group--full">
                    <label for="actionPlan-adv" class="form-label">Action Plan</label>
                    <textarea id="actionPlan-adv" name="actionPlan" rows="2" class="form-control"></textarea>
                </div>
            </div>
            <button type="submit" class="btn btn--primary">Save Consultation Log</button>
        </form>
    </section>

    <%-- Bottom Section: Advisor history data array --%>
    <section class="card">
        <h2 class="section-title">Your Students' Consultation History</h2>
        <c:choose>
            <c:when test="${empty records}">
                <p class="empty-state">No consultation logs have been created for your advisory group yet.</p>
            </c:when>
            <c:otherwise>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Record ID</th>
                                <th>Appt. ID</th>
                                <th>Student</th>
                                <th>Summary</th>
                                <th>Action Plan</th>
                                <th>Status</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="rec" items="${records}">
                                <tr>
                                    <td class="mono">#<c:out value="${rec.recordId}" /></td>
                                    <td class="mono"><c:out value="${rec.appointmentId}" /></td>
                                    <td><c:out value="${not empty rec.studentName ? rec.studentName : 'Unknown Student'}" /></td>
                                    <td class="summary-cell">
                                        <c:choose>
                                            <c:when test="${not empty rec.summary}">
                                                <span title="<c:out value='${rec.summary}' />">
                                                    <c:out value="${fn:substring(rec.summary, 0, 70)}" /><c:if test="${fn:length(rec.summary) > 70}"></c:if>
                                                </span>
                                            </c:when>
                                            <c:otherwise><span class="text-muted">No summary</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="summary-cell">
                                        <c:choose>
                                            <c:when test="${not empty rec.actionPlan}">
                                                <span title="<c:out value='${rec.actionPlan}' />">
                                                    <c:out value="${fn:substring(rec.actionPlan, 0, 50)}" /><c:if test="${fn:length(rec.actionPlan) > 50}"></c:if>
                                                </span>
                                            </c:when>
                                            <c:otherwise><span class="text-muted">None</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:set var="advStatus" value="${not empty rec.recordStatus ? rec.recordStatus : 'Active'}" />
                                        <span class="status-badge status-badge--${fn:toLowerCase(advStatus)}"><c:out value="${advStatus}" /></span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty rec.createdDate}">
                                                <fmt:formatDate value="${rec.createdDate}" pattern="dd MMM yyyy" />
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<%-- Shared Footer Fragment Include --%>
<jsp:include page="/includes/footer.jsp" />

<%-- Scripts specific to the Advisor logic pipeline --%>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        // Progress Bar Width Sync Animation
        var bars = document.querySelectorAll('.progress-bar[data-sessions]');
        bars.forEach(function (bar) {
            var sessions = parseInt(bar.getAttribute('data-sessions'), 10) || 0;
            var pct      = Math.min(sessions / 10 * 100, 100);
            bar.style.width = pct + '%';
        });

        // Dynamic Appointment Dropdown Logic API Asynchronous Integration
        const studentSelect = document.getElementById('studentSelect');
        const appointmentSelect = document.getElementById('appointmentId-adv');

        if (studentSelect && appointmentSelect) {
            studentSelect.addEventListener('change', function() {
                const studentId = this.value;
                if (!studentId) {
                    appointmentSelect.innerHTML = '<option value="">-- Choose a Student First --</option>';
                    appointmentSelect.disabled = true;
                    return;
                }

                fetch('${pageContext.request.contextPath}/api/appointments?studentId=' + studentId)
                    .then(response => response.json())
                    .then(data => {
                        appointmentSelect.innerHTML = '<option value="">-- Select an Appointment --</option>';
                        if(data.length === 0) {
                            appointmentSelect.innerHTML = '<option value="">No unlogged appointments found</option>';
                            appointmentSelect.disabled = true;
                            return;
                        }
                        data.forEach(appt => {
                            const option = document.createElement('option');
                            option.value = appt.id;
                            option.textContent = 'Booking Reference: #' + appt.id;
                            appointmentSelect.appendChild(option);
                        });
                        appointmentSelect.disabled = false;
                    })
                    .catch(error => {
                        console.error("Error fetching appointments:", error);
                        appointmentSelect.innerHTML = '<option value="">Error loading appointments</option>';
                        appointmentSelect.disabled = true;
                    });
            });
        }
    });
</script>

</body>
</html>