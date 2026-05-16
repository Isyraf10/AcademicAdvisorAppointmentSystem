# Academic Advisor Appointment System - Database Setup Guide

## 📋 Overview

Your Academic Advisor Appointment System is a comprehensive web-based application for managing:
- **Student appointments** with academic advisors
- **Advisor schedules** and availability
- **Consultation records** and academic progress tracking
- **Role-based access control** (Admin, Advisors, Students)

---

## 📊 Database Schema

### 1. **USER Table** (Authentication & Profiles)
Stores all system users with their roles and profile information.

| Column | Type | Description |
|--------|------|-------------|
| user_id | INT | Primary key |
| noMatric | VARCHAR(50) | Unique matriculation number |
| name | VARCHAR(100) | Full name |
| email | VARCHAR(100) | Email address |
| phone_no | VARCHAR(20) | Contact number |
| password | VARCHAR(256) | Hashed password |
| roles | VARCHAR(20) | **admin**, **advisor**, or **student** |
| department | VARCHAR(100) | Department (advisors only) |
| office_location | VARCHAR(100) | Office location (advisors only) |
| is_active | BOOLEAN | Account status |
| created_at | TIMESTAMP | Account creation |
| last_login | TIMESTAMP | Last login time |

**Roles:**
- **Admin** (A001): System administrator with full access
- **Advisor** (ADV001-ADV004): Academic advisors from different departments
- **Student** (S001-S012): Students from various departments

---

### 2. **SCHEDULE Table** (Advisor Availability)
Stores advisor available time slots for appointments.

| Column | Type | Description |
|--------|------|-------------|
| schedule_id | INT | Primary key |
| advisor_id | INT | Foreign key to user |
| date | DATE | Date of availability |
| start_time | TIME | Start time of slot |
| end_time | TIME | End time of slot |
| status | ENUM | **Available**, **Booked**, or **Unavailable** |
| max_capacity | INT | Maximum appointments in slot |
| current_bookings | INT | Current number of bookings |
| notes | TEXT | Special notes |

**Status:**
- **Available**: Open for student bookings
- **Booked**: Slot is full or partially filled
- **Unavailable**: Advisor blocked this time

---

### 3. **APPOINTMENT Table** (Student Bookings)
Stores student appointment requests and their status.

| Column | Type | Description |
|--------|------|-------------|
| appointment_id | INT | Primary key |
| student_id | INT | Foreign key to student user |
| advisor_id | INT | Foreign key to advisor user |
| schedule_id | INT | Foreign key to schedule (optional) |
| title | VARCHAR(255) | Appointment title |
| description | TEXT | Detailed description |
| appointment_date | DATE | Date of appointment |
| start_time | TIME | Start time |
| end_time | TIME | End time |
| status | ENUM | **Pending**, **Approved**, **Rejected**, **Completed**, **Cancelled** |
| appointment_type | VARCHAR(100) | Type: Course Selection, Academic Planning, Mentoring, etc. |
| duration | INT | Duration in minutes |
| reason | TEXT | Student's reason for appointment |
| rejection_reason | TEXT | Advisor's reason if rejected |

**Status Workflow:**
```
Student requests appointment → Pending
↓
Advisor reviews → Approved or Rejected
↓
Meeting occurs → Completed or Cancelled
```

---

### 4. **RECORD Table** (Consultation Records)
Stores documentation and records from completed appointments.

| Column | Type | Description |
|--------|------|-------------|
| record_id | INT | Primary key |
| appointment_id | INT | Foreign key to appointment |
| student_id | INT | Foreign key to student |
| advisor_id | INT | Foreign key to advisor |
| meeting_date | DATE | Actual meeting date |
| meeting_notes | TEXT | Notes from advisor |
| feedback | TEXT | Student feedback |
| action_items | TEXT | Action items discussed |
| student_status | VARCHAR(100) | Academic status |
| gpa_status | VARCHAR(50) | GPA performance |
| record_status | VARCHAR(50) | **Active** or **Archived** |

---

## 🔑 Database Relationships

```
User (Admin, Advisors, Students)
  ├── Schedule (Advisor Availability)
  ├── Appointment (Student Bookings)
  │   └── Record (Consultation Records)
  └── Record (Academic Progress)
```

**Foreign Keys:**
- `appointment.student_id` → `user.user_id` (Student)
- `appointment.advisor_id` → `user.user_id` (Advisor)
- `appointment.schedule_id` → `schedule.schedule_id`
- `schedule.advisor_id` → `user.user_id`
- `record.appointment_id` → `appointment.appointment_id`
- `record.student_id` → `user.user_id`
- `record.advisor_id` → `user.user_id`

---

## 👥 Test Data Included

### Users (14 Total)
- **1 Admin**: A001 / admin123
- **4 Advisors**: From CS, Business, Engineering, Science departments
- **9 Students**: From various departments

### Schedules (27 Slots)
- Multiple time slots for each advisor
- Various statuses: Available, Booked, Unavailable
- Realistic times: 8 AM - 4 PM

### Appointments (9 Total)
- **3 Pending**: Students awaiting response
- **3 Approved**: Ready to meet
- **2 Completed**: Past meetings with records
- **1 Rejected**: Denied request

### Records (2 Total)
- Detailed meeting notes
- Student feedback
- Action items
- Academic status tracking

---

## 🚀 How to Use This SQL File

### Option 1: Using phpMyAdmin

1. **Open phpMyAdmin**
   ```
   http://localhost/phpmyadmin
   ```

2. **Create Database** (optional, script creates it)
   - The script will create `academic_advisor` database automatically

3. **Import SQL File**
   - Click on database name
   - Go to "Import" tab
   - Select `COMPLETE_DATABASE_SETUP.sql`
   - Click "Go"

4. **Verify Setup**
   ```sql
   USE academic_advisor;
   SELECT COUNT(*) FROM user;           -- Should show 14
   SELECT COUNT(*) FROM schedule;       -- Should show 27
   SELECT COUNT(*) FROM appointment;    -- Should show 9
   SELECT COUNT(*) FROM record;         -- Should show 2
   ```

### Option 2: Using MySQL Command Line

```bash
# Connect to MySQL
mysql -u root -p

# Run the SQL file
source C:\path\to\COMPLETE_DATABASE_SETUP.sql;

# Or in one command
mysql -u root -p < COMPLETE_DATABASE_SETUP.sql
```

### Option 3: Using IDE/NetBeans

1. Open your database connection in NetBeans
2. Right-click on the connection
3. Select "Execute SQL File..."
4. Choose `COMPLETE_DATABASE_SETUP.sql`
5. Review output for success messages

---

## 🔐 Login Credentials for Testing

| Role | Username | Password | Name | Department |
|------|----------|----------|------|-----------|
| **Admin** | A001 | admin123 | Admin User | - |
| **Advisor** | ADV001 | advisor123 | Dr. Ahmad | Computer Science |
| **Advisor** | ADV002 | advisor123 | Prof. Siti | Business Admin |
| **Advisor** | ADV003 | advisor123 | Assoc. Prof. Rajesh | Engineering |
| **Advisor** | ADV004 | advisor123 | Dr. Nur Azizah | Science |
| **Student** | S001 | student123 | Muhammad Fazli | CS |
| **Student** | S002 | student123 | Nur Izzah | CS |
| **Student** | S006 | student123 | Sarah Johnson | Business |
| **Student** | S009 | student123 | Arjun Patel | Engineering |
| **Student** | S011 | student123 | Ahmed Hassan | Science |

---

## 📝 Sample Queries

### View All Upcoming Appointments
```sql
SELECT 
    a.appointment_id,
    s.name AS Student,
    ad.name AS Advisor,
    a.title,
    a.appointment_date,
    TIME_FORMAT(a.start_time, '%H:%i') AS Time,
    a.status
FROM appointment a
JOIN user s ON a.student_id = s.user_id
JOIN user ad ON a.advisor_id = ad.user_id
WHERE a.appointment_date >= CURDATE()
ORDER BY a.appointment_date;
```

### View Advisor Schedule with Bookings
```sql
SELECT 
    u.name AS Advisor,
    sch.date,
    TIME_FORMAT(sch.start_time, '%H:%i') AS Start,
    TIME_FORMAT(sch.end_time, '%H:%i') AS End,
    sch.status,
    CONCAT(sch.current_bookings, '/', sch.max_capacity) AS Bookings
FROM schedule sch
JOIN user u ON sch.advisor_id = u.user_id
ORDER BY sch.date, sch.start_time;
```

### View Student Appointment History
```sql
SELECT 
    a.appointment_id,
    ad.name AS Advisor,
    a.title,
    a.appointment_date,
    a.status,
    a.appointment_type
FROM appointment a
JOIN user ad ON a.advisor_id = ad.user_id
WHERE a.student_id = 6
ORDER BY a.appointment_date DESC;
```

### View Consultation Records for a Student
```sql
SELECT 
    r.record_id,
    a.title AS Appointment,
    r.meeting_notes,
    r.action_items,
    r.student_status,
    r.gpa_status
FROM record r
JOIN appointment a ON r.appointment_id = a.appointment_id
WHERE r.student_id = 6
ORDER BY r.created_at DESC;
```

---

## 🔄 Database Maintenance

### Reset Database
```sql
DROP DATABASE IF EXISTS academic_advisor;
-- Then re-run the COMPLETE_DATABASE_SETUP.sql file
```

### Add New Advisor
```sql
INSERT INTO user (noMatric, name, email, password, roles, department, office_location, is_active)
VALUES ('ADV005', 'Dr. New Advisor', 'new@university.edu', 'password123', 'advisor', 'New Department', 'Building XYZ', TRUE);
```

### Add New Student
```sql
INSERT INTO user (noMatric, name, email, password, roles, is_active)
VALUES ('S013', 'Student Name', 'student@student.edu', 'student123', 'student', TRUE);
```

### Create Schedule Slot
```sql
INSERT INTO schedule (advisor_id, date, start_time, end_time, status, max_capacity, notes)
VALUES (2, '2026-05-25', '09:00:00', '10:00:00', 'Available', 2, 'Regular consultation');
```

---

## ✅ Verification Checklist

After importing the SQL file, verify:

- [ ] Database `academic_advisor` created
- [ ] 4 tables created (user, schedule, appointment, record)
- [ ] 14 users inserted
- [ ] 27 schedule slots created
- [ ] 9 appointments recorded
- [ ] 2 consultation records added
- [ ] Foreign keys working correctly
- [ ] Indexes created for performance
- [ ] Can login with provided credentials

---

## 📚 Additional Features

### Constraints Included
- **Primary Keys**: Each table has unique identifier
- **Foreign Keys**: Maintains referential integrity
- **Check Constraints**: Validates data (e.g., start_time < end_time)
- **Unique Constraints**: noMatric and email are unique
- **Enum Constraints**: Status values are restricted to valid options

### Indexes Created
- Advisor/Date lookups (Schedule)
- Student/Status lookups (Appointment)
- Student/Advisor record queries (Record)
- Date-based queries (Appointment)

### Performance Optimizations
- Composite indexes for common queries
- Proper foreign key relationships
- UTF-8mb4 character encoding for international characters
- InnoDB engine for ACID compliance

---

## 🐛 Troubleshooting

### Error: "Database already exists"
```sql
DROP DATABASE IF EXISTS academic_advisor;
-- Then run the script again
```

### Error: "Foreign key constraint fails"
- Ensure tables are created in order (user → schedule → appointment → record)
- Script creates tables in correct order

### Error: "Duplicate entry"
- This shouldn't happen with fresh database
- If error occurs, drop and recreate database

### Can't login with credentials
- Check user table: `SELECT * FROM user WHERE noMatric = 'A001';`
- Verify password is exactly: `admin123`, `advisor123`, or `student123`
- Check application's password hashing method

---

## 📞 Support

For issues or questions:
1. Check the verification section above
2. Review error messages in detail
3. Ensure MySQL is running
4. Verify file encoding is UTF-8
5. Check application database connection settings

---

**Database Setup Complete! Your system is ready for use.** ✅

