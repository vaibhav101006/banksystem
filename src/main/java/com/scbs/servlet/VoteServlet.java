package com.scbs.servlet;

import com.scbs.dao.ProposalDAO;
import com.scbs.dao.StudentDAO;
import com.scbs.dao.VoteDAO;
import com.scbs.model.BunkProposal;
import com.scbs.model.BunkVote;
import com.scbs.model.Student;
import com.scbs.service.BiometricService;
import com.scbs.service.ConsensusService;
import com.scbs.service.ReputationService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@WebServlet("/vote")
public class VoteServlet extends HttpServlet {

    private final VoteDAO voteDAO = new VoteDAO();
    private final ProposalDAO proposalDAO = new ProposalDAO();
    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Student currentUser = studentDAO.findById(((Student) session.getAttribute("currentUser")).getId());

        String proposalId = req.getParameter("proposalId");
        String choiceStr = req.getParameter("choice"); // "agree" or "reject"
        String liveFaceDescriptor = req.getParameter("faceDescriptor");

        if (proposalId == null || choiceStr == null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=Missing+vote+parameters");
            return;
        }

        BunkProposal proposal = proposalDAO.findById(proposalId);
        if (proposal == null || !proposal.isActive()) {
            resp.sendRedirect(req.getContextPath() + "/proposal?id=" + proposalId + "&error=Proposal+is+not+active+for+voting");
            return;
        }

        // Check if student has enrolled face
        if (!currentUser.isFaceEnrolled()) {
            resp.sendRedirect(req.getContextPath() + "/profile?error=Please+enroll+your+face+first+to+vote.");
            return;
        }

        // Check duplicate vote
        BunkVote existingVote = voteDAO.findVoteByStudentAndProposal(currentUser.getId(), proposalId);
        if (existingVote != null) {
            resp.sendRedirect(req.getContextPath() + "/proposal?id=" + proposalId + "&error=You+have+already+voted+on+this+proposal");
            return;
        }

        // Biometric Face Verification
        if (liveFaceDescriptor != null && !liveFaceDescriptor.trim().isEmpty()) {
            boolean faceMatched = BiometricService.verifyFace(currentUser.getFaceDescriptor(), liveFaceDescriptor);
            if (!faceMatched) {
                resp.sendRedirect(req.getContextPath() + "/proposal?id=" + proposalId + "&error=Biometric+verification+failed.+Face+did+not+match.");
                return;
            }
        }

        boolean choice = "agree".equalsIgnoreCase(choiceStr);
        BunkVote vote = new BunkVote(UUID.randomUUID().toString(), proposalId, currentUser.getId(), choice, LocalDateTime.now(), true);
        voteDAO.castVote(vote);

        // Reward student for participation
        int newCredits = ReputationService.rewardVotingParticipation(currentUser);
        studentDAO.updateCreditsAndReliability(currentUser.getId(), newCredits, currentUser.getReliabilityScore());
        currentUser.setBunkCredits(newCredits);
        session.setAttribute("currentUser", currentUser);

        // Refresh proposal status if quorum/majority reached
        BunkProposal updatedProp = proposalDAO.findById(proposalId);
        if (updatedProp != null) {
            String evaluatedStatus = ConsensusService.evaluateProposalResult(updatedProp, updatedProp.getClassTotalStudents());
            if (updatedProp.getQuorumPercentage() >= ConsensusService.QUORUM_THRESHOLD_PERCENT) {
                proposalDAO.updateStatus(proposalId, evaluatedStatus);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/proposal?id=" + proposalId + "&msg=Vote+recorded+successfully+with+biometric+verification!");
    }
}
