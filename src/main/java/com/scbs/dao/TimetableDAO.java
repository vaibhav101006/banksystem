package com.scbs.dao;

import com.scbs.model.TimetableSlot;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimetableDAO {

    public List<TimetableSlot> findByClassId(String classId) {
        List<TimetableSlot> list = new ArrayList<>();
        String sql = "SELECT * FROM timetable_slots WHERE class_id = ? ORDER BY " +
                     "CASE day_of_week " +
                     "WHEN 'MONDAY' THEN 1 " +
                     "WHEN 'TUESDAY' THEN 2 " +
                     "WHEN 'WEDNESDAY' THEN 3 " +
                     "WHEN 'THURSDAY' THEN 4 " +
                     "WHEN 'FRIDAY' THEN 5 " +
                     "WHEN 'SATURDAY' THEN 6 " +
                     "ELSE 7 END, start_time ASC";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<TimetableSlot> findByClassAndDay(String classId, String dayOfWeek) {
        List<TimetableSlot> list = new ArrayList<>();
        String sql = "SELECT * FROM timetable_slots WHERE class_id = ? AND UPPER(day_of_week) = UPPER(?) ORDER BY start_time ASC";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classId);
            ps.setString(2, dayOfWeek);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSlot(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public TimetableSlot findById(String id) {
        String sql = "SELECT * FROM timetable_slots WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSlot(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createSlot(TimetableSlot slot) {
        String sql = "INSERT INTO timetable_slots (id, class_id, day_of_week, start_time, end_time, subject_name, faculty_name, room_number) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (slot.getId() == null) slot.setId(UUID.randomUUID().toString());
            ps.setString(1, slot.getId());
            ps.setString(2, slot.getClassId());
            ps.setString(3, slot.getDayOfWeek().toUpperCase());
            ps.setString(4, slot.getStartTime().toString());
            ps.setString(5, slot.getEndTime().toString());
            ps.setString(6, slot.getSubjectName());
            ps.setString(7, slot.getFacultyName());
            ps.setString(8, slot.getRoomNumber());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSlot(String id) {
        String sql = "DELETE FROM timetable_slots WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private TimetableSlot mapResultSetToSlot(ResultSet rs) throws SQLException {
        TimetableSlot slot = new TimetableSlot();
        slot.setId(rs.getString("id"));
        slot.setClassId(rs.getString("class_id"));
        slot.setDayOfWeek(rs.getString("day_of_week"));
        String startStr = rs.getString("start_time");
        String endStr = rs.getString("end_time");
        if (startStr != null) slot.setStartTime(LocalTime.parse(startStr));
        if (endStr != null) slot.setEndTime(LocalTime.parse(endStr));
        slot.setSubjectName(rs.getString("subject_name"));
        slot.setFacultyName(rs.getString("faculty_name"));
        slot.setRoomNumber(rs.getString("room_number"));
        return slot;
    }
}
