# ⚡ QUICK START CHECKLIST (5 Minutes)

## ✅ Pre-Flight Checklist

Before running the application, confirm these 5 things:

### 1. MySQL Running 🟢
- [ ] Open XAMPP Control Panel
- [ ] MySQL indicator = Green/Running
- [ ] If red, click "Start" button

### 2. Database Created 💾
- [ ] Open phpMyAdmin (localhost/phpmyadmin)
- [ ] Database `academic_advisor` exists
- [ ] Table `user` exists with columns: noMatric, password, roles
- [ ] Run: `SELECT COUNT(*) FROM user;` → Should return 3

### 3. Demo Data Inserted 👥
- [ ] Run this in phpMyAdmin SQL tab:
```sql
SELECT * FROM user;
```
- [ ] Confirm 3 rows:
  - A001 / admin123 / admin
  - A002 / advisor123 / advisor
  - S001 / student123 / student

### 4. NetBeans Build OK ✓
- [ ] Right-click Project → Clean and Build
- [ ] No RED errors in Output window
- [ ] Build finishes with "BUILD SUCCESSFUL"

### 5. MySQL JDBC Driver Present 📦
- [ ] File exists:
  ```
  build/web/WEB-INF/lib/mysql-connector-j-9.6.0.jar
  ```
- [ ] If not found, add via:
  - Right-click Project → Properties → Libraries → Add Library

---

## 🚀 Run Application

```
1. Right-click Project → Run (F6)
2. Wait 5-10 seconds
3. Browser opens: http://localhost:8080/AcademicAdvisor
4. You should see index.html homepage
```

---

## 🧪 Test Login

**Try these credentials:**

| Matric | Password | Expected Role |
|--------|----------|---------------|
| A001 | admin123 | admin |
| A002 | advisor123 | advisor |
| S001 | student123 | student |

**Success = Dashboard loads with greeting**

---

## 🐛 If Error Occurs

### Check Output Window (Bottom Tab)
```
Window menu → Output → Output (Alt+Shift+O)
Click "Apache Tomcat" tab to see error details
```

### Look for these patterns:

#### ❌ ClassNotFoundException
```
java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver
```
**Solution:** Add MySQL JDBC library (Step 4 above)

#### ❌ Access Denied
```
Access denied for user 'root'@'localhost'
```
**Solution:** Check MySQL password in DBConnection.java

#### ❌ Communications Link Failure
```
Communications link failure
```
**Solution:** Start MySQL in XAMPP

#### ❌ Unknown Database
```
Unknown database 'academic_advisor'
```
**Solution:** Create database in phpMyAdmin

#### ❌ Table Doesn't Exist
```
Table 'academic_advisor.user' doesn't exist
```
**Solution:** Create table, insert demo data

---

## 📞 Quick Contact Points

| Component | Expected | Actual | Status |
|-----------|----------|--------|--------|
| MySQL Server | localhost:3306 | ☐ | ☐ |
| Database | academic_advisor | ☐ | ☐ |
| Table | user | ☐ | ☐ |
| Demo Users | 3 rows | ☐ | ☐ |
| JDBC Driver | mysql-connector-j-9.6.0.jar | ☐ | ☐ |
| Web Server | Apache Tomcat | ☐ | ☐ |
| Port | 8080 | ☐ | ☐ |
| App URL | http://localhost:8080/AcademicAdvisor | ☐ | ☐ |

---

## 🎯 Success Path

```
MySQL Running ✓
    ↓
Database Exists ✓
    ↓
Demo Data Loaded ✓
    ↓
Build Successful ✓
    ↓
JDBC Driver Present ✓
    ↓
Application Runs ✓
    ↓
Login Works ✓
    ↓
Dashboard Loads ✓ ← YOU ARE HERE!
```

---

**Reference Guides Available:**
- `DATABASE_SETUP_GUIDE.md` - Detailed setup with SQL
- `TROUBLESHOOTING_GUIDE.md` - In-depth error diagnosis
- `webroot/BUILD_INSTRUCTIONS.md` - Full build guide

---

**Next Step:** Follow checklist above, then try login! 🚀
