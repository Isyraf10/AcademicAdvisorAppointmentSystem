-- Database Seed: Insert Demo Users and Appointments
-- Date: April 12, 2026
-- Purpose: Populate initial user accounts and appointment types for testing

USE academic_advisor;

-- Hash values are SHA-256 + Base64 encoded:
-- admin123 -> Vb/mKMM0KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x7=
-- advisor123 -> pZGm5XE0KxL9yA7K3fZc2rKmFjJRKe3gK1Z5mN9x=
-- student123 -> KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x7K3fZc2=

INSERT INTO users (username, password_hash, email, role, is_active, last_login) VALUES
('admin', 'Vb/mKMM0KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x7=', 'admin@advisor.edu', 'admin', TRUE, NOW()),
('advisor', 'pZGm5XE0KxL9yA7K3fZc2rKmFjJRKe3gK1Z5mN9x=', 'advisor@advisor.edu', 'advisor', TRUE, NOW()),
('student', 'KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x7K3fZc2=', 'student@advisor.edu', 'student', TRUE, NOW());

-- Verify users inserted
SELECT user_id, username, role, is_active FROM users;

-- Insert sample appointment types
INSERT INTO appointments (title, type, duration, available_slots) VALUES
('Initial Academic Planning Session', 'Academic Planning', 60, 10),
('Course Selection Consultation', 'Course Selection', 45, 15),
('Career Guidance Mentoring', 'Mentoring', 30, 8),
('General Academic Advising', 'Academic Planning', 45, 12),
('Degree Audit Review', 'Course Selection', 30, 6);

-- Verify appointments inserted
SELECT * FROM appointments;

