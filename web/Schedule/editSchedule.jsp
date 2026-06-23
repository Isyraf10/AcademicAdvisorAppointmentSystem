<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Schedule</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script>
        function toggleCustomSlot() {
            var slotInput = document.getElementById("timeSlot").value;
            var customDiv = document.getElementById("customSlotDiv");
            if (slotInput === "custom") {
                customDiv.style.display = "block";
                document.getElementById("customStartTime").required = true;
                document.getElementById("customEndTime").required = true;
            } else {
                customDiv.style.display = "none";
                document.getElementById("customStartTime").required = false;
                document.getElementById("customEndTime").required = false;
            }
        }
        window.onload = function() { toggleCustomSlot(); };

        function validateForm() {
            var slotInput = document.getElementById("timeSlot").value;
            if (slotInput === "custom") {
                var start = document.getElementById("customStartTime").value;
                var end = document.getElementById("customEndTime").value;
                if (end <= start) {
                    alert("Ralat: Custom end time must be strictly later than start time.");
                    return false;
                }
            }
            return true;
        }
    </script>
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="schedules" />
</jsp:include>

    <div class="container">
        <h2> Edit Consultation Slot</h2>
        <hr>
        
        <c:set var="dbSlot" value="${schedule.startTime}-${schedule.endTime}" />
        <c:set var="isCustom" value="true" />
        <c:if test="${dbSlot == '08:00:00-10:00:00' || dbSlot == '10:00:00-12:00:00' || dbSlot == '12:00:00-14:00:00' || dbSlot == '14:00:00-16:00:00' || dbSlot == '16:00:00-18:00:00'}">
            <c:set var="isCustom" value="false" />
        </c:if>

        <form action="ScheduleServlet" method="post" onsubmit="return validateForm();">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="${schedule.scheduleId}">
            
            <div class="form-group">
                <label>Schedule Date:</label>
                <input type="date" name="scheduleDate" value="${schedule.scheduleDate}" required>
            </div>
            
            <div class="form-group">
                <label>Select Time Slot:</label>
                <select id="timeSlot" name="timeSlot" onchange="toggleCustomSlot()" required>
                    <option value="08:00:00-10:00:00" <c:if test="${dbSlot == '08:00:00-10:00:00'}">selected</c:if>>08:00 AM - 10:00 AM</option>
                    <option value="10:00:00-12:00:00" <c:if test="${dbSlot == '10:00:00-12:00:00'}">selected</c:if>>10:00 AM - 12:00 PM</option>
                    <option value="12:00:00-14:00:00" <c:if test="${dbSlot == '12:00:00-14:00:00'}">selected</c:if>>12:00 PM - 02:00 PM</option>
                    <option value="14:00:00-16:00:00" <c:if test="${dbSlot == '14:00:00-16:00:00'}">selected</c:if>>02:00 PM - 04:00 PM</option>
                    <option value="16:00:00-18:00:00" <c:if test="${dbSlot == '16:00:00-18:00:00'}">selected</c:if>>04:00 PM - 06:00 PM</option>
                    <option value="custom" <c:if test="${isCustom}">selected</c:if> style="font-weight:bold; color:blue;">Custom Slot...</option>
                </select>
            </div>

            <div id="customSlotDiv" style="display: none; background: #e9ecef; padding: 15px; border-radius: 5px; margin-bottom: 15px; border-left: 5px solid #ffc107;">
                <div class="form-group">
                    <label>Custom Start Time:</label>
                    <input type="time" id="customStartTime" name="customStartTime" value="${isCustom ? schedule.startTime : ''}">
                </div>
                <div class="form-group">
                    <label>Custom End Time:</label>
                    <input type="time" id="customEndTime" name="customEndTime" value="${isCustom ? schedule.endTime : ''}">
                </div>
            </div>
            
            <div class="form-group">
                <label>Location:</label>
                <input type="text" name="location" value="${schedule.location}" required>
            </div>
            
            <div class="form-group">
                <label>Status:</label>
                <select name="status">
                    <option value="Available" <c:if test="${schedule.status == 'Available'}">selected</c:if>>Available</option>
                    <option value="Busy" <c:if test="${schedule.status == 'Busy'}">selected</c:if>>Busy</option>
                </select>
            </div>
            
            <br>
            <button type="submit" class="btn btn-primary" style="font-size: 1.1rem; padding: 10px 20px;"> Update Schedule</button>
            <a href="ScheduleServlet?action=list" class="btn btn-warning" style="font-size: 1.1rem; padding: 10px 20px;">Cancel</a>
        </form>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




