<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<%@ taglib prefix="fn"  uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>My Consultation Logs - Student Archive</title>
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
            <p class="page-subtitle">Personal reference consultation log history archive</p>
        </div>
        <span class="role-badge role-badge--student">
            STUDENT - <c:out value="${userName}" />
        </span>
    </div>

    <%-- Read-Only Metric Information Display Grid Cards --%>
    <div class="stat-grid">
        <div class="stat-card stat-card--accent">
            <span class="stat-value"><c:out value="${totalSessions}" /></span>
            <span class="stat-label">Total Sessions Completed</span>
        </div>
        <div class="stat-card">
            <span class="stat-value">${fn:length(records)}</span>
            <span class="stat-label">Consultation Logs Available</span>
        </div>
        <div class="stat-card">
            <span class="stat-value">View-Only</span>
            <span class="stat-label">Your Access Level</span>
        </div>
    </div>

    <%-- History Feed Card Container List --%>
    <section class="card">
        <h2 class="section-title">My Consultation History</h2>
        <p class="section-desc">A secure read-only verification record of your past advisory sessions.</p>
        <c:choose>
            <c:when test="${empty records}">
                <p class="empty-state">You have no consultation logs yet. Visit your advisor to schedule a session.</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="rec" items="${records}">
                    <div class="log-entry">
                        <div class="log-entry__header">
                            <div class="log-entry__meta">
                                <span class="log-entry__id">Record #<c:out value="${rec.recordId}" /></span>
                                <span class="log-entry__sep"></span>
                                <span class="log-entry__date">
                                    <c:choose>
                                        <c:when test="${not empty rec.createdDate}">
                                            <fmt:formatDate value="${rec.createdDate}" pattern="dd MMMM yyyy, hh:mm a" />
                                        </c:when>
                                        <c:otherwise>Unspecified Date</c:otherwise>
                                    </c:choose>
                                </span>
                                <span class="log-entry__sep"></span>
                                <span>Appointment #<c:out value="${rec.appointmentId}" /></span>
                            </div>
                            <c:set var="stStatus" value="${not empty rec.recordStatus ? rec.recordStatus : 'Active'}" />
                            <span class="status-badge status-badge--${fn:toLowerCase(stStatus)}"><c:out value="${stStatus}" /></span>
                        </div>
                        <div class="log-entry__body">
                            <c:if test="${not empty rec.summary}">
                                <div class="log-entry__section">
                                    <h4 class="log-entry__section-title">Meeting Summary</h4>
                                    <p><c:out value="${rec.summary}" /></p>
                                </div>
                            </c:if>
                            <c:if test="${not empty rec.feedback}">
                                <div class="log-entry__section">
                                    <h4 class="log-entry__section-title">Advisor Feedback</h4>
                                    <p><c:out value="${rec.feedback}" /></p>
                                </div>
                            </c:if>
                            <c:if test="${not empty rec.actionPlan}">
                                <div class="log-entry__section">
                                    <h4 class="log-entry__section-title">Action Plan</h4>
                                    <p><c:out value="${rec.actionPlan}" /></p>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<%-- Shared Footer Fragment Include --%>
<jsp:include page="/includes/footer.jsp" />

</body>
</html>




