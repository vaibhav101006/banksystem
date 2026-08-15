<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.scbs.model.ClassSection" %>
<%
  List<ClassSection> classes = (List<ClassSection>) request.getAttribute("classes");
  request.setAttribute("pageTitle", "Admin Classes — SCBS");
%>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="../common/navbar.jsp"/>

<div class="container">

  <!-- Admin Sub Navigation -->
  <div style="display: flex; gap: 8px; margin-bottom: 24px; border-bottom: 1px solid var(--border-color); padding-bottom: 12px; flex-wrap: wrap;">
    <a href="${pageContext.request.contextPath}/admin/approvals" class="nav-link">Pending Approvals</a>
    <a href="${pageContext.request.contextPath}/admin/users" class="nav-link">Student Management</a>
    <a href="${pageContext.request.contextPath}/admin/timetable" class="nav-link">Timetable Editor</a>
    <a href="${pageContext.request.contextPath}/admin/classes" class="nav-link active">Class Sections (<%= classes != null ? classes.size() : 0 %>)</a>
    <a href="${pageContext.request.contextPath}/admin/proposals" class="nav-link">Proposals Audit</a>
  </div>

  <% if (request.getParameter("msg") != null) { %>
    <div class="alert alert-success"><%= request.getParameter("msg") %></div>
  <% } %>

  <div class="grid-3" style="grid-template-columns: 2fr 1fr; align-items: start;">
    
    <!-- Classes Table -->
    <div class="card">
      <div class="card-header">
        <div>
          <h2 class="card-title">Class Sections &amp; Cohorts</h2>
          <div class="card-subtitle">Define university classes, batches, and total student strength for quorum calculation</div>
        </div>
      </div>

      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th>Class Section</th>
              <th>Semester / Term</th>
              <th>Total Students (Quorum Base)</th>
              <th style="text-align: right;">Action</th>
            </tr>
          </thead>
          <tbody>
            <% if (classes != null) {
                for (ClassSection cs : classes) {
            %>
              <tr>
                <td style="font-weight: 600; font-size: 1rem;"><%= cs.getName() %></td>
                <td><%= cs.getSemester() %></td>
                <td style="font-weight: 600; color: #60a5fa;"><%= cs.getTotalStudents() %> Students</td>
                <td style="text-align: right;">
                  <form action="${pageContext.request.contextPath}/admin/classes" method="POST" onsubmit="return confirm('Delete class?');">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="classId" value="<%= cs.getId() %>">
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

    <!-- Create Class Form -->
    <div class="card">
      <h3 class="card-title" style="margin-bottom: 16px;">Add New Section</h3>
      
      <form action="${pageContext.request.contextPath}/admin/classes" method="POST">
        <input type="hidden" name="action" value="create">

        <div class="form-group">
          <label class="form-label" for="name">Class Section Code</label>
          <input type="text" id="name" name="name" class="form-control" required placeholder="e.g. CSE-4B">
        </div>

        <div class="form-group">
          <label class="form-label" for="semester">Semester / Term</label>
          <input type="text" id="semester" name="semester" class="form-control" required placeholder="e.g. 7th Semester">
        </div>

        <div class="form-group">
          <label class="form-label" for="totalStudents">Total Students Enrolled</label>
          <input type="number" id="totalStudents" name="totalStudents" class="form-control" required value="60" min="1">
          <span style="font-size: 0.75rem; color: var(--text-dim);">Used to calculate minimum 60% voting quorum.</span>
        </div>

        <button type="submit" class="btn btn-primary btn-block">+ Create Class Section</button>
      </form>
    </div>

  </div>

</div>

<jsp:include page="../common/footer.jsp"/>
