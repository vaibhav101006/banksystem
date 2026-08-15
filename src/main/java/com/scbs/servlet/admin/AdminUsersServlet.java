package com.scbs.servlet.admin;

import com.scbs.dao.StudentDAO;
import com.scbs.model.Student;
import com.scbs.util.PasswordUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Student> students = studentDAO.findAll();
        req.setAttribute("students", students);
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentId = req.getParameter("studentId");
        String action = req.getParameter("action");

        if (studentId != null && action != null) {
            if ("toggleAdmin".equals(action)) {
                String newRole = req.getParameter("newRole");
                studentDAO.updateRole(studentId, newRole);
            } else if ("resetPassword".equals(action)) {
                String defaultPass = "student123";
                studentDAO.updatePassword(studentId, PasswordUtil.hashPassword(defaultPass));
            } else if ("updateScore".equals(action)) {
                int credits = Integer.parseInt(req.getParameter("credits"));
                double reliability = Double.parseDouble(req.getParameter("reliability"));
                studentDAO.updateCreditsAndReliability(studentId, credits, reliability);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/users?msg=User+updated+successfully.");
    }
}
