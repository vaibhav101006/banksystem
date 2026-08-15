package com.scbs.dao;

import com.scbs.model.ClassSection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClassDAO {

    public List<ClassSection> findAll() {
        List<ClassSection> list = new ArrayList<>();
        String sql = "SELECT * FROM classes ORDER BY name ASC";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ClassSection(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("semester"),
                    rs.getInt("total_students")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ClassSection findById(String id) {
        String sql = "SELECT * FROM classes WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ClassSection(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("semester"),
                        rs.getInt("total_students")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createClass(ClassSection cs) {
        String sql = "INSERT INTO classes (id, name, semester, total_students) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (cs.getId() == null) cs.setId(UUID.randomUUID().toString());
            ps.setString(1, cs.getId());
            ps.setString(2, cs.getName());
            ps.setString(3, cs.getSemester());
            ps.setInt(4, cs.getTotalStudents());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteClass(String id) {
        String sql = "DELETE FROM classes WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
