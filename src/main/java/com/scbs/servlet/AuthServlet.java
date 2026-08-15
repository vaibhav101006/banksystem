package com.scbs.servlet;

import com.scbs.dao.ClassDAO;
import com.scbs.dao.StudentDAO;
import com.scbs.model.ClassSection;
import com.scbs.model.Student;
import com.scbs.util.PasswordUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {

    private final StudentDAO studentDAO = new StudentDAO();
    private final ClassDAO classDAO = new ClassDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login?msg=Logged+out+successfully");
            return;
        }

        // If user already logged in, redirect to dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        if ("/register".equals(path)) {
            List<ClassSection> classes = classDAO.findAll();
            req.setAttribute("classes", classes);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/register".equals(path)) {
            handleRegister(req, resp);
        } else {
            handleLogin(req, resp);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "Please provide email and password.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
            return;
        }

        Student student = studentDAO.findByEmail(email.trim());
        if (student == null || !PasswordUtil.verifyPassword(password, student.getPasswordHash())) {
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
            return;
        }

        if (!student.isApproved() && !student.isAdmin()) {
            req.setAttribute("error", "Your account registration is pending admin approval.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("currentUser", student);
        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String rollNumber = req.getParameter("rollNumber");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String classId = req.getParameter("classId");

        if (fullName == null || rollNumber == null || email == null || password == null || classId == null ||
            fullName.trim().isEmpty() || rollNumber.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "All fields are required.");
            req.setAttribute("classes", classDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (studentDAO.findByEmail(email.trim()) != null) {
            req.setAttribute("error", "Email already registered.");
            req.setAttribute("classes", classDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        Student s = new Student();
        s.setFullName(fullName.trim());
        s.setRollNumber(rollNumber.trim().toUpperCase());
        s.setEmail(email.trim());
        s.setPasswordHash(PasswordUtil.hashPassword(password));
        s.setClassId(classId);
        s.setRole("STUDENT");
        s.setApprovalStatus("PENDING"); // Requires admin approval
        s.setBunkCredits(100);
        s.setReliabilityScore(100.0);

        boolean created = studentDAO.createStudent(s);
        if (created) {
            resp.sendRedirect(req.getContextPath() + "/login?msg=Registration+successful.+Wait+for+admin+approval.");
        } else {
            req.setAttribute("error", "Could not register. Roll number or email may already be taken.");
            req.setAttribute("classes", classDAO.findAll());
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
        }
    }
}
