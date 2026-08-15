package com.scbs.servlet;

import com.scbs.dao.ProposalDAO;
import com.scbs.dao.StudentDAO;
import com.scbs.dao.TimetableDAO;
import com.scbs.dao.VoteDAO;
import com.scbs.model.BunkProposal;
import com.scbs.model.BunkVote;
import com.scbs.model.Student;
import com.scbs.model.TimetableSlot;
import com.scbs.service.BiometricService;
import com.scbs.service.ReputationService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@WebServlet("/propose")
public class ProposeServlet extends HttpServlet {

    private final ProposalDAO proposalDAO = new ProposalDAO();
    private final TimetableDAO timetableDAO = new TimetableDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final VoteDAO voteDAO = new VoteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Student user = studentDAO.findById(((Student) session.getAttribute("currentUser")).getId());
        session.setAttribute("currentUser", user);

        if (!user.isFaceEnrolled()) {
            resp.sendRedirect(req.getContextPath() + "/profile?error=Please+enroll+your+face+before+proposing+a+bunk.");
            return;
        }

        if (user.getBunkCredits() < ReputationService.PROPOSAL_CREATION_COST) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=Insufficient+Bunk+Credits.+You+need+at+least+" + ReputationService.PROPOSAL_CREATION_COST + "+credits.");
            return;
        }

        List<TimetableSlot> slots = timetableDAO.findByClassId(user.getClassId());
        req.setAttribute("slots", slots);
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/propose.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Student user = studentDAO.findById(((Student) session.getAttribute("currentUser")).getId());

        String slotId = req.getParameter("slotId");
        String bunkDateStr = req.getParameter("bunkDate");
        String reason = req.getParameter("reason");
        String liveFaceDescriptor = req.getParameter("faceDescriptor");

        if (slotId == null || bunkDateStr == null || slotId.trim().isEmpty() || bunkDateStr.trim().isEmpty()) {
            req.setAttribute("error", "Please select a lecture slot and date.");
            req.setAttribute("slots", timetableDAO.findByClassId(user.getClassId()));
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/views/propose.jsp").forward(req, resp);
            return;
        }

        // Biometric Face Check
        if (liveFaceDescriptor != null && !liveFaceDescriptor.trim().isEmpty()) {
            boolean matched = BiometricService.verifyFace(user.getFaceDescriptor(), liveFaceDescriptor);
            if (!matched) {
                req.setAttribute("error", "Biometric verification failed. Face does not match profile descriptor.");
                req.setAttribute("slots", timetableDAO.findByClassId(user.getClassId()));
                req.setAttribute("user", user);
                req.getRequestDispatcher("/WEB-INF/views/propose.jsp").forward(req, resp);
                return;
            }
        }

        LocalDate bunkDate = LocalDate.parse(bunkDateStr);
        TimetableSlot slot = timetableDAO.findById(slotId);

        // Deduct 10 bunk credits
        int updatedCredits = ReputationService.deductProposalCost(user);
        studentDAO.updateCreditsAndReliability(user.getId(), updatedCredits, user.getReliabilityScore());
        user.setBunkCredits(updatedCredits);
        session.setAttribute("currentUser", user);

        // Create proposal
        String proposalId = UUID.randomUUID().toString();
        BunkProposal p = new BunkProposal();
        p.setId(proposalId);
        p.setClassId(user.getClassId());
        p.setSlotId(slotId);
        p.setCreatorId(user.getId());
        p.setBunkDate(bunkDate);
        p.setReason(reason != null ? reason.trim() : "Collective Class Absence");
        p.setStatus("ACTIVE");
        p.setExpiresAt(LocalDateTime.now().plusDays(1)); // Voting open for 24h or till lecture

        proposalDAO.createProposal(p);

        // Automatically cast Creator's AGREE vote
        BunkVote vote = new BunkVote(UUID.randomUUID().toString(), proposalId, user.getId(), true, LocalDateTime.now(), true);
        voteDAO.castVote(vote);

        resp.sendRedirect(req.getContextPath() + "/proposal?id=" + proposalId + "&msg=Proposal+created+and+voting+is+live!");
    }
}
