package com.scbs.servlet.admin;

import com.scbs.dao.ProposalDAO;
import com.scbs.model.BunkProposal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/proposals")
public class AdminProposalsServlet extends HttpServlet {

    private final ProposalDAO proposalDAO = new ProposalDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BunkProposal> proposals = proposalDAO.findAll();
        req.setAttribute("proposals", proposals);
        req.getRequestDispatcher("/WEB-INF/views/admin/proposals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String proposalId = req.getParameter("proposalId");
        String status = req.getParameter("status"); // "PASSED", "REJECTED", "EXPIRED"

        if (proposalId != null && status != null) {
            proposalDAO.updateStatus(proposalId, status);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/proposals?msg=Proposal+status+updated.");
    }
}
