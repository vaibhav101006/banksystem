<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map, com.scbs.model.ClassSection, com.scbs.model.TimetableSlot" %>
<%
  ClassSection currentClass = (ClassSection) request.getAttribute("currentClass");
  List<ClassSection> allClasses = (List<ClassSection>) request.getAttribute("allClasses");
  Map<String, List<TimetableSlot>> slotsByDay = (Map<String, List<TimetableSlot>>) request.getAttribute("slotsByDay");
  String[] days = (String[]) request.getAttribute("days");
  String currentDayName = (String) request.getAttribute("currentDayName");
  request.setAttribute("pageTitle", "Timetable — SCBS");
%>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/navbar.jsp"/>

<div class="container">
  
  <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-wrap: wrap; gap: 16px;">
    <div>
      <h1 style="font-size: 1.5rem; font-weight: 700; letter-spacing: -0.02em;">Weekly Timetable</h1>
      <p style="color: var(--text-muted); font-size: 0.88rem; margin-top: 2px;">
        Class Schedule for <%= currentClass != null ? currentClass.getName() : "Your Class" %> &bull; Current day is highlighted
      </p>
    </div>

    <!-- Class Selector -->
    <form action="${pageContext.request.contextPath}/timetable" method="GET" style="display: flex; align-items: center; gap: 8px;">
      <label for="classSelect" style="font-size: 0.85rem; color: var(--text-muted);">Switch Class:</label>
      <select id="classSelect" name="classId" class="form-control" style="width: auto;" onchange="this.form.submit()">
        <% for (ClassSection cs : allClasses) { %>
          <option value="<%= cs.getId() %>" <%= (currentClass != null && cs.getId().equals(currentClass.getId())) ? "selected" : "" %>>
            <%= cs.getName() %> (<%= cs.getSemester() %>)
          </option>
        <% } %>
      </select>
    </form>
  </div>

  <!-- Weekly Timetable Grid by Days -->
  <div style="display: flex; flex-direction: column; gap: 20px;">
    <% for (String day : days) { 
        boolean isToday = day.equalsIgnoreCase(currentDayName);
        List<TimetableSlot> slots = slotsByDay.get(day);
    %>
      <div class="card" style="<%= isToday ? "border-color: rgba(59, 130, 246, 0.5); box-shadow: 0 0 15px rgba(59, 130, 246, 0.1);" : "" %>">
        
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;">
          <div style="display: flex; align-items: center; gap: 10px;">
            <h3 style="font-size: 1.1rem; font-weight: 600;"><%= day %></h3>
            <% if (isToday) { %>
              <span class="badge badge-active">TODAY</span>
            <% } %>
          </div>
          <span style="font-size: 0.8rem; color: var(--text-dim);"><%= (slots != null ? slots.size() : 0) %> Lectures</span>
        </div>

        <% if (slots == null || slots.isEmpty()) { %>
          <div style="color: var(--text-dim); font-size: 0.85rem; font-style: italic; padding: 12px 0;">No lectures scheduled for this day.</div>
        <% } else { %>
          <div class="table-responsive">
            <table class="table">
              <thead>
                <tr>
                  <th>Time Slot</th>
                  <th>Subject</th>
                  <th>Faculty</th>
                  <th>Room</th>
                  <th style="text-align: right;">Action</th>
                </tr>
              </thead>
              <tbody>
                <% for (TimetableSlot slot : slots) { %>
                  <tr>
                    <td style="font-weight: 600; color: #93c5fd; white-space: nowrap;"><%= slot.getFormattedTime() %></td>
                    <td style="font-weight: 500;"><%= slot.getSubjectName() %></td>
                    <td style="color: var(--text-muted);"><%= slot.getFacultyName() %></td>
                    <td style="color: var(--text-dim);"><%= slot.getRoomNumber() %></td>
                    <td style="text-align: right;">
                      <a href="${pageContext.request.contextPath}/propose?slotId=<%= slot.getId() %>" class="btn btn-secondary btn-sm">
                        Propose Bunk &rarr;
                      </a>
                    </td>
                  </tr>
                <% } %>
              </tbody>
            </table>
          </div>
        <% } %>

      </div>
    <% } %>
  </div>

</div>

<jsp:include page="common/footer.jsp"/>
