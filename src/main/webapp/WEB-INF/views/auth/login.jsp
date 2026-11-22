<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Login — SCBS" scope="request"/>
<jsp:include page="../common/header.jsp"/>

<div class="container" style="min-height: 80vh; display: flex; align-items: center; justify-content: center;">
  <div class="card" style="width: 100%; max-width: 440px;">
    
    <div style="text-align: center; margin-bottom: 24px;">
      <div class="brand-icon" style="width: 48px; height: 48px; margin: 0 auto 12px;">
        <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
          <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
          <path d="m9 12 2 2 4-4"/>
        </svg>
      </div>
      <h2 style="font-size: 1.5rem; font-weight: 700; letter-spacing: -0.02em;">Welcome to SCBS</h2>
      <p style="color: var(--text-muted); font-size: 0.88rem; margin-top: 4px;">Smart Collective Bunk &amp; Attendance Consensus</p>
    </div>

    <!-- Server-Side Error & Status Alerts using JSTL and EL -->
    <c:if test="${not empty error}">
      <div class="alert alert-error">⚠️ <c:out value="${error}"/></div>
    </c:if>
    <c:if test="${not empty param.msg}">
      <div class="alert alert-success">✅ <c:out value="${param.msg}"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="POST" id="loginForm">
      <div class="form-group">
        <label class="form-label" for="email">Student / Admin Email</label>
        <input type="email" id="email" name="email" class="form-control" required placeholder="alex@scbs.edu" autocomplete="email">
      </div>

      <div class="form-group">
        <label class="form-label" for="password">Password</label>
        <input type="password" id="password" name="password" class="form-control" required minlength="4" placeholder="••••••••" autocomplete="current-password">
      </div>

      <button type="submit" class="btn btn-primary btn-block" style="margin-top: 8px;">Sign In</button>
    </form>

    <!-- Quick Demo Logins -->
    <div style="margin-top: 24px; padding-top: 18px; border-top: 1px solid var(--border-color);">
      <div style="font-size: 0.78rem; font-weight: 600; color: var(--text-dim); text-transform: uppercase; margin-bottom: 10px;">Quick Demo Accounts</div>
      <div style="display: flex; gap: 8px;">
        <button type="button" class="btn btn-secondary btn-sm" style="flex: 1;" onclick="fillLogin('alex@scbs.edu', 'student123')">Student (Alex)</button>
        <button type="button" class="btn btn-secondary btn-sm" style="flex: 1;" onclick="fillLogin('admin@scbs.edu', 'admin123')">Admin</button>
      </div>
    </div>

    <div style="margin-top: 20px; text-align: center; font-size: 0.85rem; color: var(--text-muted);">
      Don't have an account? <a href="${pageContext.request.contextPath}/register" style="color: var(--primary); font-weight: 500;">Register as Student</a>
    </div>

  </div>
</div>

<script>
  function fillLogin(email, password) {
    document.getElementById('email').value = email;
    document.getElementById('password').value = password;
  }
</script>

<jsp:include page="../common/footer.jsp"/>
