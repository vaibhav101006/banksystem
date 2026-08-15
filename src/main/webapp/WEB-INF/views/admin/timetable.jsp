<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.scbs.model.ClassSection, com.scbs.model.TimetableSlot" %>
<%
  List<ClassSection> classes = (List<ClassSection>) request.getAttribute("classes");
  String selectedClassId = (String) request.getAttribute("selectedClassId");
  List<TimetableSlot> slots = (List<TimetableSlot>) request.getAttribute("slots");
  request.setAttribute("pageTitle", "Admin Timetable — SCBS");
%>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="../common/navbar.jsp"/>

<div class="container">

  <!-- Admin Sub Navigation -->
  <div style="display: flex; gap: 8px; margin-bottom: 24px; border-bottom: 1px solid var(--border-color); padding-bottom: 12px; flex-wrap: wrap;">
    <a href="${pageContext.request.contextPath}/admin/approvals" class="nav-link">Pending Approvals</a>
    <a href="${pageContext.request.contextPath}/admin/users" class="nav-link">Student Management</a>
    <a href="${pageContext.request.contextPath}/admin/timetable" class="nav-link active">Timetable Editor</a>
    <a href="${pageContext.request.contextPath}/admin/classes" class="nav-link">Class Sections</a>
    <a href="${pageContext.request.contextPath}/admin/proposals" class="nav-link">Proposals Audit</a>
  </div>

  <div class="grid-3" style="grid-template-columns: 2fr 1fr; align-items: start;">
    
    <!-- Slots Table -->
    <div class="card">
      <div class="card-header">
        <div>
          <h2 class="card-title">Schedule Slots</h2>
          <div class="card-subtitle">Manage timetable lecture periods for classes</div>
        </div>

        <!-- Class Filter -->
        <form action="${pageContext.request.contextPath}/admin/timetable" method="GET">
          <select name="classId" class="form-control" style="width: auto;" onchange="this.form.submit()">
            <% if (classes != null) {
                for (ClassSection cs : classes) {
            %>
              <option value="<%= cs.getId() %>" <%= cs.getId().equals(selectedClassId) ? "selected" : "" %>>
                <%= cs.getName() %> (<%= cs.getSemester() %>)
              </option>
            <%   }
              } %>
          </select>
        </form>
      </div>

      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th>Day</th>
              <th>Time</th>
              <th>Subject</th>
              <th>Faculty</th>
              <th>Room</th>
              <th style="text-align: right;">Action</th>
            </tr>
          </thead>
          <tbody>
            <% if (slots == null || slots.isEmpty()) { %>
              <tr>
                <td colspan="6" style="text-align: center; color: var(--text-dim); padding: 24px;">No lecture slots defined for this class.</td>
              </tr>
            <% } else {
                for (TimetableSlot slot : slots) {
            %>
              <tr>
                <td style="font-weight: 600;"><%= slot.getDayOfWeek() %></td>
                <td style="color: #93c5fd;"><%= slot.getFormattedTime() %></td>
                <td style="font-weight: 500;"><%= slot.getSubjectName() %></td>
                <td style="color: var(--text-muted);"><%= slot.getFacultyName() %></td>
                <td style="color: var(--text-dim);"><%= slot.getRoomNumber() %></td>
                <td style="text-align: right;">
                  <form action="${pageContext.request.contextPath}/admin/timetable" method="POST" onsubmit="return confirm('Delete slot?');">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="slotId" value="<%= slot.getId() %>">
                    <input type="hidden" name="classId" value="<%= selectedClassId %>">
                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                  </form>
                </td>
              </tr>
            <%   }
              } %>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Add New Slot Form -->
    <div class="card">
      <h3 class="card-title" style="margin-bottom: 16px;">Add Lecture Slot</h3>
      
      <form action="${pageContext.request.contextPath}/admin/timetable" method="POST">
        <input type="hidden" name="action" value="create">
        <input type="hidden" name="classId" value="<%= selectedClassId %>">

        <div class="form-group">
          <label class="form-label" for="dayOfWeek">Day of Week</label>
          <select id="dayOfWeek" name="dayOfWeek" class="form-control" required>
            <option value="MONDAY">Monday</option>
            <option value="TUESDAY">Tuesday</option>
            <option value="WEDNESDAY">Wednesday</option>
            <option value="THURSDAY">Thursday</option>
            <option value="FRIDAY">Friday</option>
            <option value="SATURDAY">Saturday</option>
          </select>
        </div>

        <div class="grid-2">
          <div class="form-group">
            <label class="form-label" for="startTime">Start Time</label>
            <input type="time" id="startTime" name="startTime" class="form-control" required value="09:00">
          </div>
          <div class="form-group">
            <label class="form-label" for="endTime">End Time</label>
            <input type="time" id="endTime" name="endTime" class="form-control" required value="10:00">
          </div>
        </div>

        <div class="form-group">
          <label class="form-label" for="subjectName">Subject Name</label>
          <input type="text" id="subjectName" name="subjectName" class="form-control" required placeholder="e.g. Distributed Systems">
        </div>

        <div class="form-group">
          <label class="form-label" for="facultyName">Faculty Name</label>
          <input type="text" id="facultyName" name="facultyName" class="form-control" placeholder="e.g. Dr. Leslie Lamport">
        </div>

        <div class="form-group">
          <label class="form-label" for="roomNumber">Room / Lab</label>
          <input type="text" id="roomNumber" name="roomNumber" class="form-control" placeholder="e.g. Hall 304">
        </div>

        <button type="submit" class="btn btn-primary btn-block">+ Add Timetable Slot</button>
      </form>
    </div>

  </div>

</div>

<jsp:include page="../common/footer.jsp"/>
