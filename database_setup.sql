-- =====================================================================
-- Smart Collective Bunk System (SCBS) / Banking Consensus System
-- Advanced Java Technology (AJT - 01CE0308) - Task 4 Database Scripts
-- Student Name: Vaibhav Rabadiya (Enrollment No: 92610120040)
-- Department of Computer Engineering, Marwadi University
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1. Table: classes (Class Sections & Student Quotas)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS classes (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    semester VARCHAR(20) NOT NULL,
    total_students INT DEFAULT 60
);

-- ---------------------------------------------------------------------
-- 2. Table: students (User Accounts, Roles, Biometric Embeddings)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS students (
    id VARCHAR(36) PRIMARY KEY,
    roll_number VARCHAR(30) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(128) NOT NULL,
    class_id VARCHAR(36),
    role VARCHAR(20) DEFAULT 'STUDENT',
    approval_status VARCHAR(20) DEFAULT 'PENDING',
    bunk_credits INT DEFAULT 100,
    reliability_score DOUBLE PRECISION DEFAULT 100.0,
    face_descriptor TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE SET NULL
);

-- ---------------------------------------------------------------------
-- 3. Table: timetable_slots (Weekly Schedule and Course Matrix)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS timetable_slots (
    id VARCHAR(36) PRIMARY KEY,
    class_id VARCHAR(36) NOT NULL,
    day_of_week VARCHAR(15) NOT NULL,
    start_time VARCHAR(10) NOT NULL,
    end_time VARCHAR(10) NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    faculty_name VARCHAR(100) NOT NULL,
    room_number VARCHAR(30),
    CONSTRAINT fk_slot_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 4. Table: bunk_proposals (Democratic Consensus Ballots)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bunk_proposals (
    id VARCHAR(36) PRIMARY KEY,
    class_id VARCHAR(36) NOT NULL,
    slot_id VARCHAR(36) NOT NULL,
    creator_id VARCHAR(36) NOT NULL,
    bunk_date VARCHAR(15) NOT NULL,
    reason TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_prop_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
    CONSTRAINT fk_prop_slot FOREIGN KEY (slot_id) REFERENCES timetable_slots(id) ON DELETE CASCADE,
    CONSTRAINT fk_prop_creator FOREIGN KEY (creator_id) REFERENCES students(id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- 5. Table: bunk_votes (Secure Ballot Casting & Biometric Audit)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bunk_votes (
    id VARCHAR(36) PRIMARY KEY,
    proposal_id VARCHAR(36) NOT NULL,
    student_id VARCHAR(36) NOT NULL,
    choice BOOLEAN NOT NULL,
    voted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    face_verified BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_vote_prop FOREIGN KEY (proposal_id) REFERENCES bunk_proposals(id) ON DELETE CASCADE,
    CONSTRAINT fk_vote_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT uq_vote_voter UNIQUE (proposal_id, student_id)
);

-- =====================================================================
-- INITIAL DATABASE SEED DATA (Test Credentials & Default Slots)
-- =====================================================================

-- Classes Seed
INSERT INTO classes (id, name, semester, total_students) VALUES 
('class-cse-3a', 'CSE-3A', '5th Semester', 55),
('class-cse-3b', 'CSE-3B', '5th Semester', 60),
('class-it-2a',  'IT-2A',  '3rd Semester', 50);

-- Timetable Slots Seed (CSE-3A Weekly Schedule)
INSERT INTO timetable_slots (id, class_id, day_of_week, start_time, end_time, subject_name, faculty_name, room_number) VALUES
('slot-1', 'class-cse-3a', 'MONDAY', '09:00', '10:00', 'Operating Systems', 'Dr. Alan Turing', 'Lab 301'),
('slot-2', 'class-cse-3a', 'MONDAY', '10:00', '11:00', 'Database Management Systems', 'Prof. Edgar Codd', 'Hall 102'),
('slot-3', 'class-cse-3a', 'MONDAY', '11:15', '12:15', 'Computer Networks', 'Dr. Vint Cerf', 'Hall 102'),
('slot-4', 'class-cse-3a', 'MONDAY', '13:00', '14:00', 'Software Engineering', 'Prof. Margaret Hamilton', 'Hall 205'),
('slot-5', 'class-cse-3a', 'TUESDAY', '09:00', '10:00', 'Artificial Intelligence', 'Dr. John McCarthy', 'Lab 402'),
('slot-6', 'class-cse-3a', 'TUESDAY', '10:00', '11:00', 'Operating Systems', 'Dr. Alan Turing', 'Hall 102'),
('slot-7', 'class-cse-3a', 'TUESDAY', '11:15', '12:15', 'Cloud Computing', 'Prof. Werner Vogels', 'Lab 301'),
('slot-8', 'class-cse-3a', 'TUESDAY', '13:00', '14:00', 'Database Management Systems', 'Prof. Edgar Codd', 'Hall 102'),
('slot-9', 'class-cse-3a', 'WEDNESDAY', '09:00', '10:00', 'Computer Networks', 'Dr. Vint Cerf', 'Hall 102'),
('slot-10', 'class-cse-3a', 'WEDNESDAY', '10:00', '11:00', 'Software Engineering', 'Prof. Margaret Hamilton', 'Hall 205'),
('slot-11', 'class-cse-3a', 'WEDNESDAY', '11:15', '13:15', 'Web Tech & Full Stack Lab', 'Prof. Tim Berners-Lee', 'Lab 501');

-- Default Passwords (SHA-256 Hashed):
-- 'admin123'   -> 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- 'student123' -> 01930d63c41389d47d173e14f861556a00d72f39e5e30b45db4cd53c39373e86

-- Students & Admin Users Seed
INSERT INTO students (id, roll_number, full_name, email, password_hash, class_id, role, approval_status, bunk_credits, reliability_score, face_descriptor) VALUES
('admin-01', 'ADMIN-001', 'System Administrator', 'admin@scbs.edu', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'class-cse-3a', 'ADMIN', 'APPROVED', 500, 100.0, '[0.12, -0.45, 0.88, 0.32, -0.19, 0.74, -0.56, 0.23, 0.65, -0.31]'),
('student-01', 'CSE-2024-001', 'Alex Vance', 'alex@scbs.edu', '01930d63c41389d47d173e14f861556a00d72f39e5e30b45db4cd53c39373e86', 'class-cse-3a', 'STUDENT', 'APPROVED', 120, 96.5, '[0.12, -0.45, 0.88, 0.32, -0.19, 0.74, -0.56, 0.23, 0.65, -0.31]'),
('student-02', 'CSE-2024-002', 'Sarah Jenkins', 'sarah@scbs.edu', '01930d63c41389d47d173e14f861556a00d72f39e5e30b45db4cd53c39373e86', 'class-cse-3a', 'STUDENT', 'APPROVED', 95, 92.0, '[0.12, -0.45, 0.88, 0.32, -0.19, 0.74, -0.56, 0.23, 0.65, -0.31]'),
('student-03', 'CSE-2024-003', 'Jordan Lee', 'jordan@scbs.edu', '01930d63c41389d47d173e14f861556a00d72f39e5e30b45db4cd53c39373e86', 'class-cse-3a', 'STUDENT', 'APPROVED', 85, 88.5, NULL),
('student-04', 'CSE-2024-004', 'Liam Patel', 'liam@scbs.edu', '01930d63c41389d47d173e14f861556a00d72f39e5e30b45db4cd53c39373e86', 'class-cse-3a', 'STUDENT', 'PENDING', 100, 100.0, NULL);

-- Live Active Proposal Seed
INSERT INTO bunk_proposals (id, class_id, slot_id, creator_id, bunk_date, reason, status, expires_at) VALUES
('prop-demo-01', 'class-cse-3a', 'slot-1', 'student-01', CURRENT_DATE, 'Upcoming National Hackathon preparation & sprint session.', 'ACTIVE', CURRENT_TIMESTAMP + INTERVAL '1 DAY');

-- Live Votes Seed
INSERT INTO bunk_votes (id, proposal_id, student_id, choice, face_verified) VALUES
('vote-01', 'prop-demo-01', 'student-01', TRUE, TRUE),
('vote-02', 'prop-demo-01', 'student-02', TRUE, TRUE);
