<%-- 
    Document   : dashboard
    Created on : 5 Apr 2026
    Author     : isyra
    Purpose    : Dashboard page for authenticated users
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // Check if user is logged in
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard - Academic Advisor</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f5f5f5;
            }

            .navbar {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 20px 40px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }

            .navbar h1 {
                font-size: 24px;
                font-weight: 600;
            }

            .nav-right {
                display: flex;
                align-items: center;
                gap: 20px;
            }

            .user-info {
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .user-avatar {
                width: 40px;
                height: 40px;
                background: rgba(255, 255, 255, 0.3);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 20px;
            }

            .logout-btn {
                background: rgba(255, 255, 255, 0.2);
                color: white;
                border: 1px solid white;
                padding: 8px 16px;
                border-radius: 5px;
                cursor: pointer;
                font-size: 14px;
                transition: all 0.3s;
                text-decoration: none;
                display: inline-block;
            }

            .logout-btn:hover {
                background: rgba(255, 255, 255, 0.3);
            }

            .container {
                max-width: 1200px;
                margin: 40px auto;
                padding: 20px;
            }

            .welcome-card {
                background: white;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                margin-bottom: 30px;
            }

            .welcome-card h2 {
                color: #333;
                margin-bottom: 10px;
                font-size: 26px;
            }

            .welcome-card p {
                color: #666;
                font-size: 16px;
                line-height: 1.6;
            }

            .dashboard-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                gap: 20px;
                margin-top: 30px;
            }

            .dashboard-card {
                background: white;
                padding: 25px;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                border-top: 4px solid #667eea;
                transition: all 0.3s;
            }

            .dashboard-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
            }

            .dashboard-card h3 {
                color: #333;
                margin-bottom: 15px;
                font-size: 18px;
            }

            .dashboard-card p {
                color: #666;
                font-size: 14px;
                line-height: 1.6;
                margin-bottom: 15px;
            }

            .btn {
                display: inline-block;
                padding: 10px 20px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                text-decoration: none;
                border-radius: 5px;
                font-size: 13px;
                font-weight: 600;
                transition: all 0.3s;
                border: none;
                cursor: pointer;
            }

            .btn:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            }

            .session-info {
                background: #f9f9f9;
                padding: 15px;
                border-radius: 5px;
                margin-top: 20px;
                font-size: 12px;
                color: #666;
                border-left: 4px solid #667eea;
            }

            @media (max-width: 768px) {
                .navbar {
                    padding: 15px 20px;
                    flex-direction: column;
                    gap: 10px;
                }

                .container {
                    margin: 20px 10px;
                }

                .dashboard-grid {
                    grid-template-columns: 1fr;
                }
            }
        </style>
    </head>
    <body>
        <div class="navbar">
            <h1>Academic Advisor System</h1>
            <div class="nav-right">
                <div class="user-info">
                    <span>Welcome, <strong><%= username %></strong></span>
                </div>
                <a href="logout.jsp" class="logout-btn">Logout</a>
            </div>
        </div>

        <div class="container">
            <div class="welcome-card">
                <h2>Welcome to Academic Advisor Appointment System</h2>
                <p>You have successfully logged in to your account. From here, you can manage your appointments, schedule sessions with your academic advisor, and view your academic progress.</p>
            </div>


        </div>
    </body>
</html>
