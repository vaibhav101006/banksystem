<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.scbs.model.Student" %>
<%
  List<Student> students = (List<Student>) request.getAttribute("students");
  request.setAttribute("pageTitle", "Admin Users — SCBS");
%>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="../common/navbar.jsp"/>

<div class="container">

  <!-- Admin Sub Navigation -->
  <div style="display: flex; gap: 8px; margin-bottom: 24px; border-bottom: 1px solid var(--border-color); padding-bottom: 12px; flex-wrap: wrap;">
    <a href="${pageContext.request.contextPath}/admin/approvals" class="nav-link">Pending Approvals</a>
    <a href="${pageContext.request.contextPath}/admin/users" class="nav-link active">Student Management (<%= students != null ? students.size() : 0 %>)</a>
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
        <h2 class="card-title">Registered Students &amp; Admins</h2>
        <div class="card-subtitle">Manage roles, reset credentials, and monitor student reliability metrics</div>
      </div>
    </div>

    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th>Roll No</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <th>Credits</th>
            <th>Reliability</th>
            <th>Face Status</th>
            <th style="text-align: right;">Admin Actions</th>
          </tr>
        </thead>
        <tbody>
          <% if (students != null) {
              for (Student s : students) {
          %>
            <tr>
              <td style="font-weight: 600; color: #93c5fd;"><%= s.getRollNumber() %></td>
              <td style="font-weight: 500;"><%= s.getFullName() %></td>
              <td style="font-size: 0.85rem;"><%= s.getEmail() %></td>
              <td>
                <span class="badge" style="<%= s.isAdmin() ? "background: rgba(139, 92, 246, 0.2); color: #c084fc; border: 1px solid rgba(139, 92, 246, 0.4);" : "background: rgba(255,255,255,0.05); color: var(--text-muted);" %>">
                  <%= s.getRole() %>
                </span>
              </td>
              <td>
                <span class="badge badge-<%= s.getApprovalStatus().toLowerCase() %>">
                  <%= s.getApprovalStatus() %>
                </span>
              </td>
              <td style="font-weight: 600; color: #60a5fa;"><%= s.getBunkCredits() %></td>
              <td style="font-weight: 600; color: #34d399;"><%= s.getReliabilityScore() %>%</td>
              <td>
                <%= s.isFaceEnrolled() ? "<span style='color:#34d399;'>✓ Enrolled</span>" : "<span style='color:var(--text-dim);'>Not set</span>" %>
              </td>
              <td style="text-align: right;">
                <div style="display: inline-flex; gap: 6px;">
                  
                  <!-- Role Toggle -->
                  <form action="${pageContext.request.contextPath}/admin/users" method="POST">
                    <input type="hidden" name="studentId" value="<%= s.getId() %>">
                    <input type="hidden" name="action" value="toggleAdmin">
                    <input type="hidden" name="newRole" value="<%= s.isAdmin() ? "STUDENT" : "ADMIN" %>">
                    <button type="submit" class="btn btn-secondary btn-sm" title="Toggle Admin Role">
                      <%= s.isAdmin() ? "Demote" : "Make Admin" %>
                    </button>
                  </form>

                  <!-- Password Reset -->
                  <form action="${pageContext.request.contextPath}/admin/users" method="POST" onsubmit="return confirm('Reset password to default (student123)?');">
                    <input type="hidden" name="studentId" value="<%= s.getId() %>">
                    <input type="hidden" name="action" value="resetPassword">
                    <button type="submit" class="btn btn-secondary btn-sm" title="Reset password to student123">
                      🔑 Reset
                    </button>
                  </form>

                </div>
              </td>
            </tr>
          <%   }
            } %>
        </tbody>
      </table>
    </div>
  </div>

</div>

<jsp:include page="../common/footer.jsp"/>
