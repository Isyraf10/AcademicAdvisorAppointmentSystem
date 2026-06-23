<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Add Schedule</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <script>
        // 1. Store original time slots in memory
        const allSlots = [
            { value: "08:00:00-10:00:00", text: "08:00 AM - 10:00 AM" },
            { value: "10:00:00-12:00:00", text: "10:00 AM - 12:00 PM" },
            { value: "12:00:00-14:00:00", text: "12:00 PM - 02:00 PM" },
            { value: "14:00:00-16:00:00", text: "02:00 PM - 04:00 PM" },
            { value: "16:00:00-18:00:00", text: "04:00 PM - 06:00 PM" }
        ];

        // Show/Hide custom time inputs
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

        // 2. AJAX Function: Remove booked time slots
        function checkAvailableSlots() {
            var dateInput = document.getElementById("scheduleDate").value;
            var select = document.getElementById("timeSlot");

            // Clear dropdown choices when date changes
            select.innerHTML = '<option value="">-- Choose a Slot --</option>';

            // If no date is selected, load all slots
            if(!dateInput) {
                populateDropdown([]); 
                return;
            }

            // Fetch booked slots from database live
            fetch('ScheduleServlet?action=checkDate&date=' + dateInput)
            .then(response => response.text())
            .then(data => {
                var bookedArray = data ? data.split(',') : [];
                populateDropdown(bookedArray); 
            });
        }

        // 3. Rebuild the dropdown menu safely
        function populateDropdown(bookedArray) {
            var select = document.getElementById("timeSlot");
            
            // Only add slots that are NOT in the booked array
            allSlots.forEach(slot => {
                if(!bookedArray.includes(slot.value)) {
                    var opt = document.createElement('option');
                    opt.value = slot.value;
                    opt.innerHTML = slot.text;
                    select.appendChild(opt);
                }
            });

            // Always add the 'Custom Slot' option at the bottom
            var customOpt = document.createElement('option');
            customOpt.value = "custom";
            customOpt.innerHTML = "Custom Slot...";
            customOpt.style.fontWeight = "bold";
            customOpt.style.color = "blue";
            select.appendChild(customOpt);
        }

        // Validate form before submission
        function validateForm() {
            var dateInput = document.getElementById("scheduleDate").value;
            var slotInput = document.getElementById("timeSlot").value;
            if (dateInput === "" || slotInput === "") { 
                alert("Please select a valid date and time."); 
                return false; 
            }
            return true;
        }
        
        // Load slots when page opens
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
        <h2>Create Available Consultation Slot</h2>
        <hr>
        <form action="ScheduleServlet" method="post" onsubmit="return validateForm();">
            <input type="hidden" name="action" value="insert">
            
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
            <button type="submit" class="btn btn-primary" style="font-size: 1.1rem; padding: 10px 20px;">Save Schedule</button>
            <a href="ScheduleServlet?action=list" class="btn btn-warning" style="font-size: 1.1rem; padding: 10px 20px;">Cancel</a>
        </form>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




