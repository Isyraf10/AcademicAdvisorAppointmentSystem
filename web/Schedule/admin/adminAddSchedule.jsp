<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin - Add Schedule</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script>
        // 1. Store original time slots
        const allSlots = [
            { value: "08:00:00-10:00:00", text: "08:00 AM - 10:00 AM" },
            { value: "10:00:00-12:00:00", text: "10:00 AM - 12:00 PM" },
            { value: "12:00:00-14:00:00", text: "12:00 PM - 02:00 PM" },
            { value: "14:00:00-16:00:00", text: "02:00 PM - 04:00 PM" },
            { value: "16:00:00-18:00:00", text: "04:00 PM - 06:00 PM" }
        ];

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

        // 2. AJAX Function: Check both Date and Advisor ID
        function checkAvailableSlots() {
            var dateInput = document.getElementById("scheduleDate").value;
            var advisorInput = document.getElementById("advisorId").value; 
            var select = document.getElementById("timeSlot");

            // Reset dropdown
            select.innerHTML = '<option value="">-- Choose a Slot --</option>';

            // Must select both date and advisor before fetching data
            if(!dateInput || !advisorInput) {
                populateDropdown([]); 
                return;
            }

            // Fetch data from Servlet
            fetch('ScheduleServlet?action=checkDate&date=' + dateInput + '&advisorId=' + advisorInput)
            .then(response => response.text())
            .then(data => {
                var bookedArray = data ? data.split(',') : [];
                populateDropdown(bookedArray); 
            });
        }

        function populateDropdown(bookedArray) {
            var select = document.getElementById("timeSlot");
            allSlots.forEach(slot => {
                if(!bookedArray.includes(slot.value)) {
                    var opt = document.createElement('option');
                    opt.value = slot.value;
                    opt.innerHTML = slot.text;
                    select.appendChild(opt);
                }
            });
            var customOpt = document.createElement('option');
            customOpt.value = "custom";
            customOpt.innerHTML = "Custom Slot...";
            customOpt.style.fontWeight = "bold";
            customOpt.style.color = "blue";
            select.appendChild(customOpt);
        }

        function validateForm() {
            var advisorInput = document.getElementById("advisorId").value;
            var dateInput = document.getElementById("scheduleDate").value;
            var slotInput = document.getElementById("timeSlot").value;
            
            if (advisorInput === "" || dateInput === "" || slotInput === "") { 
                alert("Please complete all fields (Advisor, Date, and Time)."); 
                return false; 
            }
            return true;
        }
        
        window.onload = function() {
            populateDropdown([]);
        };
    </script>
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="schedules" />
</jsp:include>

    <div class="container">
        <h2>Admin: Create Schedule for Advisor</h2>
        <hr>
        <form action="ScheduleServlet" method="post" onsubmit="return validateForm();">
            <input type="hidden" name="action" value="insert">
            
            <div class="form-group">
                <label>Select Advisor:</label>
                <select id="advisorId" name="advisorId" onchange="checkAvailableSlots()" required>
                    <option value="">-- Choose Advisor --</option>
                    <option value="1">Dr. Ahmad (ID: 1)</option>
                    <option value="2">Dr. Siti (ID: 2)</option>
                    <option value="3">Prof. Razak (ID: 3)</option>
                </select>
            </div>

            <div class="form-group">
                <label>Schedule Date:</label>
                <input type="date" id="scheduleDate" name="scheduleDate" onchange="checkAvailableSlots()" required>
            </div>
            
            <div class="form-group">
                <label>Select Time Slot:</label>
                <select id="timeSlot" name="timeSlot" onchange="toggleCustomSlot()" required>
                    <option value="">-- Choose a Slot --</option>
                </select>
            </div>

            <div id="customSlotDiv" style="display: none; background: #e9ecef; padding: 15px; border-radius: 5px; margin-bottom: 15px; border-left: 5px solid #ffc107;">
                <div class="form-group"><label>Custom Start Time:</label><input type="time" id="customStartTime" name="customStartTime"></div>
                <div class="form-group"><label>Custom End Time:</label><input type="time" id="customEndTime" name="customEndTime"></div>
            </div>
            
            <div class="form-group">
                <label>Location (Office/Online Link):</label>
                <input type="text" name="location" placeholder="e.g., MP3 OR https://meet.google.com/..." required>
            </div>
            
            <br>
            <button type="submit" class="btn" style="background: #343a40; color: white; font-size: 1.1rem; padding: 10px 20px;">Save Schedule (Admin)</button>
            <a href="ScheduleServlet?action=adminList" class="btn btn-warning" style="font-size: 1.1rem; padding: 10px 20px;">Cancel</a>
        </form>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




