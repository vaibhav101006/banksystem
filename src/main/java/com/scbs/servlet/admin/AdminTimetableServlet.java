package com.scbs.servlet.admin;

import com.scbs.dao.ClassDAO;
import com.scbs.dao.TimetableDAO;
import com.scbs.model.ClassSection;
import com.scbs.model.TimetableSlot;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/admin/timetable")
public class AdminTimetableServlet extends HttpServlet {

    private final TimetableDAO timetableDAO = new TimetableDAO();
    private final ClassDAO classDAO = new ClassDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<ClassSection> classes = classDAO.findAll();
        String selectedClassId = req.getParameter("classId");
        if ((selectedClassId == null || selectedClassId.isEmpty()) && !classes.isEmpty()) {
            selectedClassId = classes.get(0).getId();
        }

        List<TimetableSlot> slots = (selectedClassId != null) ? timetableDAO.findByClassId(selectedClassId) : List.of();

        req.setAttribute("classes", classes);
        req.setAttribute("selectedClassId", selectedClassId);
        req.setAttribute("slots", slots);
        req.getRequestDispatcher("/WEB-INF/views/admin/timetable.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            String slotId = req.getParameter("slotId");
            if (slotId != null) {
                timetableDAO.deleteSlot(slotId);
            }
        } else if ("create".equals(action)) {
            String classId = req.getParameter("classId");
            String dayOfWeek = req.getParameter("dayOfWeek");
            String startTimeStr = req.getParameter("startTime");
            String endTimeStr = req.getParameter("endTime");
            String subjectName = req.getParameter("subjectName");
            String facultyName = req.getParameter("facultyName");
            String roomNumber = req.getParameter("roomNumber");

            if (classId != null && dayOfWeek != null && startTimeStr != null && endTimeStr != null && subjectName != null) {
                TimetableSlot slot = new TimetableSlot();
                slot.setClassId(classId);
                slot.setDayOfWeek(dayOfWeek);
                slot.setStartTime(LocalTime.parse(startTimeStr));
                slot.setEndTime(LocalTime.parse(endTimeStr));
                slot.setSubjectName(subjectName);
                slot.setFacultyName(facultyName != null ? facultyName : "");
                slot.setRoomNumber(roomNumber != null ? roomNumber : "");
                timetableDAO.createSlot(slot);
            }
        }

        String classId = req.getParameter("classId");
        resp.sendRedirect(req.getContextPath() + "/admin/timetable" + (classId != null ? "?classId=" + classId : ""));
    }
}
