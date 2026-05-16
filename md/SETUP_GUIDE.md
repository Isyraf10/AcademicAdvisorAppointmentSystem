# Academic Advisor Appointment System - Complete Setup Guide

## 🎯 Project Overview

The **Academic Advisor Appointment System** is a professional web-based application built with:
- **Architecture**: MVC (Model-View-Controller)
- **Frontend**: JSP, HTML, CSS, JavaScript
- **Backend**: Java Servlets
- **Database**: MySQL
- **Server**: Apache Tomcat

The system features three main modules:
1. **Manage Appointment** - CRUD operations for student appointment requests
2. **Manage Schedule** - CRUD operations for advisor consultation schedules  
3. **Manage Record** - CRUD operations for consultation records and academic tracking

## 📋 Prerequisites

Before starting, ensure you have installed:
- ✅ Java JDK 8 or higher
- ✅ Apache NetBeans IDE (or similar Java IDE)
- ✅ Apache Tomcat Server (included with NetBeans)
- ✅ MySQL Server 5.7 or higher
- ✅ XAMPP (for easy MySQL management) - Optional but recommended

## 🔧 Installation Steps

### Step 1: Database Setup

#### 1a. Create Database
```sql
CREATE DATABASE academic_advisor;
```

#### 1b. Run Migration Scripts

Execute the following SQL migration scripts in order:

**File:** `database/migrations/001_create_users.sql`
```sql
USE academic_advisor;
-- Creates users table
```

**File:** `database/migrations/002_create_complete_schema.sql`
```sql
-- Creates schedule, appointment, and record tables
-- Inserts demo data
```

**To execute in phpMyAdmin:**
1. Open phpMyAdmin: `http://localhost/phpmyadmin`
2. Create database `academic_advisor`
3. Go to SQL tab
4. Copy and paste content from `002_create_complete_schema.sql`
5. Click Execute

#### 1c. Verify Database
```sql
USE academic_advisor;
SHOW TABLES;
SELECT COUNT(*) FROM user;  -- Should return 6 (demo users)
```

### Step 2: Project Setup in NetBeans

1. Open NetBeans IDE
2. File → Open Project
3. Navigate to: `AcademicAdvisor - DB`
4. Click Open Project
5. Wait for project indexing to complete

### Step 3: Configure Database Connection

File: `src/java/com/lab/util/DBConnection.java`

```java
private static final String JDBC_URL = "jdbc:mysql://localhost:3306/academic_advisor";
private static final String JDBC_USERNAME = "root";
private static final String JDBC_PASSWORD = "";  // XAMPP default
private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
```

**Note:** If your MySQL password is different, update accordingly.

### Step 4: Add MySQL JDBC Driver

1. Right-click Project → Properties
2. Libraries → Add Library
3. Search for "MySQL" or "Connector"
4. Add `mysql-connector-j-9.6.0.jar`
5. If not found, download from: `https://dev.mysql.com/downloads/connector/j/`

### Step 5: Build Project

1. Right-click Project → Clean and Build
2. Wait for build to complete
3. Check Output window - should show "BUILD SUCCESSFUL"

### Step 6: Configure Tomcat Server

1. Tools → Options → Java → Build, Compile
2. Servers → Add → Apache Tomcat
3. Set Tomcat Home directory
4. Click OK

### Step 7: Deploy & Run

1. Right-click Project
2. Select "Run" (F6)
3. Wait for browser to open
4. You should see: `http://localhost:8080/AcademicAdvisor`

## 🧪 Testing the System

### Demo Credentials

| Username | Password | Role | Full Name |
|----------|----------|------|-----------|
| A001 | admin123 | admin | Admin User |
| ADV001 | advisor123 | advisor | Dr. Ahmad Advisor |
| ADV002 | advisor123 | advisor | Prof. Siti Mentor |
| S001 | student123 | student | Muhammad Fazli |
| S002 | student123 | student | Nur Izzah |
| S003 | student123 | student | Ali Hassan |

### Test Scenarios

#### 1. Admin Login
- Login with: **A001 / admin123**
- Expected: Access all modules
- Actions: View all appointments, schedules, records

#### 2. Advisor Login
- Login with: **ADV001 / advisor123**
- Expected: Access schedule and appointment modules
- Actions:
  - Create schedule slots (⏰ Manage Schedule)
  - Review appointment requests (📅 My Appointments)
  - Create consultation records (📝 Consultation Records)

#### 3. Student Login
- Login with: **S001 / student123**
- Expected: Access appointment and record modules
- Actions:
  - View appointments (📅 My Appointments)
  - View consultation records (📋 My Progress)

## 📁 Project Structure

```
AcademicAdvisor - DB/
├── src/java/com/lab/
│   ├── model/                    # Data Models
│   │   ├── User.java
│   │   ├── Appointment.java
│   │   ├── Schedule.java
│   │   ├── Record.java
│   ├── dao/                      # Database Access Objects
│   │   ├── UserDAO.java
│   │   ├── AppointmentDAO.java
│   │   ├── ScheduleDAO.java
│   │   ├── RecordDAO.java
│   ├── controller/               # Servlet Controllers
│   │   ├── LoginServlet.java
│   │   ├── CreateAppointmentServlet.java
│   │   ├── ListAppointmentsServlet.java
│   │   ├── ManageScheduleServlet.java
│   │   ├── ManageRecordServlet.java
│   ├── util/
│   │   ├── DBConnection.java
│   │   ├── UserRole.java
├── web/                          # JSP & Static Files
│   ├── index.html               # Homepage
│   ├── login.jsp                # Login page
│   ├── logout.jsp               # Logout handler
│   ├── dashboard.jsp            # User dashboard
│   ├── WEB-INF/
│   │   ├── web.xml             # Web configuration
├── database/
│   ├── migrations/              # SQL scripts
│   │   ├── 001_create_users.sql
│   │   ├── 002_create_complete_schema.sql
│   ├── seeds/                   # Demo data
├── build/                        # Compiled files
├── nbproject/                    # NetBeans configuration
```

## 🔍 Key Features

### 1. Manage Appointment Module
- **Create**: Students book appointments with advisors
- **Read**: View appointment history and status
- **Update**: Change appointment details or status
- **Delete**: Cancel appointments
- **Status**: Pending, Approved, Rejected, Completed, Cancelled

### 2. Manage Schedule Module
- **Create**: Advisors create available time slots
- **Read**: View all scheduled slots
- **Update**: Mark slots as busy or unavailable
- **Delete**: Remove slots
- **Status**: Available, Booked, Unavailable

### 3. Manage Record Module
- **Create**: Create consultation records after meetings
- **Read**: View meeting notes and progress
- **Update**: Add feedback and update student status
- **Delete**: Remove old or error records
- **Fields**: Meeting notes, feedback, action items, student status

## 🛡️ Security Features

✅ **Session Management**: 30-minute timeout
✅ **Role-Based Access**: Different permissions for each user type
✅ **Input Validation**: Server-side & client-side
✅ **SQL Injection Prevention**: PreparedStatements used throughout
✅ **Password Hashing**: SHA-256 with Base64 encoding
✅ **Authentication**: Mandatory login for all protected pages

## 🐛 Common Issues & Solutions

### Issue 1: ClassNotFoundException for MySQL Driver
**Solution:**
```
- Right-click Project → Properties
- Libraries → Add Library
- Find and add mysql-connector-j JAR
- Rebuild project
```

### Issue 2: Database Connection Failed
**Solution:**
```
- Check MySQL is running in XAMPP
- Verify database 'academic_advisor' exists
- Check credentials in DBConnection.java
- Test with: mysql -u root -p academic_advisor
```

### Issue 3: Port 8080 Already in Use
**Solution:**
```
- Close other Tomcat instances
- Or change port in Tomcat configuration
- Tools → Options → Servers
```

### Issue 4: 404 Not Found Error
**Solution:**
```
- Ensure project is deployed to Tomcat
- Check build folder has all files
- Refresh browser or clear cache
- Restart Tomcat server
```

## 📊 Database Schema

### Users Table
- `user_id` - Primary key
- `noMatric` - Student/Advisor ID (unique)
- `password` - Plain text (for demo)
- `roles` - admin, advisor, student
- `name` - User's full name
- `email` - Email address
- `is_active` - Account status

### Appointment Table
- `appointment_id` - Primary key
- `student_id` - Foreign key to user
- `advisor_id` - Foreign key to user
- `schedule_id` - Foreign key to schedule
- `title`, `description` - Appointment details
- `appointment_date`, `start_time`, `end_time` - When
- `status` - Pending, Approved, Rejected, Completed, Cancelled
- `appointment_type` - Academic Planning, Course Selection, etc.

### Schedule Table
- `schedule_id` - Primary key
- `advisor_id` - Foreign key to user
- `date`, `start_time`, `end_time` - When slot is available
- `status` - Available, Booked, Unavailable
- `max_capacity`, `current_bookings` - Slot tracking

### Record Table
- `record_id` - Primary key
- `appointment_id` - Foreign key to appointment
- `student_id`, `advisor_id` - Foreign keys
- `meeting_notes` - What was discussed
- `feedback` - Student's feedback
- `action_items` - Follow-up items
- `student_status` - Good Standing, On Probation, Graduated

## 🚀 Running the Application

### Quick Start (After Setup Complete)
```
1. Press F6 (or Right-click Project → Run)
2. Wait for browser to open
3. You'll see homepage at http://localhost:8080/AcademicAdvisor
4. Click "Login" or use demo credentials
5. Explore the system!
```

### Manual Tomcat Start
```
1. Tools → Servers → Start
2. Navigate to: http://localhost:8080/AcademicAdvisor
```

## 📚 User Guides

### For Students
1. Login with your student ID and password
2. Go to "My Appointments" to view/book consultations
3. Check "My Progress" for consultation records
4. View meeting notes from advisors

### For Advisors
1. Login with your advisor ID and password
2. Go to "Manage Schedule" to create available slots
3. Check "My Appointments" for student requests
4. Create "Consultation Records" after meetings
5. Track student progress over time

### For Administrators
1. Login with admin credentials
2. View and manage all system appointments
3. Monitor all advisor schedules
4. Access all consultation records
5. Oversee system-wide operations

## 📝 Notes

- **Demo Data**: System comes pre-loaded with 6 demo users
- **UI Design**: Beginner-friendly with clear navigation
- **Professional Backend**: Enterprise-grade Java code
- **Responsive Design**: Works on desktop and tablet
- **Error Handling**: Comprehensive error messages and validation

## 🔄 Troubleshooting Checklist

- [ ] MySQL running in XAMPP?
- [ ] Database 'academic_advisor' created?
- [ ] 002_create_complete_schema.sql executed?
- [ ] MySQL JDBC driver added to project?
- [ ] Project cleaned and built successfully?
- [ ] Tomcat started?
- [ ] Can access http://localhost:8080/AcademicAdvisor?
- [ ] Can login with demo credentials?

## 📞 Support

If you encounter issues:
1. Check the Troubleshooting section above
2. Review the server logs in NetBeans Output window
3. Verify database connection with phpMyAdmin
4. Check file paths and permissions
5. Ensure all ports are available

---

**Version:** 1.0  
**Last Updated:** May 10, 2026  
**Built with:** Java, JSP, MySQL, Apache Tomcat
