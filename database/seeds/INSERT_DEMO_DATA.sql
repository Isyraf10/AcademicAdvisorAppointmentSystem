-- Academic Advisor Appointment System
-- Demo Data Insert Script for Local Database
-- Database: academic_advisor
-- Table: user, appointments

-- ========== INSERT DEMO USERS ==========
-- These credentials match what you see in the login.jsp demo credentials

INSERT INTO user (noMatric, password, roles) VALUES
('A001', 'admin123', 'admin'),
('A002', 'advisor123', 'advisor'),
('S001', 'student123', 'student');

-- Verify insertion
SELECT * FROM user;

-- ========== INSERT SAMPLE APPOINTMENTS ==========
-- If you have an appointments table, uncomment below:

-- INSERT INTO appointments (title, type, duration, available_slots) VALUES
-- ('Initial Academic Planning Session', 'Academic Planning', 60, 10),
-- ('Course Selection Consultation', 'Course Selection', 45, 15),
-- ('Career Guidance Mentoring', 'Mentoring', 30, 8),
-- ('General Academic Advising', 'Academic Planning', 45, 12),
-- ('Degree Audit Review', 'Course Selection', 30, 6);

-- Verify appointments insertion
-- SELECT * FROM appointments;
