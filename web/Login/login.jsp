<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Academic Advisor System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="login" />
</jsp:include>

    <div class="login-wrapper">
        <div class="login-card">
            <h2 class="card-title">Welcome Back</h2>
            <p class="card-subtitle">Sign in to your account to continue</p>
            
            <% if ("1".equals(request.getParameter("error"))) { %>
                <div class="alert alert-error">Invalid email or password. Please try again.</div>
            <% } else if ("1".equals(request.getParameter("logout"))) { %>
                <div class="alert alert-success">You have successfully logged out.</div>
            <% } else {
                String errorMsg = (String) request.getAttribute("errorMessage");
                String successMsg = (String) request.getAttribute("successMessage");
                
                if (errorMsg != null) {
            %>
                <div class="alert alert-error"><%= errorMsg %></div>
            <%  } else if (successMsg != null) { %>
                <div class="alert alert-success"><%= successMsg %></div>
            <%  }
               } %>

            <form action="<%= request.getContextPath() %>/LoginServlet" method="POST">
                <div class="form-group">
                    <label for="email">Email Address <span>*</span></label>
                    <input type="email" id="email" name="email" class="form-control" required placeholder="you@example.com">
                </div>
                
                <div class="form-group">
                    <label for="password">Password <span>*</span></label>
                    <input type="password" id="password" name="password" class="form-control" required placeholder="Enter your password">
                </div>
                
                <button type="submit" class="btn btn-primary btn-full" style="margin-top: 1.5rem;">Sign In</button>
            </form>

            <div class="auth-links">
                <p>Don't have an account? <a href="<%= request.getContextPath() %>/Login/register.jsp">Create one now</a></p>
            </div>
        </div>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




