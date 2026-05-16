<%-- 
    Document   : login
    Created on : 5 Apr 2026
    Author     : isyraf
    Purpose    : login page for Academic Advisor Appointment System
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Academic Advisor - Login</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <div class="login-container">
            <div class="login-header">
                <h1>Academic Advisor</h1>
                <p>Appointment System Login</p>
            </div>

            <%
                String errorMessage = (String) request.getAttribute("errorMessage");
                if (errorMessage != null) {
            %>
            <div class="error-message">
                <%= errorMessage %>
            </div>
            <%
                }
            %>

            <form method="POST" action="LoginServlet" onsubmit="return validateForm()">
                <div class="form-group">
                    <label for="noMatric">No. Matric</label>
                    <input 
                        type="text" 
                        id="noMatric" 
                        name="noMatric" 
                        placeholder="Enter your matric number"
                        autocomplete="text"
                        required
                    >
                </div>

                <div class="form-group password-toggle">
                    <label for="password">Password</label>
                    <input 
                        type="password" 
                        id="password" 
                        name="password" 
                        placeholder="Enter your password"
                        autocomplete="current-password"
                        required
                    >
                </div>

                <button type="submit" class="login-btn">Sign In</button>

            <div class="demo-credentials">
                <h4>Demo Credentials:</h4>
                <p><strong>Admin:</strong> admin / admin123</p>
                <p><strong>Advisor:</strong> advisor / advisor123</p>
                <p><strong>Student:</strong> student / student123</p>
            </div>
        </div>

        <script>
            function validateForm() {
                const username = document.getElementById('username').value.trim();
                const password = document.getElementById('password').value.trim();

                if (!username) {
                    alert('Please enter your username');
                    document.getElementById('username').focus();
                    return false;
                }

                if (!password) {
                    alert('Please enter your password');
                    document.getElementById('password').focus();
                    return false;
                }

                return true;
            }

            // Set focus to username field on page load
            window.addEventListener('load', function() {
                document.getElementById('username').focus();
            });
        </script>
    </body>
</html>
