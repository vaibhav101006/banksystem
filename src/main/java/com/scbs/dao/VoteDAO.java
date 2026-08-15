package com.scbs.dao;

import com.scbs.model.BunkVote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VoteDAO {

    public boolean castVote(BunkVote vote) {
        String sql = "INSERT INTO bunk_votes (id, proposal_id, student_id, choice, face_verified, voted_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (vote.getId() == null) vote.setId(UUID.randomUUID().toString());
            ps.setString(1, vote.getId());
            ps.setString(2, vote.getProposalId());
            ps.setString(3, vote.getStudentId());
            ps.setBoolean(4, vote.isChoice());
            ps.setBoolean(5, vote.isFaceVerified());
            ps.setTimestamp(6, Timestamp.valueOf(vote.getVotedAt()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BunkVote findVoteByStudentAndProposal(String studentId, String proposalId) {
        String sql = "SELECT v.*, s.full_name, s.roll_number FROM bunk_votes v " +
                     "JOIN students s ON v.student_id = s.id " +
                     "WHERE v.student_id = ? AND v.proposal_id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, proposalId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVote(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BunkVote> findVotesByProposalId(String proposalId) {
        List<BunkVote> list = new ArrayList<>();
        String sql = "SELECT v.*, s.full_name, s.roll_number FROM bunk_votes v " +
                     "JOIN students s ON v.student_id = s.id " +
                     "WHERE v.proposal_id = ? ORDER BY v.voted_at DESC";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, proposalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToVote(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countVotesByChoice(String proposalId, boolean choice) {
        String sql = "SELECT COUNT(*) FROM bunk_votes WHERE proposal_id = ? AND choice = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, proposalId);
            ps.setBoolean(2, choice);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private BunkVote mapResultSetToVote(ResultSet rs) throws SQLException {
        BunkVote v = new BunkVote();
        v.setId(rs.getString("id"));
        v.setProposalId(rs.getString("proposal_id"));
        v.setStudentId(rs.getString("student_id"));
        v.setStudentName(rs.getString("full_name"));
        v.setStudentRoll(rs.getString("roll_number"));
        v.setChoice(rs.getBoolean("choice"));
        v.setFaceVerified(rs.getBoolean("face_verified"));
        Timestamp ts = rs.getTimestamp("voted_at");
        if (ts != null) v.setVotedAt(ts.toLocalDateTime());
        return v;
    }
}
