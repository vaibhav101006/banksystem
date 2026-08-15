package com.scbs.dao;

import com.scbs.util.PasswordUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class DBConnectionManager {

    private static final String DB_DRIVER_H2 = "org.h2.Driver";
    private static final String DB_DRIVER_SQLITE = "org.sqlite.JDBC";
    
    // Default to embedded H2 file database with AUTO_SERVER=TRUE for multi-process safety
    private static String dbUrl = "jdbc:h2:./scbs_db;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1;MODE=PostgreSQL;DATABASE_TO_UPPER=FALSE";
    private static String dbUser = "sa";
    private static String dbPassword = "";
    private static boolean initialized = false;

    public static synchronized void init(String url, String user, String password) {
        if (url != null && !url.trim().isEmpty()) {
            dbUrl = url;
        }
        if (user != null) dbUser = user;
        if (password != null) dbPassword = password;
        
        initializeDatabase();
    }

    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            initializeDatabase();
        }
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public static synchronized void initializeDatabase() {
        if (initialized) return;

        // Try registering H2 or SQLite driver
        try {
            Class.forName(DB_DRIVER_H2);
        } catch (ClassNotFoundException e1) {
            try {
                Class.forName(DB_DRIVER_SQLITE);
                if (dbUrl.contains("h2")) {
                    dbUrl = "jdbc:sqlite:./scbs.db";
                }
            } catch (ClassNotFoundException e2) {
                // If neither found, fallback to embedded H2 or Derby
                System.out.println("[DBConnectionManager] Note: Ensure H2 or SQLite JDBC jar is in classpath.");
            }
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {

            // 1. Classes Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS classes (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "semester VARCHAR(20) NOT NULL, " +
                    "total_students INT DEFAULT 60" +
                    ")");

            // 2. Students Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS students (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "roll_number VARCHAR(30) UNIQUE NOT NULL, " +
                    "full_name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "password_hash VARCHAR(128) NOT NULL, " +
                    "class_id VARCHAR(36), " +
                    "role VARCHAR(20) DEFAULT 'STUDENT', " +
                    "approval_status VARCHAR(20) DEFAULT 'PENDING', " +
                    "bunk_credits INT DEFAULT 100, " +
                    "reliability_score DOUBLE DEFAULT 100.0, " +
                    "face_descriptor TEXT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (class_id) REFERENCES classes(id)" +
                    ")");

            // 3. Timetable Slots Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS timetable_slots (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "class_id VARCHAR(36), " +
                    "day_of_week VARCHAR(15) NOT NULL, " +
                    "start_time VARCHAR(10) NOT NULL, " +
                    "end_time VARCHAR(10) NOT NULL, " +
                    "subject_name VARCHAR(100) NOT NULL, " +
                    "faculty_name VARCHAR(100) NOT NULL, " +
                    "room_number VARCHAR(30), " +
                    "FOREIGN KEY (class_id) REFERENCES classes(id)" +
                    ")");

            // 4. Bunk Proposals Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bunk_proposals (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "class_id VARCHAR(36), " +
                    "slot_id VARCHAR(36), " +
                    "creator_id VARCHAR(36), " +
                    "bunk_date VARCHAR(15) NOT NULL, " +
                    "reason TEXT, " +
                    "status VARCHAR(20) DEFAULT 'ACTIVE', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "expires_at TIMESTAMP NOT NULL, " +
                    "FOREIGN KEY (class_id) REFERENCES classes(id), " +
                    "FOREIGN KEY (slot_id) REFERENCES timetable_slots(id), " +
                    "FOREIGN KEY (creator_id) REFERENCES students(id)" +
                    ")");

            // 5. Bunk Votes Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bunk_votes (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "proposal_id VARCHAR(36), " +
                    "student_id VARCHAR(36), " +
                    "choice BOOLEAN NOT NULL, " +
                    "voted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "face_verified BOOLEAN DEFAULT TRUE, " +
                    "FOREIGN KEY (proposal_id) REFERENCES bunk_proposals(id), " +
                    "FOREIGN KEY (student_id) REFERENCES students(id), " +
                    "UNIQUE (proposal_id, student_id)" +
                    ")");

            seedInitialData(conn);
            initialized = true;
            System.out.println("[DBConnectionManager] Database tables and initial seed ready!");

        } catch (SQLException e) {
            System.err.println("[DBConnectionManager] Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void seedInitialData(Connection conn) throws SQLException {
        // Check if classes exist
        try (Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery("SELECT COUNT(*) FROM classes")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return; // already seeded
            }
        }

        String classCse3A = "class-cse-3a";
        String classCse3B = "class-cse-3b";
        String classIt2A  = "class-it-2a";

        // 1. Seed Classes
        String insertClass = "INSERT INTO classes (id, name, semester, total_students) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertClass)) {
            ps.setString(1, classCse3A);
            ps.setString(2, "CSE-3A");
            ps.setString(3, "5th Semester");
            ps.setInt(4, 55);
            ps.executeUpdate();

            ps.setString(1, classCse3B);
            ps.setString(2, "CSE-3B");
            ps.setString(3, "5th Semester");
            ps.setInt(4, 60);
            ps.executeUpdate();

            ps.setString(1, classIt2A);
            ps.setString(2, "IT-2A");
            ps.setString(3, "3rd Semester");
            ps.setInt(4, 50);
            ps.executeUpdate();
        }

        // 2. Seed Timetable Slots for CSE-3A
        String insertSlot = "INSERT INTO timetable_slots (id, class_id, day_of_week, start_time, end_time, subject_name, faculty_name, room_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSlot)) {
            String[][] slots = {
                {"slot-1", classCse3A, "MONDAY", "09:00", "10:00", "Operating Systems", "Dr. Alan Turing", "Lab 301"},
                {"slot-2", classCse3A, "MONDAY", "10:00", "11:00", "Database Management Systems", "Prof. Edgar Codd", "Hall 102"},
                {"slot-3", classCse3A, "MONDAY", "11:15", "12:15", "Computer Networks", "Dr. Vint Cerf", "Hall 102"},
                {"slot-4", classCse3A, "MONDAY", "13:00", "14:00", "Software Engineering", "Prof. Margaret Hamilton", "Hall 205"},
                
                {"slot-5", classCse3A, "TUESDAY", "09:00", "10:00", "Artificial Intelligence", "Dr. John McCarthy", "Lab 402"},
                {"slot-6", classCse3A, "TUESDAY", "10:00", "11:00", "Operating Systems", "Dr. Alan Turing", "Hall 102"},
                {"slot-7", classCse3A, "TUESDAY", "11:15", "12:15", "Cloud Computing", "Prof. Werner Vogels", "Lab 301"},
                {"slot-8", classCse3A, "TUESDAY", "13:00", "14:00", "Database Management Systems", "Prof. Edgar Codd", "Hall 102"},

                {"slot-9", classCse3A, "WEDNESDAY", "09:00", "10:00", "Computer Networks", "Dr. Vint Cerf", "Hall 102"},
                {"slot-10", classCse3A, "WEDNESDAY", "10:00", "11:00", "Software Engineering", "Prof. Margaret Hamilton", "Hall 205"},
                {"slot-11", classCse3A, "WEDNESDAY", "11:15", "13:15", "Web Tech & Full Stack Lab", "Prof. Tim Berners-Lee", "Lab 501"},

                {"slot-12", classCse3A, "THURSDAY", "09:00", "10:00", "Artificial Intelligence", "Dr. John McCarthy", "Lab 402"},
                {"slot-13", classCse3A, "THURSDAY", "10:00", "11:00", "Operating Systems", "Dr. Alan Turing", "Hall 102"},
                {"slot-14", classCse3A, "THURSDAY", "11:15", "12:15", "Database Systems Lab", "Prof. Edgar Codd", "Lab 301"},

                {"slot-15", classCse3A, "FRIDAY", "09:00", "10:00", "Cloud Computing", "Prof. Werner Vogels", "Lab 301"},
                {"slot-16", classCse3A, "FRIDAY", "10:00", "11:00", "Software Engineering", "Prof. Margaret Hamilton", "Hall 205"},
                {"slot-17", classCse3A, "FRIDAY", "11:15", "12:15", "Project Mentorship", "Dr. Alan Turing", "Seminar Room"}
            };

            for (String[] s : slots) {
                ps.setString(1, s[0]);
                ps.setString(2, s[1]);
                ps.setString(3, s[2]);
                ps.setString(4, s[3]);
                ps.setString(5, s[4]);
                ps.setString(6, s[5]);
                ps.setString(7, s[6]);
                ps.setString(8, s[7]);
                ps.executeUpdate();
            }
        }

        // 3. Seed Students & Admin
        String insertStudent = "INSERT INTO students (id, roll_number, full_name, email, password_hash, class_id, role, approval_status, bunk_credits, reliability_score, face_descriptor) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        String defaultFaceDesc = "[0.12, -0.45, 0.88, 0.32, -0.19, 0.74, -0.56, 0.23, 0.65, -0.31]";

        try (PreparedStatement ps = conn.prepareStatement(insertStudent)) {
            // Admin Account
            ps.setString(1, "admin-01");
            ps.setString(2, "ADMIN-001");
            ps.setString(3, "System Administrator");
            ps.setString(4, "admin@scbs.edu");
            ps.setString(5, PasswordUtil.hashPassword("admin123"));
            ps.setString(6, classCse3A);
            ps.setString(7, "ADMIN");
            ps.setString(8, "APPROVED");
            ps.setInt(9, 500);
            ps.setDouble(10, 100.0);
            ps.setString(11, defaultFaceDesc);
            ps.executeUpdate();

            // Approved Student (Alex Vance - Proposer)
            ps.setString(1, "student-01");
            ps.setString(2, "CSE-2024-001");
            ps.setString(3, "Alex Vance");
            ps.setString(4, "alex@scbs.edu");
            ps.setString(5, PasswordUtil.hashPassword("student123"));
            ps.setString(6, classCse3A);
            ps.setString(7, "STUDENT");
            ps.setString(8, "APPROVED");
            ps.setInt(9, 120);
            ps.setDouble(10, 96.5);
            ps.setString(11, defaultFaceDesc);
            ps.executeUpdate();

            // Approved Student (Sarah Jenkins)
            ps.setString(1, "student-02");
            ps.setString(2, "CSE-2024-002");
            ps.setString(3, "Sarah Jenkins");
            ps.setString(4, "sarah@scbs.edu");
            ps.setString(5, PasswordUtil.hashPassword("student123"));
            ps.setString(6, classCse3A);
            ps.setString(7, "STUDENT");
            ps.setString(8, "APPROVED");
            ps.setInt(9, 95);
            ps.setDouble(10, 92.0);
            ps.setString(11, defaultFaceDesc);
            ps.executeUpdate();

            // Approved Student (Jordan Lee)
            ps.setString(1, "student-03");
            ps.setString(2, "CSE-2024-003");
            ps.setString(3, "Jordan Lee");
            ps.setString(4, "jordan@scbs.edu");
            ps.setString(5, PasswordUtil.hashPassword("student123"));
            ps.setString(6, classCse3A);
            ps.setString(7, "STUDENT");
            ps.setString(8, "APPROVED");
            ps.setInt(9, 85);
            ps.setDouble(10, 88.5);
            ps.setString(11, null); // Face not enrolled
            ps.executeUpdate();

            // Pending Student (Liam Patel)
            ps.setString(1, "student-04");
            ps.setString(2, "CSE-2024-004");
            ps.setString(3, "Liam Patel");
            ps.setString(4, "liam@scbs.edu");
            ps.setString(5, PasswordUtil.hashPassword("student123"));
            ps.setString(6, classCse3A);
            ps.setString(7, "STUDENT");
            ps.setString(8, "PENDING");
            ps.setInt(9, 100);
            ps.setDouble(10, 100.0);
            ps.setString(11, null);
            ps.executeUpdate();
        }

        // 4. Seed Live Demo Bunk Proposal
        String insertProposal = "INSERT INTO bunk_proposals (id, class_id, slot_id, creator_id, bunk_date, reason, status, expires_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String propId = "prop-demo-01";
        try (PreparedStatement ps = conn.prepareStatement(insertProposal)) {
            ps.setString(1, propId);
            ps.setString(2, classCse3A);
            ps.setString(3, "slot-1"); // OS Lecture
            ps.setString(4, "student-01"); // Alex Vance
            ps.setString(5, java.time.LocalDate.now().toString());
            ps.setString(6, "Upcoming Hackathon preparation & collective project sprint.");
            ps.setString(7, "ACTIVE");
            ps.setTimestamp(8, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().plusDays(1)));
            ps.executeUpdate();
        }

        // 5. Seed Votes on Live Proposal
        String insertVote = "INSERT INTO bunk_votes (id, proposal_id, student_id, choice, face_verified) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertVote)) {
            ps.setString(1, "vote-01");
            ps.setString(2, propId);
            ps.setString(3, "student-01");
            ps.setBoolean(4, true); // Agree
            ps.setBoolean(5, true);
            ps.executeUpdate();

            ps.setString(1, "vote-02");
            ps.setString(2, propId);
            ps.setString(3, "student-02");
            ps.setBoolean(4, true); // Agree
            ps.setBoolean(5, true);
            ps.executeUpdate();
        }
    }
}
