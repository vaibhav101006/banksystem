<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.scbs.model.ClassSection" %>
<% request.setAttribute("pageTitle", "Register — SCBS"); %>
<jsp:include page="../common/header.jsp"/>

<div class="container" style="min-height: 80vh; display: flex; align-items: center; justify-content: center; padding: 40px 20px;">
  <div class="card" style="width: 100%; max-width: 480px;">
    
    <div style="text-align: center; margin-bottom: 24px;">
      <h2 style="font-size: 1.4rem; font-weight: 700; letter-spacing: -0.02em;">Student Registration</h2>
      <p style="color: var(--text-muted); font-size: 0.85rem; margin-top: 4px;">Join your class collective consensus network</p>
    </div>

    <% if (request.getAttribute("error") != null) { %>
      <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="${pageContext.request.contextPath}/register" method="POST">
      <div class="form-group">
        <label class="form-label" for="fullName">Full Name</label>
        <input type="text" id="fullName" name="fullName" class="form-control" required placeholder="e.g. Jordan Lee">
      </div>

      <div class="grid-2">
        <div class="form-group">
          <label class="form-label" for="rollNumber">Roll Number</label>
          <input type="text" id="rollNumber" name="rollNumber" class="form-control" required placeholder="CSE-2024-005">
        </div>
        <div class="form-group">
          <label class="form-label" for="classId">Class Section</label>
          <select id="classId" name="classId" class="form-control" required>
            <%
              List<ClassSection> classes = (List<ClassSection>) request.getAttribute("classes");
              if (classes != null) {
                for (ClassSection cs : classes) {
            %>
              <option value="<%= cs.getId() %>"><%= cs.getName() %> (<%= cs.getSemester() %>)</option>
            <%   }
              } %>
          </select>
        </div>
      </div>

      <div class="form-group">
        <label class="form-label" for="email">College Email</label>
        <input type="email" id="email" name="email" class="form-control" required placeholder="student@scbs.edu">
      </div>

      <div class="form-group">
        <label class="form-label" for="password">Create Password</label>
        <input type="password" id="password" name="password" class="form-control" required placeholder="••••••••">
      </div>

      <div style="background: rgba(255,255,255,0.02); border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 12px; font-size: 0.8rem; color: var(--text-muted); margin-bottom: 20px;">
        ℹ️ <strong>Admin Approval Notice:</strong> New student registrations are verified and approved by class administrators to prevent unauthorized access.
      </div>

      <button type="submit" class="btn btn-primary btn-block">Submit Registration</button>
    </form>

    <div style="margin-top: 20px; text-align: center; font-size: 0.85rem; color: var(--text-muted);">
      Already registered? <a href="${pageContext.request.contextPath}/login" style="color: var(--primary); font-weight: 500;">Sign In</a>
    </div>

  </div>
</div>

<jsp:include page="../common/footer.jsp"/>
