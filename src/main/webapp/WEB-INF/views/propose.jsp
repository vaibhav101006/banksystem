<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.scbs.model.Student, com.scbs.model.TimetableSlot, java.time.LocalDate" %>
<%
  Student user = (Student) request.getAttribute("user");
  List<TimetableSlot> slots = (List<TimetableSlot>) request.getAttribute("slots");
  String preselectedSlotId = request.getParameter("slotId");
  request.setAttribute("pageTitle", "Propose Bunk — SCBS");
%>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/navbar.jsp"/>

<div class="container" style="max-width: 680px;">
  
  <div class="card">
    <div style="margin-bottom: 20px;">
      <h1 class="card-title" style="font-size: 1.4rem;">Propose a Mass Bunk</h1>
      <p class="card-subtitle">Initiate a verified consensus vote for your class section</p>
    </div>

    <% if (request.getAttribute("error") != null) { %>
      <div class="alert alert-error"><%= request.getAttribute("error") %></div>
    <% } %>

    <!-- Notice on Credits & Biometrics -->
    <div style="background: rgba(59, 130, 246, 0.08); border: 1px solid rgba(59, 130, 246, 0.2); border-radius: var(--radius-md); padding: 14px; margin-bottom: 20px; font-size: 0.85rem; color: #93c5fd;">
      ⚡ <strong>Proposal Cost:</strong> 10 Bunk Credits will be deducted from your balance (<%= user.getBunkCredits() %> available).
    </div>

    <form id="proposeForm" action="${pageContext.request.contextPath}/propose" method="POST">
      
      <div class="form-group">
        <label class="form-label" for="slotId">Select Lecture / Subject</label>
        <select id="slotId" name="slotId" class="form-control" required>
          <option value="">-- Choose Timetable Slot --</option>
          <% if (slots != null) {
              for (TimetableSlot slot : slots) {
          %>
            <option value="<%= slot.getId() %>" <%= (preselectedSlotId != null && preselectedSlotId.equals(slot.getId())) ? "selected" : "" %>>
              <%= slot.getDayOfWeek() %> &bull; <%= slot.getFormattedTime() %> &bull; <%= slot.getSubjectName() %> (<%= slot.getFacultyName() %>)
            </option>
          <%   }
            } %>
        </select>
      </div>

      <div class="form-group">
        <label class="form-label" for="bunkDate">Bunk Date</label>
        <input type="date" id="bunkDate" name="bunkDate" class="form-control" required value="<%= LocalDate.now() %>" min="<%= LocalDate.now() %>">
      </div>

      <div class="form-group">
        <label class="form-label" for="reason">Collective Reason / Justification</label>
        <textarea id="reason" name="reason" rows="3" class="form-control" placeholder="e.g. Preparing for semester project deadline or college technical fest." required></textarea>
      </div>

      <!-- Biometric Verification Section -->
      <div style="border-top: 1px solid var(--border-color); padding-top: 18px; margin-top: 20px;">
        <label class="form-label" style="font-weight: 600; color: var(--text-main);">Biometric Confirmation</label>
        <p style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 12px;">Ensure your camera is enabled. Your live facial embedding verifies you are initiating this proposal.</p>
        
        <div class="camera-box" style="height: 220px; display: flex; align-items: center; justify-content: center; background: #000;">
          <video id="proposeCamera" autoplay playsinline muted style="width: 100%; height: 100%; object-fit: cover;"></video>
          <div class="face-guide"></div>
        </div>
        <canvas id="proposeCanvas" style="display: none;"></canvas>

        <input type="hidden" id="proposeFaceDescriptor" name="faceDescriptor" value="">
      </div>

      <div style="margin-top: 24px;">
        <button type="button" class="btn btn-primary btn-block" onclick="submitProposalWithFace()">
          🚀 Submit Proposal &amp; Open Live Voting
        </button>
      </div>

    </form>
  </div>

</div>

<script>
  window.addEventListener('DOMContentLoaded', () => {
    startCamera('proposeCamera');
  });

  function submitProposalWithFace() {
    const desc = captureDescriptor('proposeCamera', 'proposeCanvas');
    document.getElementById('proposeFaceDescriptor').value = desc;
    document.getElementById('proposeForm').submit();
  }
</script>

<jsp:include page="common/footer.jsp"/>
