<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.scbs.model.Student, com.scbs.model.BunkProposal, com.scbs.model.TimetableSlot" %>
<%
  Student user = (Student) request.getAttribute("user");
  List<BunkProposal> proposals = (List<BunkProposal>) request.getAttribute("proposals");
  TimetableSlot activeSlot = (TimetableSlot) request.getAttribute("activeSlot");
  request.setAttribute("pageTitle", "Dashboard — SCBS");
%>
<jsp:include page="common/header.jsp"/>
<jsp:include page="common/navbar.jsp"/>

<div class="container">

  <!-- Face Enrollment Banner if missing -->
  <% if (!user.isFaceEnrolled()) { %>
  <div class="alert alert-info" style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 12px;">
    <div>
      <strong>⚠️ Biometrics Pending:</strong> You haven't enrolled your facial descriptor. Face enrollment is required to propose bunks and cast votes.
    </div>
    <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary btn-sm">Enroll Face Now</a>
  </div>
  <% } %>

  <!-- Top Metrics Bar -->
  <div class="grid-4" style="margin-bottom: 24px;">
    
    <div class="card">
      <div style="font-size: 0.8rem; color: var(--text-dim); text-transform: uppercase; font-weight: 600;">Bunk Credits</div>
      <div style="font-size: 1.8rem; font-weight: 700; color: #60a5fa; margin-top: 4px;">⚡ <%= user.getBunkCredits() %></div>
      <div style="font-size: 0.78rem; color: var(--text-muted); margin-top: 4px;">Cost: 10 per proposal, +5 on vote</div>
    </div>

    <div class="card">
      <div style="font-size: 0.8rem; color: var(--text-dim); text-transform: uppercase; font-weight: 600;">Reliability Score</div>
      <div style="font-size: 1.8rem; font-weight: 700; color: #34d399; margin-top: 4px;">🛡️ <%= user.getReliabilityScore() %>%</div>
      <div style="font-size: 0.78rem; color: var(--text-muted); margin-top: 4px;">Based on voting &amp; attendance honesty</div>
    </div>

    <div class="card">
      <div style="font-size: 0.8rem; color: var(--text-dim); text-transform: uppercase; font-weight: 600;">Class Section</div>
      <div style="font-size: 1.8rem; font-weight: 700; color: var(--text-main); margin-top: 4px;">🏫 <%= user.getClassName() != null ? user.getClassName() : "CSE-3A" %></div>
      <div style="font-size: 0.78rem; color: var(--text-muted); margin-top: 4px;">Roll No: <%= user.getRollNumber() %></div>
    </div>

    <div class="card">
      <div style="font-size: 0.8rem; color: var(--text-dim); text-transform: uppercase; font-weight: 600;">Active Today</div>
      <div style="font-size: 1.1rem; font-weight: 600; color: #f59e0b; margin-top: 8px;">
        <%= activeSlot != null ? activeSlot.getSubjectName() : "No ongoing class" %>
      </div>
      <div style="font-size: 0.78rem; color: var(--text-muted); margin-top: 4px;">
        <%= activeSlot != null ? (activeSlot.getFormattedTime() + " &bull; " + activeSlot.getRoomNumber()) : "Relax or review schedule" %>
      </div>
    </div>

  </div>

  <!-- Main Content Grid -->
  <div class="grid-3" style="grid-template-columns: 2fr 1fr; align-items: start;">
    
    <!-- Active & Recent Bunk Proposals -->
    <div class="card">
      <div class="card-header">
        <div>
          <h2 class="card-title">Collective Bunk Proposals</h2>
          <div class="card-subtitle">Active consensus votes and quorum status for <%= user.getClassName() %></div>
        </div>
        <a href="${pageContext.request.contextPath}/propose" class="btn btn-primary btn-sm">+ Propose Bunk</a>
      </div>

      <% if (proposals == null || proposals.isEmpty()) { %>
        <div style="text-align: center; padding: 48px 20px; color: var(--text-muted);">
          <div style="font-size: 2rem; margin-bottom: 8px;">🏝️</div>
          <div style="font-weight: 500;">No active bunk proposals right now.</div>
          <div style="font-size: 0.82rem; margin-top: 4px;">Have a busy schedule or lab workload? Propose a mass bunk to your classmates!</div>
          <a href="${pageContext.request.contextPath}/propose" class="btn btn-secondary btn-sm" style="margin-top: 16px;">Create First Proposal</a>
        </div>
      <% } else { %>
        <div style="display: flex; flex-direction: column; gap: 14px;">
          <% for (BunkProposal p : proposals) { %>
            <div style="background-color: #0d1322; border: 1px solid var(--border-color); border-radius: var(--radius-md); padding: 18px;">
              <div style="display: flex; justify-content: space-between; align-items: flex-start; gap: 12px;">
                <div>
                  <div style="display: flex; align-items: center; gap: 8px; flex-wrap: wrap;">
                    <span class="badge badge-<%= p.getStatus().toLowerCase() %>"><%= p.getStatus() %></span>
                    <strong style="font-size: 1.05rem;"><%= p.getSlot() != null ? p.getSlot().getSubjectName() : "Class Slot" %></strong>
                  </div>
                  <div style="font-size: 0.83rem; color: var(--text-muted); margin-top: 4px;">
                    📅 <strong><%= p.getBunkDate() %></strong> &bull; ⏰ <%= p.getSlot() != null ? p.getSlot().getFormattedTime() : "" %> &bull; 👨‍🏫 <%= p.getSlot() != null ? p.getSlot().getFacultyName() : "" %>
                  </div>
                  <div style="font-size: 0.85rem; color: #d1d5db; margin-top: 8px; background: rgba(255,255,255,0.02); padding: 8px 12px; border-radius: var(--radius-sm); border-left: 3px solid var(--primary);">
                    "<%= p.getReason() %>" &mdash; <span style="color: var(--text-dim); font-size: 0.78rem;">by <%= p.getCreatorName() %></span>
                  </div>
                </div>

                <a href="${pageContext.request.contextPath}/proposal?id=<%= p.getId() %>" class="btn btn-secondary btn-sm" style="white-space: nowrap;">
                  Vote &amp; Details &rarr;
                </a>
              </div>

              <!-- Quorum Progress -->
              <div style="margin-top: 16px;">
                <div style="display: flex; justify-content: space-between; font-size: 0.8rem; margin-bottom: 6px;">
                  <span>Quorum: <strong><%= p.getQuorumPercentage() %>%</strong> (<%= p.getTotalVotes() %> / <%= p.getClassTotalStudents() %> students)</span>
                  <span>Approval: <strong style="color: #34d399;"><%= p.getApprovalPercentage() %>% Agree</strong></span>
                </div>
                <div class="progress-container">
                  <div class="progress-bar progress-success" style="width: <%= p.getApprovalPercentage() %>%;"></div>
                </div>
              </div>
            </div>
          <% } %>
        </div>
      <% } %>
    </div>

    <!-- Right Sidebar Quick Info -->
    <div style="display: flex; flex-direction: column; gap: 20px;">
      
      <div class="card">
        <h3 class="card-title" style="font-size: 1rem; margin-bottom: 12px;">⚡ SCBS Rules &amp; Consensus</h3>
        <ul style="font-size: 0.83rem; color: var(--text-muted); list-style: none; display: flex; flex-direction: column; gap: 10px;">
          <li>🔒 <strong>Quorum Requirement:</strong> At least <strong>60%</strong> of the section must vote.</li>
          <li>🎯 <strong>Supermajority:</strong> At least <strong>75%</strong> of votes must be "Agree" for a proposal to pass.</li>
          <li>🤳 <strong>Biometric Verification:</strong> Every vote requires live webcam face confirmation.</li>
          <li>🪙 <strong>Bunk Credits:</strong> You earn <strong>+5 credits</strong> for every vote cast.</li>
        </ul>
      </div>

      <div class="card">
        <h3 class="card-title" style="font-size: 1rem; margin-bottom: 12px;">📅 Quick Actions</h3>
        <div style="display: flex; flex-direction: column; gap: 10px;">
          <a href="${pageContext.request.contextPath}/timetable" class="btn btn-secondary btn-block" style="justify-content: flex-start;">
            📅 View Weekly Timetable
          </a>
          <a href="${pageContext.request.contextPath}/propose" class="btn btn-primary btn-block" style="justify-content: flex-start;">
            ➕ Propose Mass Absence
          </a>
          <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary btn-block" style="justify-content: flex-start;">
            👤 Manage Face Biometrics
          </a>
        </div>
      </div>

    </div>

  </div>

</div>

<jsp:include page="common/footer.jsp"/>
