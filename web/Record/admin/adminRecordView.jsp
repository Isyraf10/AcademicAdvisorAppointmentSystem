<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<%@ taglib prefix="fn"  uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Master Consultation Logs - Admin Terminal</title>
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
            <p class="page-subtitle">Master administrative consultation logs database terminal</p>
        </div>
        <span class="role-badge role-badge--admin">
            ADMIN - <c:out value="${userName}" />
        </span>
    </div>

    <%-- Operations Feedback Alerts --%>
    <c:if test="${not empty flashMsg}">
        <c:choose>
            <c:when test="${flashMsg eq 'created'}">
                <div class="alert alert--success">Consultation log created successfully.</div>
            </c:when>
            <c:when test="${flashMsg eq 'updated'}">
                <div class="alert alert--success">Record status updated successfully.</div>
            </c:when>
            <c:when test="${flashMsg eq 'deleted'}">
                <div class="alert alert--warning">Record deleted from the system database archive ledger.</div>
            </c:when>
            <c:when test="${flashMsg eq 'denied'}">
                <div class="alert alert--error">Access Denied: Insufficient authorization permissions.</div>
            </c:when>
        </c:choose>
    </c:if>

    <%-- System Metric Stats Counter Grid --%>
    <div class="stat-grid">
        <div class="stat-card">
            <span class="stat-value"><c:out value="${totalAllRecords}" /></span>
            <span class="stat-label">Total Consultation Logs</span>
        </div>
        <div class="stat-card">
            <span class="stat-value">${fn:length(records)}</span>
            <span class="stat-label">Records Currently Shown</span>
        </div>
        <div class="stat-card stat-card--accent">
            <span class="stat-value">Admin</span>
            <span class="stat-label">Full Master Control</span>
        </div>
    </div>

    <%-- Form: Admin Direct Manual Overlay Log Generation --%>
    <section class="card">
        <h2 class="section-title">Create New Consultation Log</h2>
        <form action="${pageContext.request.contextPath}/records/dashboard" method="post" class="record-form">
            <input type="hidden" name="action" value="create" />
            <div class="form-grid">
                <div class="form-group">
                    <label for="appointmentId-admin" class="form-label">Appointment ID</label>
                    <input type="number" id="appointmentId-admin" name="appointmentId" class="form-control" placeholder="e.g. 1001" required />
                </div>
                <div class="form-group form-group--full">
                    <label for="summary-admin" class="form-label">Meeting Summary</label>
                    <textarea id="summary-admin" name="summary" rows="3" class="form-control" required></textarea>
                </div>
                <div class="form-group form-group--full">
                    <label for="feedback-admin" class="form-label">Advisor Feedback</label>
                    <textarea id="feedback-admin" name="feedback" rows="3" class="form-control"></textarea>
                </div>
                <div class="form-group form-group--full">
                    <label for="actionPlan-admin" class="form-label">Action Plan</label>
                    <textarea id="actionPlan-admin" name="actionPlan" rows="2" class="form-control"></textarea>
                </div>
            </div>
            <button type="submit" class="btn btn--primary">Save Consultation Log</button>
        </form>
    </section>

    <%-- Master Table Ledger Panel --%>
    <section class="card">
        <h2 class="section-title">Master Consultation Log</h2>
        <c:choose>
            <c:when test="${empty records}">
                <p class="empty-state">No consultation records found in the system.</p>
            </c:when>
            <c:otherwise>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Record ID</th>
                                <th>Appt. ID</th>
                                <th>Student</th>
                                <th>Advisor</th>
                                <th>Summary</th>
                                <th>Status</th>
                                <th>Created</th>
                                <th class="col-actions">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="rec" items="${records}">
                                <tr>
                                    <td class="mono">#<c:out value="${rec.recordId}" /></td>
                                    <td class="mono"><c:out value="${rec.appointmentId}" /></td>
                                    <td><c:out value="${not empty rec.studentName ? rec.studentName : 'Unknown Student'}" /></td>
                                    <td><c:out value="${not empty rec.advisorName ? rec.advisorName : 'Unknown Advisor'}" /></td>
                                    <td class="summary-cell">
                                        <c:choose>
                                            <c:when test="${not empty rec.summary}">
                                                <span title="<c:out value='${rec.summary}' />">
                                                    <c:out value="${fn:substring(rec.summary, 0, 60)}" /><c:if test="${fn:length(rec.summary) > 60}"></c:if>
                                                </span>
                                            </c:when>
                                            <c:otherwise><span class="text-muted">No description</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:set var="admStatus" value="${not empty rec.recordStatus ? rec.recordStatus : 'Active'}" />
                                        <span class="status-badge status-badge--${fn:toLowerCase(admStatus)}"><c:out value="${admStatus}" /></span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty rec.createdDate}">
                                                <fmt:formatDate value="${rec.createdDate}" pattern="dd MMM yyyy" />
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="action-cell">
                                        <form action="${pageContext.request.contextPath}/records/dashboard" method="post" class="inline-form">
                                            <input type="hidden" name="action" value="updateStatus" />
                                            <input type="hidden" name="recordId" value="${rec.recordId}" />
                                            <select name="newStatus" class="form-control form-control--sm">
                                                <option value="Active" ${rec.recordStatus eq 'Active' ? 'selected' : ''}>Active</option>
                                                <option value="Graduated" ${rec.recordStatus eq 'Graduated' ? 'selected' : ''}>Graduated</option>
                                                <option value="Archived" ${rec.recordStatus eq 'Archived' ? 'selected' : ''}>Archived</option>
                                            </select>
                                            <button type="submit" class="btn btn--sm btn--secondary" title="Update status">Update</button>
                                        </form>
                                        <form action="${pageContext.request.contextPath}/records/dashboard" method="post" class="inline-form" onsubmit="return confirm('Delete Record #${rec.recordId}? This cannot be undone.');">
                                            <input type="hidden" name="action" value="delete" />
                                            <input type="hidden" name="recordId" value="${rec.recordId}" />
                                            <button type="submit" class="btn btn--sm btn--danger" title="Delete record">Delete</button>
                                        </form>
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

</body>
</html>




