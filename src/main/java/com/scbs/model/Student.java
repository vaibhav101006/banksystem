package com.scbs.model;

import java.time.LocalDateTime;

public class Student {
    private String id;
    private String rollNumber;
    private String fullName;
    private String email;
    private String passwordHash;
    private String classId;
    private String className; // Helper for display
    private String role; // STUDENT, ADMIN
    private String approvalStatus; // PENDING, APPROVED, REJECTED
    private int bunkCredits;
    private double reliabilityScore;
    private String faceDescriptor; // JSON or comma-separated float array
    private LocalDateTime createdAt;

    public Student() {
        this.role = "STUDENT";
        this.approvalStatus = "PENDING";
        this.bunkCredits = 100;
        this.reliabilityScore = 100.0;
        this.createdAt = LocalDateTime.now();
    }

    public Student(String id, String rollNumber, String fullName, String email, String passwordHash, 
                   String classId, String role, String approvalStatus, int bunkCredits, 
                   double reliabilityScore, String faceDescriptor, LocalDateTime createdAt) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.classId = classId;
        this.role = role;
        this.approvalStatus = approvalStatus;
        this.bunkCredits = bunkCredits;
        this.reliabilityScore = reliabilityScore;
        this.faceDescriptor = faceDescriptor;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }

    public int getBunkCredits() { return bunkCredits; }
    public void setBunkCredits(int bunkCredits) { this.bunkCredits = bunkCredits; }

    public double getReliabilityScore() { return reliabilityScore; }
    public void setReliabilityScore(double reliabilityScore) { this.reliabilityScore = reliabilityScore; }

    public String getFaceDescriptor() { return faceDescriptor; }
    public void setFaceDescriptor(String faceDescriptor) { this.faceDescriptor = faceDescriptor; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isFaceEnrolled() {
        return faceDescriptor != null && !faceDescriptor.trim().isEmpty();
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }

    public boolean isApproved() {
        return "APPROVED".equalsIgnoreCase(approvalStatus);
    }
}
