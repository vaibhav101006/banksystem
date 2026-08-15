package com.scbs.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BunkProposal {
    private String id;
    private String classId;
    private String className;
    private String slotId;
    private TimetableSlot slot; // Expanded slot details
    private String creatorId;
    private String creatorName;
    private String creatorRoll;
    private LocalDate bunkDate;
    private String reason;
    private String status; // ACTIVE, PASSED, REJECTED, EXPIRED
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    
    // Aggregated stats
    private int agreeVotes;
    private int rejectVotes;
    private int totalVotes;
    private int classTotalStudents;
    private double quorumPercentage;
    private double approvalPercentage;
    private Boolean currentUserVote; // null if not voted, true for agree, false for reject

    public BunkProposal() {
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }

    public TimetableSlot getSlot() { return slot; }
    public void setSlot(TimetableSlot slot) { this.slot = slot; }

    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }

    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    public String getCreatorRoll() { return creatorRoll; }
    public void setCreatorRoll(String creatorRoll) { this.creatorRoll = creatorRoll; }

    public LocalDate getBunkDate() { return bunkDate; }
    public void setBunkDate(LocalDate bunkDate) { this.bunkDate = bunkDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public int getAgreeVotes() { return agreeVotes; }
    public void setAgreeVotes(int agreeVotes) { this.agreeVotes = agreeVotes; }

    public int getRejectVotes() { return rejectVotes; }
    public void setRejectVotes(int rejectVotes) { this.rejectVotes = rejectVotes; }

    public int getTotalVotes() { return totalVotes; }
    public void setTotalVotes(int totalVotes) { this.totalVotes = totalVotes; }

    public int getClassTotalStudents() { return classTotalStudents; }
    public void setClassTotalStudents(int classTotalStudents) { this.classTotalStudents = classTotalStudents; }

    public double getQuorumPercentage() { return quorumPercentage; }
    public void setQuorumPercentage(double quorumPercentage) { this.quorumPercentage = quorumPercentage; }

    public double getApprovalPercentage() { return approvalPercentage; }
    public void setApprovalPercentage(double approvalPercentage) { this.approvalPercentage = approvalPercentage; }

    public Boolean getCurrentUserVote() { return currentUserVote; }
    public void setCurrentUserVote(Boolean currentUserVote) { this.currentUserVote = currentUserVote; }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status) && (expiresAt == null || expiresAt.isAfter(LocalDateTime.now()));
    }
}
