package com.scbs.servlet;

import com.scbs.dao.ClassDAO;
import com.scbs.dao.TimetableDAO;
import com.scbs.model.ClassSection;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/timetable")
public class TimetableServlet extends HttpServlet {

    private final TimetableDAO timetableDAO = new TimetableDAO();
    private final ClassDAO classDAO = new ClassDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Student user = (Student) session.getAttribute("currentUser");

        String classId = req.getParameter("classId");
        if (classId == null || classId.trim().isEmpty()) {
            classId = user.getClassId();
        }

        ClassSection currentClass = classDAO.findById(classId);
        List<TimetableSlot> allSlots = timetableDAO.findByClassId(classId);

        // Group slots by Day of Week
        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
        Map<String, List<TimetableSlot>> slotsByDay = new HashMap<>();
        for (String day : days) {
            slotsByDay.put(day, timetableDAO.findByClassAndDay(classId, day));
        }

        DayOfWeek currentDay = LocalDate.now().getDayOfWeek();
        LocalTime currentTime = LocalTime.now();

        req.setAttribute("currentClass", currentClass);
        req.setAttribute("allClasses", classDAO.findAll());
        req.setAttribute("slotsByDay", slotsByDay);
        req.setAttribute("days", days);
        req.setAttribute("currentDayName", currentDay.name());
        req.setAttribute("currentTime", currentTime);

        req.getRequestDispatcher("/WEB-INF/views/timetable.jsp").forward(req, resp);
    }
}
