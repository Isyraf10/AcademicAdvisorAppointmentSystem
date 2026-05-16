DROP DATABASE IF EXISTS `academic_advisor`;
CREATE DATABASE `academic_advisor` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `academic_advisor`;
CREATE TABLE `user` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `noMatric` VARCHAR(50) UNIQUE NOT NULL COMMENT 'Student/Advisor matriculation number',
    `name` VARCHAR(100) NOT NULL COMMENT 'Full name',
    `email` VARCHAR(100) NOT NULL UNIQUE COMMENT 'Email address',
    `phone_no` VARCHAR(20) COMMENT 'Phone number',
    `password` VARCHAR(256) NOT NULL COMMENT 'Hashed password (SHA-256)',
    `roles` VARCHAR(20) NOT NULL DEFAULT 'student' COMMENT 'User role: admin, advisor, student',
    `department` VARCHAR(100) COMMENT 'Department (for advisors)',
    `office_location` VARCHAR(100) COMMENT 'Office location (for advisors)',
    `is_active` BOOLEAN DEFAULT TRUE COMMENT 'Account active status',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Account creation date',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update date',
    `last_login` TIMESTAMP NULL COMMENT 'Last login timestamp',
    
    INDEX idx_noMatric (`noMatric`),
    INDEX idx_roles (`roles`),
    INDEX idx_email (`email`),
    CONSTRAINT chk_roles CHECK (`roles` IN ('admin', 'advisor', 'student'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User authentication and profile information';

CREATE TABLE `schedule` (
    `schedule_id` INT AUTO_INCREMENT PRIMARY KEY,
    `advisor_id` INT NOT NULL COMMENT 'Advisor user_id',
    `date` DATE NOT NULL COMMENT 'Date of availability',
    `start_time` TIME NOT NULL COMMENT 'Start time of slot',
    `end_time` TIME NOT NULL COMMENT 'End time of slot',
    `status` ENUM('Available', 'Booked', 'Unavailable') DEFAULT 'Available' COMMENT 'Slot availability status',
    `max_capacity` INT DEFAULT 1 COMMENT 'Maximum appointments allowed in this slot',
    `current_bookings` INT DEFAULT 0 COMMENT 'Current number of bookings',
    `notes` TEXT COMMENT 'Special notes for this slot',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`advisor_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
    INDEX idx_advisor_date (`advisor_id`, `date`),
    INDEX idx_status (`status`),
    INDEX idx_date (`date`),
    CONSTRAINT chk_schedule_time CHECK (`start_time` < `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Advisor availability slots';

CREATE TABLE `appointment` (
    `appointment_id` INT AUTO_INCREMENT PRIMARY KEY,
    `student_id` INT NOT NULL COMMENT 'Student user_id',
    `advisor_id` INT NOT NULL COMMENT 'Advisor user_id',
    `schedule_id` INT COMMENT 'Reference to schedule slot',
    `title` VARCHAR(255) NOT NULL COMMENT 'Appointment title',
    `description` TEXT COMMENT 'Detailed description',
    `appointment_date` DATE NOT NULL COMMENT 'Date of appointment',
    `start_time` TIME NOT NULL COMMENT 'Start time',
    `end_time` TIME NOT NULL COMMENT 'End time',
    `status` ENUM('Pending', 'Approved', 'Rejected', 'Completed', 'Cancelled') DEFAULT 'Pending' COMMENT 'Appointment status',
    `appointment_type` VARCHAR(100) COMMENT 'Type: Academic Planning, Course Selection, Mentoring, etc.',
    `duration` INT COMMENT 'Duration in minutes',
    `reason` TEXT COMMENT 'Student reason for appointment',
    `rejection_reason` TEXT COMMENT 'Advisor reason for rejection',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`student_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`advisor_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`schedule_id`) REFERENCES `schedule`(`schedule_id`) ON DELETE SET NULL,
    INDEX idx_student_status (`student_id`, `status`),
    INDEX idx_advisor_date (`advisor_id`, `appointment_date`),
    INDEX idx_appointment_date (`appointment_date`),
    INDEX idx_status (`status`),
    CONSTRAINT chk_appointment_time CHECK (`start_time` < `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Student appointments with advisors';

CREATE TABLE `record` (
    `record_id` INT AUTO_INCREMENT PRIMARY KEY,
    `appointment_id` INT NOT NULL COMMENT 'Reference to appointment',
    `student_id` INT NOT NULL COMMENT 'Student user_id',
    `advisor_id` INT NOT NULL COMMENT 'Advisor user_id',
    `meeting_date` DATE COMMENT 'Actual meeting date',
    `meeting_notes` TEXT COMMENT 'Meeting summary and notes from advisor',
    `feedback` TEXT COMMENT 'Student feedback on the session',
    `action_items` TEXT COMMENT 'Action items discussed during meeting',
    `student_status` VARCHAR(100) COMMENT 'Academic status: Good Standing, Probation, Graduated, etc.',
    `gpa_status` VARCHAR(50) COMMENT 'GPA status/performance',
    `record_status` VARCHAR(50) DEFAULT 'Active' COMMENT 'Record status: Active, Archived',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`appointment_id`) REFERENCES `appointment`(`appointment_id`) ON DELETE CASCADE,
    FOREIGN KEY (`student_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`advisor_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
    INDEX idx_student_records (`student_id`),
    INDEX idx_advisor_records (`advisor_id`),
    INDEX idx_appointment (`appointment_id`),
    INDEX idx_record_status (`record_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Consultation records and academic progress tracking';

-- ================================================================
-- INSERT TEST DATA - REALISTIC SCENARIOS
-- ================================================================

-- Admin User
INSERT INTO `user` (`noMatric`, `name`, `email`, `phone_no`, `password`, `roles`, `is_active`) VALUES
('A001', 'Admin User', 'admin@ocean.umt.edu.my', '0123456789', '123', 'admin', TRUE);

-- Academic Advisors
INSERT INTO `user` (`noMatric`, `name`, `email`, `phone_no`, `password`, `roles`, `department`, `office_location`, `is_active`) VALUES
('ADV001', 'Dr. Ahmad Advisor', 'ahmad.advisor@ocean.umt.edu.my', '0123456790', 'advisor123', 'advisor', 'Computer Science', 'CS Building, Room 301', TRUE),
('ADV002', 'Prof. Siti Mentor', 'siti.mentor@ocean.umt.edu.my', '0123456791', 'advisor123', 'advisor', 'Business Administration', 'Admin Building, Room 205', TRUE),
('ADV003', 'Assoc. Prof. Rajesh Kumar', 'rajesh.kumar@ocean.umt.edu.my', '0123456792', 'advisor123', 'advisor', 'Engineering', 'ENG Building, Room 501', TRUE),
('ADV004', 'Dr. Nur Azizah', 'nur.azizah@ocean.umt.edu.my', '0123456793', 'advisor123', 'advisor', 'Science', 'Science Building, Room 402', TRUE);

-- Students - Computer Science Department
INSERT INTO `user` (`noMatric`, `name`, `email`, `phone_no`, `password`, `roles`, `is_active`) VALUES
('S75717', 'Muhammad Fazli', 'S75717@ocean.umt.edu.my', '0123456794', 'student123', 'student', TRUE),
('S75718', 'Nur Izzah', 'S75718@ocean.umt.edu.my', '0123456795', 'student123', 'student', TRUE),
('S75719', 'Ali Hassan', 'S75719@ocean.umt.edu.my', '0123456796', 'student123', 'student', TRUE),
('S75720', 'Fatima Abdullah', 'S75720@ocean.umt.edu.my', '0123456797', 'student123', 'student', TRUE),
('S75721', 'Chen Wei', 'S75721@ocean.umt.edu.my', '0123456798', 'student123', 'student', TRUE);

-- Students - Business Administration Department
INSERT INTO `user` (`noMatric`, `name`, `email`, `phone_no`, `password`, `roles`, `is_active`) VALUES
('S75722', 'Sarah Johnson', 'S75722@ocean.umt.edu.my', '0123456799', 'student123', 'student', TRUE),
('S75723', 'Michael Tan', 'S75723@ocean.umt.edu.my', '0123456800', 'student123', 'student', TRUE),
('S75724', 'Emma Watson', 'S75724@ocean.umt.edu.my', '0123456801', 'student123', 'student', TRUE);

-- Students - Engineering Department
INSERT INTO `user` (`noMatric`, `name`, `email`, `phone_no`, `password`, `roles`, `is_active`) VALUES
('S75725', 'Arjun Patel', 'S75725@ocean.edu.my', '0123456802', 'student123', 'student', TRUE),
('S75726', 'Lisa Wong', 'S75726@ocean.edu.my', '0123456803', 'student123', 'student', TRUE);

-- Students - Science Department
INSERT INTO `user` (`noMatric`, `name`, `email`, `phone_no`, `password`, `roles`, `is_active`) VALUES
('S75727', 'Ahmed Hassan', 'S75727@ocean.edu.my', '0123456804', 'student123', 'student', TRUE),
('S75728', 'Sophia Lee', 'S75728@ocean.edu.my', '0123456805', 'student123', 'student', TRUE);

-- Dr. Ahmad's Schedule (CS Advisor) - Multiple time slots for variety
INSERT INTO `schedule` (`advisor_id`, `date`, `start_time`, `end_time`, `status`, `max_capacity`, `current_bookings`, `notes`) VALUES
(2, '2026-05-18', '09:00:00', '10:00:00', 'Available', 2, 0, 'Morning session'),
(2, '2026-05-18', '10:30:00', '11:30:00', 'Booked', 1, 1, 'Only 1 slot available'),
(2, '2026-05-18', '14:00:00', '15:00:00', 'Available', 2, 0, 'Afternoon session'),
(2, '2026-05-19', '09:00:00', '10:00:00', 'Available', 2, 0, 'Tuesday morning'),
(2, '2026-05-19', '15:00:00', '16:00:00', 'Available', 1, 0, 'Late afternoon'),
(2, '2026-05-20', '10:00:00', '11:00:00', 'Available', 2, 0, 'Wednesday 10 AM'),
(2, '2026-05-20', '14:30:00', '15:30:00', 'Booked', 1, 1, 'Reserved slot'),
(2, '2026-05-21', '09:30:00', '10:30:00', 'Available', 2, 0, 'Thursday morning');

-- Prof. Siti's Schedule (Business Admin Advisor) - Different times
INSERT INTO `schedule` (`advisor_id`, `date`, `start_time`, `end_time`, `status`, `max_capacity`, `current_bookings`, `notes`) VALUES
(3, '2026-05-18', '08:00:00', '09:00:00', 'Available', 1, 0, 'Early morning'),
(3, '2026-05-18', '11:00:00', '12:00:00', 'Available', 2, 0, 'Mid-morning'),
(3, '2026-05-18', '13:00:00', '14:00:00', 'Available', 1, 0, 'Early afternoon'),
(3, '2026-05-19', '10:00:00', '11:00:00', 'Booked', 2, 2, 'Full capacity'),
(3, '2026-05-19', '15:30:00', '16:30:00', 'Available', 1, 0, 'Late afternoon'),
(3, '2026-05-20', '09:00:00', '10:00:00', 'Available', 2, 0, 'Wednesday morning'),
(3, '2026-05-21', '11:00:00', '12:00:00', 'Available', 1, 0, 'Thursday session');

-- Assoc. Prof. Rajesh's Schedule (Engineering Advisor)
INSERT INTO `schedule` (`advisor_id`, `date`, `start_time`, `end_time`, `status`, `max_capacity`, `current_bookings`, `notes`) VALUES
(4, '2026-05-18', '08:30:00', '09:30:00', 'Available', 2, 0, 'Early slot'),
(4, '2026-05-18', '13:00:00', '14:00:00', 'Available', 2, 0, 'Afternoon'),
(4, '2026-05-19', '10:00:00', '11:00:00', 'Booked', 1, 1, '1 slot left'),
(4, '2026-05-19', '14:30:00', '15:30:00', 'Available', 2, 0, 'Late afternoon'),
(4, '2026-05-20', '08:00:00', '09:00:00', 'Available', 1, 0, 'Early Wednesday'),
(4, '2026-05-20', '15:00:00', '16:00:00', 'Available', 2, 0, 'Late afternoon'),
(4, '2026-05-21', '09:00:00', '10:00:00', 'Available', 2, 0, 'Thursday morning');

-- Dr. Nur's Schedule (Science Advisor)
INSERT INTO `schedule` (`advisor_id`, `date`, `start_time`, `end_time`, `status`, `max_capacity`, `current_bookings`, `notes`) VALUES
(5, '2026-05-18', '09:00:00', '10:00:00', 'Available', 2, 0, 'Sunday 9 AM'),
(5, '2026-05-18', '14:00:00', '15:00:00', 'Available', 1, 0, 'Sunday afternoon'),
(5, '2026-05-19', '10:30:00', '11:30:00', 'Available', 2, 0, 'Monday session'),
(5, '2026-05-19', '15:00:00', '16:00:00', 'Booked', 2, 2, 'Full'),
(5, '2026-05-20', '09:00:00', '10:00:00', 'Available', 1, 0, 'Wednesday'),
(5, '2026-05-21', '11:00:00', '12:00:00', 'Available', 2, 0, 'Thursday');


-- Pending Appointments (Students requesting meetings)
INSERT INTO `appointment` (`student_id`, `advisor_id`, `schedule_id`, `title`, `description`, `appointment_date`, `start_time`, `end_time`, `status`, `appointment_type`, `duration`, `reason`) VALUES
-- CS Student - Fazli booking with Dr. Ahmad
(6, 2, 1, 'Course Selection for Next Semester', 'Need guidance on selecting CS electives', '2026-05-18', '09:00:00', '10:00:00', 'Pending', 'Course Selection', 60, 'Confused between AI and Graphics courses'),

-- Business Student - Sarah booking with Prof. Siti
(12, 3, 10, 'Academic Performance Review', 'Discuss current GPA and progress', '2026-05-18', '08:00:00', '09:00:00', 'Pending', 'Academic Planning', 60, 'Want to improve my grades'),

-- Engineering Student - Arjun booking with Rajesh
(15, 4, 17, 'Project Consultation', 'Need help with final year project', '2026-05-18', '08:30:00', '09:30:00', 'Pending', 'Academic Planning', 60, 'Struggling with project timeline'),

-- Approved Appointments (Already scheduled)
(7, 2, 2, 'Mid-Semester Review', 'Check progress halfway through semester', '2026-05-18', '10:30:00', '11:30:00', 'Approved', 'Academic Planning', 60, 'Regular check-in'),
(13, 3, 15, 'Internship Discussion', 'Explore internship opportunities', '2026-05-19', '10:00:00', '11:00:00', 'Approved', 'Mentoring', 60, 'Summer internship planning'),
(16, 4, 19, 'Thesis Proposal Review', 'Review thesis proposal for thesis submission', '2026-05-19', '10:00:00', '11:00:00', 'Approved', 'Academic Planning', 60, 'Final thesis check'),

-- Completed Appointments (Past meetings)
(8, 2, NULL, 'Course Prerequisite Review', 'Discussed prerequisite requirements', '2026-05-10', '14:00:00', '15:00:00', 'Completed', 'Course Selection', 60, 'Needed prerequisite clarification'),
(6, 3, NULL, 'Career Guidance Session', 'Discussed career paths in Business', '2026-05-12', '11:00:00', '12:00:00', 'Completed', 'Mentoring', 60, 'Career exploration'),

-- Rejected Appointment
(9, 2, NULL, 'Extra Credit Opportunity', 'Request for extra credit assignment', '2026-05-16', '09:00:00', '10:00:00', 'Rejected', 'Other', 60, 'Extra credit request', 'Policy does not allow mid-semester extra credit'),

-- Cancelled Appointment
(14, 3, NULL, 'Dropped Class Discussion', 'Discuss implications of dropping a course', '2026-05-17', '13:00:00', '14:00:00', 'Cancelled', 'Academic Planning', 60, 'Wanted to drop 1 course');


-- Record for Completed Appointment with Nur Izzah (S002)
INSERT INTO `record` (`appointment_id`, `student_id`, `advisor_id`, `meeting_date`, `meeting_notes`, `feedback`, `action_items`, `student_status`, `gpa_status`, `record_status`) VALUES
(8, 7, 2, '2026-05-10', 
    'Nur Izzah asked about prerequisites for Database Systems (CS 301). Currently taking CS 200, planning to take CS 301 next semester. Prerequisites are met. Discussed importance of understanding SQL basics first.',
    'Very helpful session. Dr. Ahmad explained the course structure clearly and gave me confidence to proceed.',
    'Action 1: Review SQL tutorials before semester starts\nAction 2: Email Dr. Ahmad with final course selection by May 25\nAction 3: Join CS 301 study group',
    'Good Standing', 'GPA 3.5 - Excellent', 'Active'),

-- Record for Career Guidance with Muhammad Fazli (S001)
(9, 6, 3, '2026-05-12', 
    'Muhammad Fazli meeting regarding career prospects in business. Discussed MBA programs, corporate career tracks, and entrepreneurship. Showed interest in management consulting. Advised to take leadership courses.',
    'Insightful session. Prof. Siti helped me see different career paths I hadn\'t considered.',
    'Action 1: Attend management seminar on May 20\nAction 2: Setup informational interview with alumni\nAction 3: Take Org Leadership course next term',
    'Good Standing', 'GPA 3.7 - Excellent', 'Active');

-- Show Summary of Data Inserted
SELECT '=== USER DATA SUMMARY ===' AS Info;
SELECT 'Total Users:' AS Info, COUNT(*) AS Count FROM `user`;
SELECT `roles`, COUNT(*) AS Count FROM `user` GROUP BY `roles`;

SELECT '=== SCHEDULE DATA SUMMARY ===' AS Info;
SELECT 'Total Schedule Slots:' AS Info, COUNT(*) AS Count FROM `schedule`;
SELECT `status`, COUNT(*) AS Count FROM `schedule` GROUP BY `status`;

SELECT '=== APPOINTMENT DATA SUMMARY ===' AS Info;
SELECT 'Total Appointments:' AS Info, COUNT(*) AS Count FROM `appointment`;
SELECT `status`, COUNT(*) AS Count FROM `appointment` GROUP BY `status`;

SELECT '=== RECORD DATA SUMMARY ===' AS Info;
SELECT 'Total Records:' AS Info, COUNT(*) AS Count FROM `record`;

-- Sample Query: Show upcoming appointments
SELECT 
    a.appointment_id,
    s.name AS Student,
    ad.name AS Advisor,
    a.title,
    a.appointment_date,
    TIME_FORMAT(a.start_time, '%H:%i') AS Time,
    a.status,
    a.appointment_type
FROM `appointment` a
JOIN `user` s ON a.student_id = s.user_id
JOIN `user` ad ON a.advisor_id = ad.user_id
ORDER BY a.appointment_date, a.start_time;

-- Sample Query: Show advisor schedules
SELECT 
    sch.schedule_id,
    u.name AS Advisor,
    sch.date,
    TIME_FORMAT(sch.start_time, '%H:%i') AS Start,
    TIME_FORMAT(sch.end_time, '%H:%i') AS End,
    sch.status,
    CONCAT(sch.current_bookings, '/', sch.max_capacity) AS Bookings
FROM `schedule` sch
JOIN `user` u ON sch.advisor_id = u.user_id
ORDER BY sch.date, sch.start_time;
