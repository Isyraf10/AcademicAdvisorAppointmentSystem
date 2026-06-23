<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Academic Advisor System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<jsp:include page="/includes/header.jsp">
    <jsp:param name="activePage" value="register" />
</jsp:include>

    <div class="register-wrapper">
        <div class="register-card">
            <h2 class="card-title">Create Account</h2>
            <p class="card-subtitle">Join the Academic Advisor System today</p>
            
            <%
                String errorMsg = (String) request.getAttribute("errorMessage");
                if (errorMsg != null) {
            %>
                <div class="alert alert-error"><%= errorMsg %></div>
            <%  } %>

            <form action="<%= request.getContextPath() %>/RegisterServlet" method="POST">
                <div class="form-group">
                    <label for="name">Full Name <span>*</span></label>
                    <input type="text" id="name" name="name" class="form-control" required placeholder="John Doe">
                </div>
                
                <div class="form-group">
                    <label for="email">Email Address (Matric Number) <span>*</span></label>
                    <input type="email" id="email" name="email" class="form-control" required placeholder="s12345@ocean.umt.edu.my">
                </div>
                
                <div class="form-group">
                    <label for="password">Password <span>*</span></label>
                    <input type="password" id="password" name="password" class="form-control" required placeholder="Create a secure password">
                </div>
                
                <div class="form-group">
                    <label for="phoneNumber">Phone Number <span>*</span></label>
                    <input type="text" id="phoneNumber" name="phoneNumber" class="form-control" required placeholder="012-3456789">
                </div>
                
                <div class="form-group">
                    <label for="role">Register As <span>*</span></label>
                    <select id="role" name="role" class="form-select" required>
                        <option value="">-- Select Your Role --</option>
                        <option value="student">Student</option>
                        <option value="advisor">Academic Advisor</option>
                        <option value="admin">System Administrator</option>
                    </select>
                </div>
                
                <button type="submit" class="btn btn-primary btn-full" style="margin-top: 1.5rem;">Create Account</button>
            </form>
            
            <div class="auth-links">
                <p>Already have an account? <a href="<%= request.getContextPath() %>/Login/login.jsp">Sign in here</a></p>
            </div>
        </div>
    </div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>




