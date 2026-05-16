-- Academic Advisor Appointment System - Complete Schema
-- Date: May 10, 2026
-- Purpose: Complete database schema for the appointment system

USE academic_advisor;

-- ==========================================
-- 1. USER TABLE (Already exists - for reference)
-- ==========================================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `user_id` INT AUTO_INCREMENT PRIMARY KEY,
    `noMatric` VARCHAR(50) UNIQUE NOT NULL COMMENT 'Student/Advisor matriculation number',
    `password` VARCHAR(256) NOT NULL COMMENT 'Plain text password (for demo)',
    `roles` VARCHAR(20) NOT NULL DEFAULT 'student' COMMENT 'admin, advisor, student',
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100),
    `phone_no` VARCHAR(20),
    `is_active` BOOLEAN DEFAULT TRUE,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `last_login` TIMESTAMP NULL,
    INDEX idx_noMatric (`noMatric`),
    INDEX idx_roles (`roles`),
    CONSTRAINT chk_roles CHECK (`roles` IN ('admin', 'advisor', 'student'))
);

-- ==========================================
-- 2. SCHEDULE TABLE (Advisor availability)
-- ==========================================
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule` (
    `schedule_id` INT AUTO_INCREMENT PRIMARY KEY,
    `advisor_id` INT NOT NULL COMMENT 'Reference to user (advisor)',
    `date` DATE NOT NULL COMMENT 'Scheduled date',
    `start_time` TIME NOT NULL COMMENT 'Start time',
    `end_time` TIME NOT NULL COMMENT 'End time',
    `status` ENUM('Available', 'Booked', 'Unavailable') DEFAULT 'Available',
    `max_capacity` INT DEFAULT 1,
    `current_bookings` INT DEFAULT 0,
    `notes` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`advisor_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
    INDEX idx_advisor_date (`advisor_id`, `date`),
    INDEX idx_status (`status`),
    CONSTRAINT chk_time CHECK (`start_time` < `end_time`)
);

-- ==========================================
-- 3. APPOINTMENT TABLE (Student appointment requests)
-- ==========================================
DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment` (
    `appointment_id` INT AUTO_INCREMENT PRIMARY KEY,
    `student_id` INT NOT NULL COMMENT 'Reference to user (student)',
    `advisor_id` INT NOT NULL COMMENT 'Reference to user (advisor)',
    `schedule_id` INT COMMENT 'Reference to schedule slot',
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `appointment_date` DATE NOT NULL,
    `start_time` TIME NOT NULL,
    `end_time` TIME NOT NULL,
    `status` ENUM('Pending', 'Approved', 'Rejected', 'Completed', 'Cancelled') DEFAULT 'Pending',
    `appointment_type` VARCHAR(100) COMMENT 'Academic Planning, Course Selection, etc.',
    `duration` INT COMMENT 'Duration in minutes',
    `reason` TEXT COMMENT 'Reason for appointment',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`student_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`advisor_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`schedule_id`) REFERENCES `schedule`(`schedule_id`) ON DELETE SET NULL,
    INDEX idx_student_status (`student_id`, `status`),
    INDEX idx_advisor_date (`advisor_id`, `appointment_date`),
    INDEX idx_appointment_date (`appointment_date`),
    CONSTRAINT chk_appointment_time CHECK (`start_time` < `end_time`)
);

-- ==========================================
-- 4. RECORD TABLE (Post-consultation documentation)
-- ==========================================
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
    `record_id` INT AUTO_INCREMENT PRIMARY KEY,
    `appointment_id` INT NOT NULL COMMENT 'Reference to appointment',
    `student_id` INT NOT NULL COMMENT 'Reference to user (student)',
    `advisor_id` INT NOT NULL COMMENT 'Reference to user (advisor)',
    `meeting_notes` TEXT COMMENT 'Meeting summary/notes from advisor',
    `feedback` TEXT COMMENT 'Student feedback on the session',
    `action_items` TEXT COMMENT 'Action items from the meeting',
    `student_status` VARCHAR(100) COMMENT 'e.g., Graduated, Good Standing, On Probation',
    `record_status` VARCHAR(50) DEFAULT 'Active',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`appointment_id`) REFERENCES `appointment`(`appointment_id`) ON DELETE CASCADE,
    FOREIGN KEY (`student_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
    FOREIGN KEY (`advisor_id`) REFERENCES `user`(`user_id`) ON DELETE CASCADE,
    INDEX idx_student_records (`student_id`),
    INDEX idx_advisor_records (`advisor_id`),
    INDEX idx_appointment (`appointment_id`)
);

-- ==========================================
-- 5. DEMO DATA
-- ==========================================

-- Insert users
INSERT INTO `user` (`noMatric`, `password`, `roles`, `name`, `email`, `phone_no`) VALUES
('A001', 'admin123', 'admin', 'Admin User', 'admin@university.edu', '0123456789'),
('ADV001', 'advisor123', 'advisor', 'Dr. Ahmad Advisor', 'ahmad@university.edu', '0123456790'),
('ADV002', 'advisor123', 'advisor', 'Prof. Siti Mentor', 'siti@university.edu', '0123456791'),
('S001', 'student123', 'student', 'Muhammad Fazli', 'fazli@student.edu', '0123456792'),
('S002', 'student123', 'student', 'Nur Izzah', 'izzah@student.edu', '0123456793'),
('S003', 'student123', 'student', 'Ali Hassan', 'ali@student.edu', '0123456794');

-- Verify tables created
SHOW TABLES;
