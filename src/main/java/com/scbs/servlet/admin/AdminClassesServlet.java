package com.scbs.servlet.admin;

import com.scbs.dao.ClassDAO;
import com.scbs.model.ClassSection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/classes")
public class AdminClassesServlet extends HttpServlet {

    private final ClassDAO classDAO = new ClassDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ClassSection> classes = classDAO.findAll();
        req.setAttribute("classes", classes);
        req.getRequestDispatcher("/WEB-INF/views/admin/classes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("create".equals(action)) {
            String name = req.getParameter("name");
            String semester = req.getParameter("semester");
            String totalStudentsStr = req.getParameter("totalStudents");

            if (name != null && semester != null && !name.trim().isEmpty()) {
                int totalStudents = (totalStudentsStr != null && !totalStudentsStr.isEmpty()) ? Integer.parseInt(totalStudentsStr) : 60;
                ClassSection cs = new ClassSection(null, name.trim().toUpperCase(), semester.trim(), totalStudents);
                classDAO.createClass(cs);
            }
        } else if ("delete".equals(action)) {
            String classId = req.getParameter("classId");
            if (classId != null) {
                classDAO.deleteClass(classId);
            }
        }

        resp.sendRedirect(req.getContextPath() + "/admin/classes?msg=Class+updated.");
    }
}
