package com.scbs.servlet;

import com.scbs.dao.ClassDAO;
import com.scbs.dao.ProposalDAO;
import com.scbs.dao.VoteDAO;
import com.scbs.model.BunkProposal;
import com.scbs.model.BunkVote;
import com.scbs.model.ClassSection;
import com.scbs.model.Student;
import com.scbs.service.ConsensusService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/proposal")
public class ProposalDetailServlet extends HttpServlet {

    private final ProposalDAO proposalDAO = new ProposalDAO();
    private final VoteDAO voteDAO = new VoteDAO();
    private final ClassDAO classDAO = new ClassDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if (id == null || id.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=Invalid+proposal+ID");
            return;
        }

        BunkProposal proposal = proposalDAO.findById(id);
        if (proposal == null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=Proposal+not+found");
            return;
        }

        HttpSession session = req.getSession();
        Student currentUser = (Student) session.getAttribute("currentUser");

        // Check if current student has voted
        BunkVote userVote = voteDAO.findVoteByStudentAndProposal(currentUser.getId(), id);
        if (userVote != null) {
            proposal.setCurrentUserVote(userVote.isChoice());
        }

        // Get list of all votes
        List<BunkVote> votes = voteDAO.findVotesByProposalId(id);

        req.setAttribute("proposal", proposal);
        req.setAttribute("votes", votes);
        req.setAttribute("userVote", userVote);
        req.setAttribute("user", currentUser);

        req.getRequestDispatcher("/WEB-INF/views/proposal-detail.jsp").forward(req, resp);
    }
}
