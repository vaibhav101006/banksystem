package com.scbs.servlet;

import com.scbs.dao.ProposalDAO;
import com.scbs.dao.StudentDAO;
import com.scbs.dao.TimetableDAO;
import com.scbs.model.BunkProposal;
import com.scbs.model.Student;
import com.scbs.model.TimetableSlot;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();
    private final ProposalDAO proposalDAO = new ProposalDAO();
    private final TimetableDAO timetableDAO = new TimetableDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Student sessionUser = (Student) session.getAttribute("currentUser");

        // Refresh user info from database to get fresh credits & reliability
        Student user = studentDAO.findById(sessionUser.getId());
        if (user != null) {
            session.setAttribute("currentUser", user);
        } else {
            user = sessionUser;
        }

        // Fetch active proposals for user's class
        List<BunkProposal> proposals = proposalDAO.findByClassId(user.getClassId());
        
        // Find current or upcoming lecture today
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        List<TimetableSlot> todaySlots = timetableDAO.findByClassAndDay(user.getClassId(), dayOfWeek.name());
        
        TimetableSlot activeSlot = null;
        LocalTime now = LocalTime.now();
        for (TimetableSlot slot : todaySlots) {
            if (slot.getStartTime().isBefore(now) && slot.getEndTime().isAfter(now)) {
                activeSlot = slot;
                break;
            } else if (slot.getStartTime().isAfter(now) && activeSlot == null) {
                activeSlot = slot;
            }
        }

        req.setAttribute("user", user);
        req.setAttribute("proposals", proposals);
        req.setAttribute("todaySlots", todaySlots);
        req.setAttribute("activeSlot", activeSlot);

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}
