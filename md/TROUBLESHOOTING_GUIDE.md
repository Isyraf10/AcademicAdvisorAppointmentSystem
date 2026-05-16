# 🔍 TROUBLESHOOTING CHECKLIST - Database Connection Issues

## Langkah 1: Check MySQL Status ✅

### Buka XAMPP Control Panel
```
1. Cari "XAMPP Control Panel" di Start Menu
2. Pastikan MySQL status = "Running" (Green indicator)
3. Jika belum Running, klik "Start" button di sebelah MySQL
```

**Kalau masih error:**
```
- Klik "Logs" button untuk MySQL
- Cari "error" atau "failed"
- Screenshot dan report
```

---

## Langkah 2: Verify MySQL Credentials ✅

### Test Connection Direct
```
1. Buka phpMyAdmin: localhost/phpmyadmin
2. Kalau boleh login → MySQL/Credentials OK
3. Kalau access denied → Username/Password salah
```

**Current DBConnection.java Settings:**
```
JDBC_URL: jdbc:mysql://localhost:3306/academic_advisor
Username: root
Password: (empty)
Driver: com.mysql.cj.jdbc.Driver
```

**Jika berbeda, update DBConnection.java:**
```java
private static final String JDBC_URL = "jdbc:mysql://YOUR_HOST:YOUR_PORT/YOUR_DATABASE";
private static final String JDBC_USERNAME = "YOUR_USERNAME";
private static final String JDBC_PASSWORD = "YOUR_PASSWORD";
```

---

## Langkah 3: Check Database & Table Exists ✅

### Di phpMyAdmin, run this SQL:
```sql
SHOW DATABASES;
USE academic_advisor;
DESC user;
DESC appointments;
```

**Should return:**
- Database `academic_advisor` ✓
- Table `user` with columns: noMatric, password, roles ✓
- Table `appointments` (optional) ✓

**Kalau table tidak ada, buat:**
```sql
CREATE TABLE user (
    noMatric VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(20) NOT NULL
);
```

---

## Langkah 4: Insert Demo Data ✅

### Run this SQL in phpMyAdmin:
```sql
INSERT INTO user (noMatric, password, roles) VALUES
('A001', 'admin123', 'admin'),
('A002', 'advisor123', 'advisor'),
('S001', 'student123', 'student');

SELECT * FROM user;
```

**Expected output: 3 rows inserted**

---

## Langkah 5: Check MySQL JDBC Driver ✅

### Location Check
```
Project Root/build/web/WEB-INF/lib/mysql-connector-j-9.6.0.jar
```

**If NOT FOUND:**

**Option A: Add via NetBeans**
```
1. Right-click Project → Properties
2. Go to Libraries panel
3. Click "Add Library..."
4. Select "MySQL JDBC Driver"
5. Click Add Library
6. Clean & Build Project
```

**Option B: Manual**
```
1. Download: 
   https://dev.mysql.com/downloads/connector/j/
2. Copy JAR to: 
   Project/web/WEB-INF/lib/
3. Right-click Project → Properties → Libraries
4. Add the JAR file
```

---

## Langkah 6: Check NetBeans Output Window ✅

### Open Output Window
```
1. Window → Output → Output (Alt+Shift+O)
2. Select tab: "Apache Tomcat" or your server
3. Look for these error patterns:
```

### Common Errors & Solutions

| Error | Penyebab | Solusi |
|-------|---------|--------|
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | MySQL JDBC JAR tidak ada di classpath | Add MySQL JDBC Library (Langkah 5) |
| `Access denied for user 'root'@'localhost'` | Password/Username salah | Update DBConnection.java |
| `Communications link failure` | MySQL tidak running | Start MySQL di XAMPP |
| `Unknown database 'academic_advisor'` | Database tidak ada | Buat database di phpMyAdmin |
| `Table 'user' doesn't exist` | Table tidak ada | Create table dengan SQL di atas |
| `SEVERE: Error configuring application listener` | web.xml error atau servlet class tidak ada | Check web.xml servlet class names |

---

## Langkah 7: Test Database Connection ✅

### Buat test file (Optional)
```java
// File: TestDB.java
import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/academic_advisor", 
                "root", 
                ""
            );
            System.out.println("✓ Database Connection SUCCESS!");
            conn.close();
        } catch (Exception e) {
            System.out.println("✗ Connection FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

**Run:**
```
javac TestDB.java
java -cp .;mysql-connector-j-9.6.0.jar TestDB
```

---

## Langkah 8: Clean & Rebuild Project ✅

### Di NetBeans:
```
1. Right-click Project
2. Select "Clean and Build"
3. Wait untuk build complete
4. Check Output window untuk errors
```

**If errors appear:**
```
1. Scroll through output window
2. Find line dengan "SEVERE" atau "ERROR"
3. Screenshot and note the exact error message
```

---

## Langkah 9: Start Server & Test ✅

### Deploy & Run:
```
1. Make sure MySQL is running
2. Right-click Project → Run (F6)
3. Application should open: http://localhost:8080/AcademicAdvisor
4. You should see index.html
```

### Test Login:
```
Matric: A001
Password: admin123

Expected: Login successful → Redirect to dashboard.jsp
Actual: ??? (kalau error, check browser console untuk clues)
```

---

## 📋 Quick Reference - Configuration Summary

### DBConnection.java Settings
```
Host:     localhost
Port:     3306
Database: academic_advisor
Username: root
Password: (empty)
Driver:   com.mysql.cj.jdbc.Driver
```

### Database Details
| Item | Value |
|------|-------|
| Database | academic_advisor |
| Table 1 | user (noMatric, password, roles) |
| Table 2 | appointments (id, title, type, duration, available_slots) |
| Demo Users | 3 (A001, A002, S001) |

### Servlet Mappings
| Servlet | URL | Auth Required |
|---------|-----|---------------|
| LoginServlet | /LoginServlet | No |
| ViewAppointmentServlet | /ViewAppointmentServlet | Yes |
| CreateAppointmentServlet | /CreateAppointmentServlet | Yes (Advisor+) |
| UpdateAppointmentServlet | /UpdateAppointmentServlet | Yes (Advisor+) |
| DeleteAppointmentServlet | /DeleteAppointmentServlet | Yes (Admin) |

---

## 🆘 Emergency Troubleshooting

**If nothing works:**

1. **Screenshot Output Window** → Send to developer
2. **Note exact error message** → Very important!
3. **Check MySQL service** → XAMPP Control Panel
4. **Verify database exists** → phpMyAdmin
5. **Confirm JDBC JAR location** → build/web/WEB-INF/lib/

---

## ✅ Success Indicators

Kalau semua setup betul, should see:

✓ MySQL running (XAMPP green indicator)
✓ Database `academic_advisor` exists
✓ Table `user` ada dengan 3 rows demo data
✓ mysql-connector-j-9.6.0.jar di build output
✓ Project builds tanpa compile errors
✓ Application starts at http://localhost:8080/AcademicAdvisor
✓ Login page loads dengan form
✓ Login dengan A001/admin123 works
✓ Dashboard loads dengan greeting

---

**Jika masih issue, run langkah 1-9 di atas dan screenshot output. Boleh tanya lagi!** 💪
