<%-- 
    Document   : login
    Created on : 5 Apr 2026
    Author     : isyra
    Purpose    : Professional login page for Academic Advisor Appointment System
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Academic Advisor - Login</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                padding: 20px;
            }

            .login-container {
                background: white;
                border-radius: 10px;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
                width: 100%;
                max-width: 420px;
                padding: 50px 40px;
                animation: slideIn 0.5s ease-out;
            }

            @keyframes slideIn {
                from {
                    opacity: 0;
                    transform: translateY(-20px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .login-header {
                text-align: center;
                margin-bottom: 40px;
            }

            .login-header h1 {
                color: #333;
                font-size: 28px;
                margin-bottom: 10px;
                font-weight: 600;
            }

            .login-header p {
                color: #666;
                font-size: 14px;
            }

            .form-group {
                margin-bottom: 20px;
            }

            label {
                display: block;
                margin-bottom: 8px;
                color: #333;
                font-weight: 500;
                font-size: 14px;
            }

            input[type="text"],
            input[type="password"] {
                width: 100%;
                padding: 12px 15px;
                border: 2px solid #e0e0e0;
                border-radius: 5px;
                font-size: 14px;
                transition: all 0.3s ease;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }

            input[type="text"]:focus,
            input[type="password"]:focus {
                outline: none;
                border-color: #667eea;
                box-shadow: 0 0 10px rgba(102, 126, 234, 0.1);
            }

            .password-toggle {
                position: relative;
            }

            .login-btn {
                width: 100%;
                padding: 12px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border: none;
                border-radius: 5px;
                font-size: 16px;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s ease;
            }

            .login-btn:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
            }

            .login-btn:active {
                transform: translateY(0);
            
                background-color: #fee;
                color: #c33;
                padding: 12px 15px;
                border-radius: 5px;
                margin-bottom: 20px;
                font-size: 13px;
                border-left: 4px solid #c33;
                animation: shake 0.5s;
            }

            @keyframes shake {
                0%, 100% { transform: translateX(0); }
                25% { transform: translateX(-5px); }
                75% { transform: translateX(5px); }
            }

            .success-message {
                background-color: #efe;
                color: #3c3;
                padding: 12px 15px;
                border-radius: 5px;
                margin-bottom: 20px;
                font-size: 13px;
                border-left: 4px solid #3c3;
            }

            .demo-credentials {
                display: none;
            }

            @media (max-width: 480px) {
                .login-container {
                    padding: 30px 20px;
                }

                .login-header h1 {
                    font-size: 24px;
                }

                input[type="text"],
                input[type="password"] {
                    padding: 10px 12px;
                    font-size: 16px;
                }
            }
        </style>
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
                    <label for="username">Username</label>
                    <input 
                        type="text" 
                        id="username" 
                        name="username" 
                        placeholder="Enter your username"
                        autocomplete="username"
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
