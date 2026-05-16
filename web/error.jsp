<%-- 
    Document   : error
    Created on : 12 Apr 2026
    Author     : isyra
    Purpose    : Error page for Academic Advisor System
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Error - Academic Advisor System</title>
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
                align-items: center;
                justify-content: center;
                padding: 20px;
            }

            .error-container {
                background: white;
                padding: 50px;
                border-radius: 10px;
                box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
                max-width: 600px;
                text-align: center;
            }

            .error-code {
                font-size: 4em;
                color: #ff6b6b;
                font-weight: bold;
                margin-bottom: 20px;
            }

            h1 {
                color: #333;
                margin-bottom: 15px;
                font-size: 2em;
            }

            .error-message {
                color: #666;
                font-size: 1.1em;
                margin-bottom: 30px;
                line-height: 1.6;
            }

            .error-details {
                background: #f5f5f5;
                padding: 20px;
                border-radius: 5px;
                margin-bottom: 30px;
                text-align: left;
                max-height: 200px;
                overflow-y: auto;
            }

            .error-details pre {
                color: #d32f2f;
                font-size: 0.85em;
                white-space: pre-wrap;
                word-wrap: break-word;
            }

            .button-group {
                display: flex;
                gap: 10px;
                justify-content: center;
                flex-wrap: wrap;
            }

            .btn {
                padding: 12px 30px;
                border: none;
                border-radius: 5px;
                font-size: 1em;
                cursor: pointer;
                text-decoration: none;
                display: inline-block;
                transition: all 0.3s;
            }

            .btn-home {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
            }

            .btn-home:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            }

            .btn-back {
                background: #e0e0e0;
                color: #333;
            }

            .btn-back:hover {
                background: #d0d0d0;
            }

            .contact-info {
                color: #999;
                font-size: 0.9em;
                margin-top: 30px;
                padding-top: 30px;
                border-top: 1px solid #ddd;
            }
        </style>
    </head>
    <body>
        <div class="error-container">
            <div class="error-code">
                <%
                    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
                    String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
                    
                    if (statusCode == null) {
                        statusCode = 500;
                    }
                    
                    out.print(statusCode);
                %>
            </div>

            <h1>
                <%
                    switch(statusCode) {
                        case 404:
                            out.print("Page Not Found");
                            break;
                        case 403:
                            out.print("Access Denied");
                            break;
                        case 500:
                            out.print("Internal Server Error");
                            break;
                        default:
                            out.print("Error");
                    }
                %>
            </h1>

            <div class="error-message">
                <%
                    switch(statusCode) {
                        case 404:
                            out.print("The page you're looking for doesn't exist or has been moved.");
                            break;
                        case 403:
                            out.print("You don't have permission to access this resource. Contact your administrator if you believe this is an error.");
                            break;
                        case 500:
                            out.print("Something went wrong on our end. Please try again later.");
                            break;
                        default:
                            out.print("An error occurred while processing your request.");
                    }
                %>
            </div>

            <%
                String exceptionMessage = (String) request.getAttribute("javax.servlet.error.message");
                Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
                
                if (exception != null) {
            %>
                <div class="error-details">
                    <strong>Details:</strong><br>
                    <pre><%= exception.getMessage() %></pre>
                </div>
            <%
                }
            %>

            <div class="button-group">
                <a href="index.html" class="btn btn-home">🏠 Go to Home</a>
                <button class="btn btn-back" onclick="window.history.back()">← Go Back</button>
            </div>

            <div class="contact-info">
                <p>If you need further assistance, please contact the system administrator.</p>
                <p>Error Code: <%= statusCode %></p>
            </div>
        </div>
    </body>
</html>
