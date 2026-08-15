package com.scbs.service;

import com.scbs.model.BunkProposal;

public class ConsensusService {

    // Quorum threshold: at least 60% of class must participate
    public static final double QUORUM_THRESHOLD_PERCENT = 60.0;
    
    // Supermajority threshold: at least 75% of votes cast must be AGREE
    public static final double PASS_THRESHOLD_PERCENT = 75.0;

    public static void computeProposalStats(BunkProposal proposal, int totalClassStudents) {
        if (proposal == null) return;
        
        proposal.setClassTotalStudents(totalClassStudents);
        int totalVotes = proposal.getAgreeVotes() + proposal.getRejectVotes();
        proposal.setTotalVotes(totalVotes);

        if (totalClassStudents > 0) {
            double quorum = ((double) totalVotes / totalClassStudents) * 100.0;
            proposal.setQuorumPercentage(Math.round(quorum * 10.0) / 10.0);
        } else {
            proposal.setQuorumPercentage(0.0);
        }

        if (totalVotes > 0) {
            double approval = ((double) proposal.getAgreeVotes() / totalVotes) * 100.0;
            proposal.setApprovalPercentage(Math.round(approval * 10.0) / 10.0);
        } else {
            proposal.setApprovalPercentage(0.0);
        }
    }

    public static String evaluateProposalResult(BunkProposal proposal, int totalClassStudents) {
        computeProposalStats(proposal, totalClassStudents);
        
        if (proposal.getQuorumPercentage() < QUORUM_THRESHOLD_PERCENT) {
            return "REJECTED"; // Quorum not met
        }
        
        if (proposal.getApprovalPercentage() >= PASS_THRESHOLD_PERCENT) {
            return "PASSED";
        } else {
            return "REJECTED";
        }
    }
}
