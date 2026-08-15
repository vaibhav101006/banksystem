package com.scbs.dao;

import com.scbs.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentDAO {

    public boolean createStudent(Student s) {
        String sql = "INSERT INTO students (id, roll_number, full_name, email, password_hash, class_id, role, approval_status, bunk_credits, reliability_score, face_descriptor) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (s.getId() == null) s.setId(UUID.randomUUID().toString());
            ps.setString(1, s.getId());
            ps.setString(2, s.getRollNumber());
            ps.setString(3, s.getFullName());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getPasswordHash());
            ps.setString(6, s.getClassId());
            ps.setString(7, s.getRole());
            ps.setString(8, s.getApprovalStatus());
            ps.setInt(9, s.getBunkCredits());
            ps.setDouble(10, s.getReliabilityScore());
            ps.setString(11, s.getFaceDescriptor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Student findByEmail(String email) {
        String sql = "SELECT s.*, c.name as class_name FROM students s " +
                     "LEFT JOIN classes c ON s.class_id = c.id WHERE LOWER(s.email) = LOWER(?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Student findById(String id) {
        String sql = "SELECT s.*, c.name as class_name FROM students s " +
                     "LEFT JOIN classes c ON s.class_id = c.id WHERE s.id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> findAll() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.*, c.name as class_name FROM students s LEFT JOIN classes c ON s.class_id = c.id ORDER BY s.roll_number ASC";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Student> findPendingApprovals() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.*, c.name as class_name FROM students s LEFT JOIN classes c ON s.class_id = c.id " +
                     "WHERE s.approval_status = 'PENDING' ORDER BY s.created_at DESC";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateApprovalStatus(String studentId, String status) {
        String sql = "UPDATE students SET approval_status = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRole(String studentId, String role) {
        String sql = "UPDATE students SET role = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setString(2, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(String studentId, String newPasswordHash) {
        String sql = "UPDATE students SET password_hash = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setString(2, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFaceDescriptor(String studentId, String descriptor) {
        String sql = "UPDATE students SET face_descriptor = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descriptor);
            ps.setString(2, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCreditsAndReliability(String studentId, int credits, double reliability) {
        String sql = "UPDATE students SET bunk_credits = ?, reliability_score = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, credits);
            ps.setDouble(2, reliability);
            ps.setString(3, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getString("id"));
        s.setRollNumber(rs.getString("roll_number"));
        s.setFullName(rs.getString("full_name"));
        s.setEmail(rs.getString("email"));
        s.setPasswordHash(rs.getString("password_hash"));
        s.setClassId(rs.getString("class_id"));
        s.setClassName(rs.getString("class_name"));
        s.setRole(rs.getString("role"));
        s.setApprovalStatus(rs.getString("approval_status"));
        s.setBunkCredits(rs.getInt("bunk_credits"));
        s.setReliabilityScore(rs.getDouble("reliability_score"));
        s.setFaceDescriptor(rs.getString("face_descriptor"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) s.setCreatedAt(ts.toLocalDateTime());
        return s;
    }
}
