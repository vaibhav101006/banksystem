<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.scbs.model.Student" %>
<%
  Student user = (Student) session.getAttribute("currentUser");
%>
<nav class="navbar">
  <div class="nav-container">
    <a href="${pageContext.request.contextPath}/dashboard" class="brand">
      <div class="brand-icon">
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
          <path d="m9 12 2 2 4-4"/>
        </svg>
      </div>
      <span>SCBS</span>
    </a>

    <% if (user != null) { %>
    <div class="nav-links">
      <a href="${pageContext.request.contextPath}/dashboard" class="nav-link">Dashboard</a>
      <a href="${pageContext.request.contextPath}/timetable" class="nav-link">Timetable</a>
      <a href="${pageContext.request.contextPath}/propose" class="nav-link">Propose Bunk</a>
      <a href="${pageContext.request.contextPath}/profile" class="nav-link">Profile</a>
      <% if (user.isAdmin()) { %>
        <a href="${pageContext.request.contextPath}/admin/approvals" class="nav-link" style="color: #60a5fa;">Admin Console</a>
      <% } %>
    </div>

    <div class="nav-user">
      <div class="credit-pill" title="Available Bunk Credits">
        ⚡ <span><%= user.getBunkCredits() %> Credits</span>
      </div>
      <div style="font-size: 0.85rem; color: var(--text-muted);">
        <%= user.getFullName() %> (<%= user.getRollNumber() %>)
      </div>
      <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Logout</a>
    </div>
    <% } %>
  </div>
</nav>
