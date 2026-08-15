package com.scbs.servlet;

import com.scbs.dao.StudentDAO;
import com.scbs.model.Student;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Student sessionUser = (Student) session.getAttribute("currentUser");

        Student user = studentDAO.findById(sessionUser.getId());
        session.setAttribute("currentUser", user);

        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Student sessionUser = (Student) session.getAttribute("currentUser");

        String action = req.getParameter("action");
        if ("enrollFace".equals(action)) {
            String descriptor = req.getParameter("faceDescriptor");
            if (descriptor != null && !descriptor.trim().isEmpty()) {
                studentDAO.updateFaceDescriptor(sessionUser.getId(), descriptor.trim());
                Student updated = studentDAO.findById(sessionUser.getId());
                session.setAttribute("currentUser", updated);
                resp.sendRedirect(req.getContextPath() + "/profile?msg=Face+biometrics+enrolled+successfully!");
                return;
            } else {
                resp.sendRedirect(req.getContextPath() + "/profile?error=No+biometric+data+captured.");
                return;
            }
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
