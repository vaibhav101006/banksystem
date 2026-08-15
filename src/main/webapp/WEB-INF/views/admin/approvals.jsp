<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.scbs.model.Student" %>
<%
  List<Student> pending = (List<Student>) request.getAttribute("pendingStudents");
  request.setAttribute("pageTitle", "Admin Approvals — SCBS");
%>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="../common/navbar.jsp"/>

<div class="container">

  <!-- Admin Sub Navigation -->
  <div style="display: flex; gap: 8px; margin-bottom: 24px; border-bottom: 1px solid var(--border-color); padding-bottom: 12px; flex-wrap: wrap;">
    <a href="${pageContext.request.contextPath}/admin/approvals" class="nav-link active">Pending Approvals (<%= pending != null ? pending.size() : 0 %>)</a>
    <a href="${pageContext.request.contextPath}/admin/users" class="nav-link">Student Management</a>
    <a href="${pageContext.request.contextPath}/admin/timetable" class="nav-link">Timetable Editor</a>
    <a href="${pageContext.request.contextPath}/admin/classes" class="nav-link">Class Sections</a>
    <a href="${pageContext.request.contextPath}/admin/proposals" class="nav-link">Proposals Audit</a>
  </div>

  <% if (request.getParameter("msg") != null) { %>
    <div class="alert alert-success"><%= request.getParameter("msg") %></div>
  <% } %>

  <div class="card">
    <div class="card-header">
      <div>
        <h2 class="card-title">Pending Student Registrations</h2>
        <div class="card-subtitle">Review and verify student identity before granting voting access</div>
      </div>
    </div>

    <% if (pending == null || pending.isEmpty()) { %>
      <div style="text-align: center; padding: 48px 20px; color: var(--text-muted);">
        <div style="font-size: 2.2rem; margin-bottom: 8px;">🎉</div>
        <div style="font-weight: 600; color: var(--text-main);">All Caught Up!</div>
        <div style="font-size: 0.85rem; margin-top: 4px;">There are currently no pending student registrations awaiting verification.</div>
      </div>
    <% } else { %>
      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th>Full Name</th>
              <th>Roll Number</th>
              <th>Email</th>
              <th>Section</th>
              <th>Registered At</th>
              <th style="text-align: right;">Action</th>
            </tr>
          </thead>
          <tbody>
            <% for (Student s : pending) { %>
              <tr>
                <td style="font-weight: 600;"><%= s.getFullName() %></td>
                <td style="color: #93c5fd;"><%= s.getRollNumber() %></td>
                <td><%= s.getEmail() %></td>
                <td><%= s.getClassName() != null ? s.getClassName() : "CSE-3A" %></td>
                <td style="color: var(--text-dim); font-size: 0.8rem;"><%= s.getCreatedAt() %></td>
                <td style="text-align: right;">
                  <div style="display: inline-flex; gap: 6px;">
                    <form action="${pageContext.request.contextPath}/admin/approvals" method="POST">
                      <input type="hidden" name="studentId" value="<%= s.getId() %>">
                      <input type="hidden" name="action" value="approve">
                      <button type="submit" class="btn btn-success btn-sm">Approve</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/admin/approvals" method="POST">
                      <input type="hidden" name="studentId" value="<%= s.getId() %>">
                      <input type="hidden" name="action" value="reject">
                      <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                    </form>
                  </div>
                </td>
              </tr>
            <% } %>
          </tbody>
        </table>
      </div>
    <% } %>
  </div>

</div>

<jsp:include page="../common/footer.jsp"/>
