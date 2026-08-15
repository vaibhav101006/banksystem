package com.scbs.dao;

import com.scbs.model.BunkProposal;
import com.scbs.model.TimetableSlot;
import com.scbs.service.ConsensusService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProposalDAO {

    private final VoteDAO voteDAO = new VoteDAO();

    public boolean createProposal(BunkProposal p) {
        String sql = "INSERT INTO bunk_proposals (id, class_id, slot_id, creator_id, bunk_date, reason, status, expires_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (p.getId() == null) p.setId(UUID.randomUUID().toString());
            ps.setString(1, p.getId());
            ps.setString(2, p.getClassId());
            ps.setString(3, p.getSlotId());
            ps.setString(4, p.getCreatorId());
            ps.setString(5, p.getBunkDate().toString());
            ps.setString(6, p.getReason());
            ps.setString(7, p.getStatus());
            ps.setTimestamp(8, Timestamp.valueOf(p.getExpiresAt()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BunkProposal findById(String id) {
        String sql = "SELECT p.*, c.name as class_name, c.total_students, " +
                     "s.full_name as creator_name, s.roll_number as creator_roll, " +
                     "ts.day_of_week, ts.start_time, ts.end_time, ts.subject_name, ts.faculty_name, ts.room_number " +
                     "FROM bunk_proposals p " +
                     "JOIN classes c ON p.class_id = c.id " +
                     "JOIN students s ON p.creator_id = s.id " +
                     "JOIN timetable_slots ts ON p.slot_id = ts.id " +
                     "WHERE p.id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProposal(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BunkProposal> findByClassId(String classId) {
        List<BunkProposal> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name as class_name, c.total_students, " +
                     "s.full_name as creator_name, s.roll_number as creator_roll, " +
                     "ts.day_of_week, ts.start_time, ts.end_time, ts.subject_name, ts.faculty_name, ts.room_number " +
                     "FROM bunk_proposals p " +
                     "JOIN classes c ON p.class_id = c.id " +
                     "JOIN students s ON p.creator_id = s.id " +
                     "JOIN timetable_slots ts ON p.slot_id = ts.id " +
                     "WHERE p.class_id = ? ORDER BY p.created_at DESC";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProposal(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<BunkProposal> findAll() {
        List<BunkProposal> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name as class_name, c.total_students, " +
                     "s.full_name as creator_name, s.roll_number as creator_roll, " +
                     "ts.day_of_week, ts.start_time, ts.end_time, ts.subject_name, ts.faculty_name, ts.room_number " +
                     "FROM bunk_proposals p " +
                     "JOIN classes c ON p.class_id = c.id " +
                     "JOIN students s ON p.creator_id = s.id " +
                     "JOIN timetable_slots ts ON p.slot_id = ts.id " +
                     "ORDER BY p.created_at DESC";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToProposal(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(String proposalId, String status) {
        String sql = "UPDATE bunk_proposals SET status = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, proposalId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private BunkProposal mapResultSetToProposal(ResultSet rs) throws SQLException {
        BunkProposal p = new BunkProposal();
        p.setId(rs.getString("id"));
        p.setClassId(rs.getString("class_id"));
        p.setClassName(rs.getString("class_name"));
        p.setSlotId(rs.getString("slot_id"));
        p.setCreatorId(rs.getString("creator_id"));
        p.setCreatorName(rs.getString("creator_name"));
        p.setCreatorRoll(rs.getString("creator_roll"));
        p.setBunkDate(LocalDate.parse(rs.getString("bunk_date")));
        p.setReason(rs.getString("reason"));
        p.setStatus(rs.getString("status"));
        
        Timestamp cAt = rs.getTimestamp("created_at");
        if (cAt != null) p.setCreatedAt(cAt.toLocalDateTime());
        Timestamp eAt = rs.getTimestamp("expires_at");
        if (eAt != null) p.setExpiresAt(eAt.toLocalDateTime());

        // Slot details
        TimetableSlot slot = new TimetableSlot();
        slot.setId(p.getSlotId());
        slot.setClassId(p.getClassId());
        slot.setDayOfWeek(rs.getString("day_of_week"));
        slot.setStartTime(LocalTime.parse(rs.getString("start_time")));
        slot.setEndTime(LocalTime.parse(rs.getString("end_time")));
        slot.setSubjectName(rs.getString("subject_name"));
        slot.setFacultyName(rs.getString("faculty_name"));
        slot.setRoomNumber(rs.getString("room_number"));
        p.setSlot(slot);

        // Compute votes & stats
        int agree = voteDAO.countVotesByChoice(p.getId(), true);
        int reject = voteDAO.countVotesByChoice(p.getId(), false);
        p.setAgreeVotes(agree);
        p.setRejectVotes(reject);
        int totalClass = rs.getInt("total_students");
        ConsensusService.computeProposalStats(p, totalClass);

        return p;
    }
}
