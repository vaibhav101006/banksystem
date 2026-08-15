package com.scbs.service;

import com.scbs.model.Student;

public class ReputationService {

    public static final int PROPOSAL_CREATION_COST = 10;
    public static final int VOTE_PARTICIPATION_REWARD = 5;
    public static final double HONESTY_REWARD_RELIABILITY = 2.0; // +2% reliability
    public static final double DEFECTION_PENALTY_RELIABILITY = 15.0; // -15% reliability

    public static boolean canProposeBunk(Student student) {
        if (student == null) return false;
        return student.getBunkCredits() >= PROPOSAL_CREATION_COST && student.isFaceEnrolled() && student.isApproved();
    }

    public static int deductProposalCost(Student student) {
        int newCredits = Math.max(0, student.getBunkCredits() - PROPOSAL_CREATION_COST);
        student.setBunkCredits(newCredits);
        return newCredits;
    }

    public static int rewardVotingParticipation(Student student) {
        int newCredits = student.getBunkCredits() + VOTE_PARTICIPATION_REWARD;
        student.setBunkCredits(newCredits);
        return newCredits;
    }
}
