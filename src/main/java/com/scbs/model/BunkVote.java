package com.scbs.model;

import java.time.LocalDateTime;

public class BunkVote {
    private String id;
    private String proposalId;
    private String studentId;
    private String studentName;
    private String studentRoll;
    private boolean choice; // true = AGREE, false = REJECT
    private LocalDateTime votedAt;
    private boolean faceVerified;

    public BunkVote() {
        this.votedAt = LocalDateTime.now();
        this.faceVerified = true;
    }

    public BunkVote(String id, String proposalId, String studentId, boolean choice, 
                    LocalDateTime votedAt, boolean faceVerified) {
        this.id = id;
        this.proposalId = proposalId;
        this.studentId = studentId;
        this.choice = choice;
        this.votedAt = votedAt;
        this.faceVerified = faceVerified;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProposalId() { return proposalId; }
    public void setProposalId(String proposalId) { this.proposalId = proposalId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentRoll() { return studentRoll; }
    public void setStudentRoll(String studentRoll) { this.studentRoll = studentRoll; }

    public boolean isChoice() { return choice; }
    public void setChoice(boolean choice) { this.choice = choice; }

    public LocalDateTime getVotedAt() { return votedAt; }
    public void setVotedAt(LocalDateTime votedAt) { this.votedAt = votedAt; }

    public boolean isFaceVerified() { return faceVerified; }
    public void setFaceVerified(boolean faceVerified) { this.faceVerified = faceVerified; }
}
