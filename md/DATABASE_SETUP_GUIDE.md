# 🔧 Database Configuration Guide

## Your Actual Database Schema

Your phpMyAdmin shows:
- **Database:** academic_advisor  
- **Table:** user (not users)
- **Columns:** noMatric, password, roles

## ✅ Code Changes Applied

All code has been updated to match your database schema:

### 1. **DBConnection.java** (src/java/com/lab/util/)
```
JDBC_URL: jdbc:mysql://localhost:3306/academic_advisor
JDBC_USERNAME: root
JDBC_PASSWORD: (empty)
JDBC_DRIVER: com.mysql.cj.jdbc.Driver
```
✅ **Status:** Ready for XAMPP (default credentials)

### 2. **User.java Model** (src/java/com/lab/model/)
- Changed `username` → `noMatric`
- Changed `role` → `roles`
- Updated all getters/setters and constructors

✅ **Status:** Fields match database columns

### 3. **UserDAO.java** (src/java/com/lab/dao/)
- Changed table name: `users` → `user`
- Updated query to use: `noMatric`, `password`, `roles`
- Removed hashing logic (using plaintext passwords as in your DB)
- Simplified authentication method

✅ **Status:** Queries match your table structure

### 4. **LoginServlet.java** (src/java/com/lab/model/)
- Changed parameter name: `username` → `noMatric`
- Updated session attribute: `session.setAttribute("noMatric", ...)`
- Changed password verification to plaintext comparison

✅ **Status:** Login form now uses noMatric

### 5. **login.jsp** (web/)
- Changed input field: `name="username"` → `name="noMatric"`
- Updated label: "Username" → "No. Matric"
- Updated placeholder text

✅ **Status:** Form sends correct parameter

### 6. **dashboard.jsp** (web/)
- Changed session retrieval: `getAttribute("username")` → `getAttribute("noMatric")`
- Display updated to show "No. Matric" instead of "Username"

✅ **Status:** Dashboard displays correct user field

### 7. **All Servlet Controllers** (ViewAppointmentServlet, CreateAppointmentServlet, UpdateAppointmentServlet, DeleteAppointmentServlet)
- Updated session checks from `getAttribute("username")` → `getAttribute("noMatric")`

✅ **Status:** All servlets check correct session attribute

---

## 📋 Next Steps

### Step 1: Insert Demo Data
Run this SQL in phpMyAdmin SQL tab:

```sql
INSERT INTO user (noMatric, password, roles) VALUES
('A001', 'admin123', 'admin'),
('A002', 'advisor123', 'advisor'),
('S001', 'student123', 'student');
```

Or copy-paste from: `database/seeds/INSERT_DEMO_DATA.sql`

### Step 2: Create Appointments Table (Optional)
If you want to manage appointments, create this table:

```sql
CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    duration INT NOT NULL,
    available_slots INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_type (type)
);
```

### Step 3: Test Login Credentials
| Matric | Password | Role |
|--------|----------|------|
| A001 | admin123 | admin |
| A002 | advisor123 | advisor |
| S001 | student123 | student |

---

## 🧪 Testing Checklist

- [ ] MySQL running in XAMPP
- [ ] Database `academic_advisor` exists
- [ ] Table `user` has columns: noMatric, password, roles
- [ ] Demo data inserted (3 users)
- [ ] Build project in NetBeans (Clean & Build)
- [ ] No compilation errors
- [ ] Login with A001/admin123 → Dashboard loads
- [ ] Try appointments management (depending on your UI)
- [ ] Test role-based access (student vs admin permissions)

---

## ⚠️ Important Notes

1. **Plaintext Passwords:** Your current database stores plaintext passwords. For production, use password hashing (SHA-256, bcrypt, etc.)

2. **XAMPP Credentials:** The code assumes:
   - MySQL server: localhost:3306
   - Username: root
   - Password: (empty)
   
   If different, update `DBConnection.java`

3. **Session Attributes:** Now uses:
   - `session.getAttribute("noMatric")` - user's matric number
   - `session.getAttribute("role")` - user's role
   - `session.getAttribute("loginTime")` - login timestamp

---

## 🐛 Troubleshooting

**"Connection refused" error?**
- ✔️ Ensure MySQL is running in XAMPP
- ✔️ Check database `academic_advisor` exists
- ✔️ Verify credentials in DBConnection.java

**"Table 'user' doesn't exist" error?**
- ✔️ Create table using the SQL provided above
- ✔️ Insert demo data

**Login fails with valid credentials?**
- ✔️ Check password is exactly "admin123" (plaintext)
- ✔️ Verify noMatric value in database (case-sensitive)
- ✔️ Check roles column contains: admin, advisor, or student

---

**All code is ready! Just add the demo data and test.** ✅
