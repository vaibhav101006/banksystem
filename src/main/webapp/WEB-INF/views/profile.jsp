<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.scbs.model.Student" %>
<%
  Student user = (Student) request.getAttribute("user");
  request.setAttribute("pageTitle", "Profile & Biometrics — SCBS");
%>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/navbar.jsp"/>

<div class="container" style="max-width: 860px;">

  <% if (request.getParameter("msg") != null) { %>
    <div class="alert alert-success"><%= request.getParameter("msg") %></div>
  <% } %>
  <% if (request.getParameter("error") != null) { %>
    <div class="alert alert-error"><%= request.getParameter("error") %></div>
  <% } %>

  <div class="grid-2" style="align-items: start;">
    
    <!-- Account Information -->
    <div class="card">
      <h2 class="card-title" style="margin-bottom: 16px;">Student Profile</h2>
      
      <div style="display: flex; flex-direction: column; gap: 14px;">
        <div style="padding-bottom: 12px; border-bottom: 1px solid var(--border-color);">
          <div style="font-size: 0.8rem; color: var(--text-dim); text-transform: uppercase;">Full Name</div>
          <div style="font-weight: 600; font-size: 1.05rem;"><%= user.getFullName() %></div>
        </div>

        <div style="padding-bottom: 12px; border-bottom: 1px solid var(--border-color);">
          <div style="font-size: 0.8rem; color: var(--text-dim); text-transform: uppercase;">Roll Number</div>
          <div style="font-weight: 600; font-size: 1.05rem; color: #93c5fd;"><%= user.getRollNumber() %></div>
        </div>

        <div style="padding-bottom: 12px; border-bottom: 1px solid var(--border-color);">
          <div style="font-size: 0.8rem; color: var(--text-dim); text-transform: uppercase;">Email Address</div>
          <div style="font-weight: 500;"><%= user.getEmail() %></div>
        </div>

        <div style="padding-bottom: 12px; border-bottom: 1px solid var(--border-color);">
          <div style="font-size: 0.8rem; color: var(--text-dim); text-transform: uppercase;">Class Section</div>
          <div style="font-weight: 500;"><%= user.getClassName() != null ? user.getClassName() : "CSE-3A" %></div>
        </div>

        <div style="display: flex; gap: 16px; margin-top: 6px;">
          <div style="flex: 1; background: #0d1322; padding: 12px; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
            <div style="font-size: 0.75rem; color: var(--text-dim);">Credits</div>
            <div style="font-size: 1.3rem; font-weight: 700; color: #60a5fa;">⚡ <%= user.getBunkCredits() %></div>
          </div>
          <div style="flex: 1; background: #0d1322; padding: 12px; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
            <div style="font-size: 0.75rem; color: var(--text-dim);">Reliability</div>
            <div style="font-size: 1.3rem; font-weight: 700; color: #34d399;">🛡️ <%= user.getReliabilityScore() %>%</div>
          </div>
        </div>

      </div>
    </div>

    <!-- Face Biometrics Enrollment -->
    <div class="card">
      <div class="card-header" style="margin-bottom: 10px;">
        <h2 class="card-title">Face Biometrics</h2>
        <% if (user.isFaceEnrolled()) { %>
          <span class="badge badge-passed">ENROLLED</span>
        <% } else { %>
          <span class="badge badge-rejected">NOT ENROLLED</span>
        <% } %>
      </div>

      <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 16px;">
        <%= user.isFaceEnrolled() 
            ? "Your face descriptor is registered and active for voting. You can re-enroll below at any time." 
            : "Enroll your face now to unlock proposing bunks and participating in live consensus voting." %>
      </p>

      <div class="camera-box" style="height: 240px; display: flex; align-items: center; justify-content: center; background: #000;">
        <video id="profileCamera" autoplay playsinline muted style="width: 100%; height: 100%; object-fit: cover;"></video>
        <div class="face-guide"></div>
      </div>
      <canvas id="profileCanvas" style="display: none;"></canvas>

      <form id="enrollForm" action="${pageContext.request.contextPath}/profile" method="POST" style="margin-top: 18px;">
        <input type="hidden" name="action" value="enrollFace">
        <input type="hidden" id="enrollFaceDescriptor" name="faceDescriptor" value="">
        
        <button type="button" class="btn btn-primary btn-block" onclick="captureAndEnrollFace()">
          📸 <%= user.isFaceEnrolled() ? "Re-Enroll Facial Embedding" : "Enroll Face Descriptor" %>
        </button>
      </form>

    </div>

  </div>

</div>

<script>
  window.addEventListener('DOMContentLoaded', () => {
    startCamera('profileCamera');
  });

  function captureAndEnrollFace() {
    const desc = captureDescriptor('profileCamera', 'profileCanvas');
    document.getElementById('enrollFaceDescriptor').value = desc;
    document.getElementById('enrollForm').submit();
  }
</script>

<jsp:include page="common/footer.jsp"/>
