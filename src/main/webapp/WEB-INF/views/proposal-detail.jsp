<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.scbs.model.Student, com.scbs.model.BunkProposal, com.scbs.model.BunkVote" %>
<%
  BunkProposal proposal = (BunkProposal) request.getAttribute("proposal");
  List<BunkVote> votes = (List<BunkVote>) request.getAttribute("votes");
  BunkVote userVote = (BunkVote) request.getAttribute("userVote");
  Student user = (Student) request.getAttribute("user");
  request.setAttribute("pageTitle", "Proposal Vote — SCBS");
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

  <!-- Main Proposal Header Card -->
  <div class="card" style="margin-bottom: 24px;">
    <div style="display: flex; justify-content: space-between; align-items: flex-start; gap: 16px; flex-wrap: wrap;">
      <div>
        <div style="display: flex; align-items: center; gap: 8px; margin-bottom: 6px;">
          <span class="badge badge-<%= proposal.getStatus().toLowerCase() %>"><%= proposal.getStatus() %></span>
          <span style="font-size: 0.85rem; color: var(--text-muted);">Section <%= proposal.getClassName() %></span>
        </div>
        <h1 style="font-size: 1.6rem; font-weight: 700; letter-spacing: -0.02em;">
          <%= proposal.getSlot() != null ? proposal.getSlot().getSubjectName() : "Class Slot" %>
        </h1>
        <div style="font-size: 0.9rem; color: var(--text-muted); margin-top: 4px;">
          📅 <strong><%= proposal.getBunkDate() %></strong> &bull; ⏰ <%= proposal.getSlot() != null ? proposal.getSlot().getFormattedTime() : "" %> &bull; 👨‍🏫 <%= proposal.getSlot() != null ? proposal.getSlot().getFacultyName() : "" %>
        </div>
      </div>

      <div style="text-align: right;">
        <div style="font-size: 0.8rem; color: var(--text-dim);">Proposed by</div>
        <div style="font-weight: 600; font-size: 0.95rem; color: #93c5fd;"><%= proposal.getCreatorName() %></div>
        <div style="font-size: 0.78rem; color: var(--text-muted);"><%= proposal.getCreatorRoll() %></div>
      </div>
    </div>

    <!-- Reason Box -->
    <div style="margin-top: 18px; padding: 14px; background: #0d1322; border: 1px solid var(--border-color); border-radius: var(--radius-md);">
      <div style="font-size: 0.78rem; font-weight: 600; color: var(--text-dim); text-transform: uppercase; margin-bottom: 4px;">Proposal Justification</div>
      <div style="font-size: 0.92rem; color: var(--text-main);"><%= proposal.getReason() %></div>
    </div>

    <!-- Live Consensus & Quorum Breakdown -->
    <div class="grid-2" style="margin-top: 24px; padding-top: 20px; border-top: 1px solid var(--border-color);">
      
      <!-- Quorum Metric -->
      <div style="background: #0d1322; padding: 16px; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
          <span style="font-size: 0.85rem; font-weight: 600;">Participation Quorum</span>
          <span style="font-size: 0.85rem; color: <%= proposal.getQuorumPercentage() >= 60.0 ? "#34d399" : "#f59e0b" %>; font-weight: 700;">
            <%= proposal.getQuorumPercentage() %>% (Req: 60%)
          </span>
        </div>
        <div class="progress-container" style="height: 10px;">
          <div class="progress-bar progress-primary" style="width: <%= Math.min(100.0, proposal.getQuorumPercentage()) %>%;"></div>
        </div>
        <div style="font-size: 0.78rem; color: var(--text-dim); margin-top: 6px;">
          <%= proposal.getTotalVotes() %> out of <%= proposal.getClassTotalStudents() %> students voted
        </div>
      </div>

      <!-- Supermajority Approval Metric -->
      <div style="background: #0d1322; padding: 16px; border-radius: var(--radius-md); border: 1px solid var(--border-color);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;">
          <span style="font-size: 0.85rem; font-weight: 600;">Consensus Approval</span>
          <span style="font-size: 0.85rem; color: <%= proposal.getApprovalPercentage() >= 75.0 ? "#34d399" : "#f87171" %>; font-weight: 700;">
            <%= proposal.getApprovalPercentage() %>% Agree (Req: 75%)
          </span>
        </div>
        <div class="progress-container" style="height: 10px;">
          <div class="progress-bar progress-success" style="width: <%= proposal.getApprovalPercentage() %>%;"></div>
        </div>
        <div style="font-size: 0.78rem; color: var(--text-dim); margin-top: 6px;">
          <%= proposal.getAgreeVotes() %> Agree &bull; <%= proposal.getRejectVotes() %> Reject
        </div>
      </div>

    </div>

    <!-- Voting Action Panel -->
    <div style="margin-top: 24px; padding: 20px; background: rgba(59, 130, 246, 0.05); border: 1px solid rgba(59, 130, 246, 0.2); border-radius: var(--radius-md); text-align: center;">
      <% if (!proposal.isActive()) { %>
        <div style="font-weight: 600; color: var(--text-muted);">
          🔒 Voting is closed for this proposal (Status: <%= proposal.getStatus() %>)
        </div>
      <% } else if (userVote != null) { %>
        <div style="font-size: 1rem; font-weight: 600; color: <%= userVote.isChoice() ? "#34d399" : "#f87171" %>;">
          ✓ You have voted: <strong><%= userVote.isChoice() ? "AGREE (Mass Bunk)" : "REJECT (Attend Class)" %></strong>
        </div>
        <div style="font-size: 0.8rem; color: var(--text-dim); margin-top: 4px;">
          Verified on camera at <%= userVote.getVotedAt() %> &bull; +5 Bunk credits awarded
        </div>
      <% } else { %>
        <div style="font-size: 1.05rem; font-weight: 600; margin-bottom: 6px;">Cast Your Verified Vote</div>
        <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 16px;">
          Your vote requires live camera biometric verification. Proxy votes are strictly disallowed.
        </p>
        <div style="display: flex; gap: 14px; justify-content: center; max-width: 420px; margin: 0 auto;">
          <button type="button" class="btn btn-success" style="flex: 1; padding: 12px;" onclick="openVotingModal('agree')">
            👍 Agree (Bunk)
          </button>
          <button type="button" class="btn btn-danger" style="flex: 1; padding: 12px;" onclick="openVotingModal('reject')">
            👎 Reject (Attend)
          </button>
        </div>
      <% } %>
    </div>

  </div>

  <!-- Votes Audit Log Table -->
  <div class="card">
    <div class="card-header">
      <h3 class="card-title" style="font-size: 1.1rem;">Verified Votes Activity (<%= votes != null ? votes.size() : 0 %>)</h3>
    </div>
    
    <% if (votes == null || votes.isEmpty()) { %>
      <div style="text-align: center; color: var(--text-dim); padding: 20px 0;">No votes recorded yet.</div>
    <% } else { %>
      <div class="table-responsive">
        <table class="table">
          <thead>
            <tr>
              <th>Student Name</th>
              <th>Roll Number</th>
              <th>Vote Decision</th>
              <th>Biometrics Status</th>
              <th>Timestamp</th>
            </tr>
          </thead>
          <tbody>
            <% for (BunkVote v : votes) { %>
              <tr>
                <td style="font-weight: 500;"><%= v.getStudentName() %></td>
                <td style="color: var(--text-muted);"><%= v.getStudentRoll() %></td>
                <td>
                  <% if (v.isChoice()) { %>
                    <span class="badge badge-passed">AGREE</span>
                  <% } else { %>
                    <span class="badge badge-rejected">REJECT</span>
                  <% } %>
                </td>
                <td style="color: #34d399; font-size: 0.8rem;">🛡️ Face Verified</td>
                <td style="color: var(--text-dim); font-size: 0.8rem;"><%= v.getVotedAt() %></td>
              </tr>
            <% } %>
          </tbody>
        </table>
      </div>
    <% } %>
  </div>

</div>

<!-- Biometric Voting Camera Modal -->
<div id="votingModal" class="modal-overlay">
  <div class="modal-dialog">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
      <h3 id="voteChoiceLabel" style="font-size: 1.15rem; font-weight: 700;">Biometric Verification</h3>
      <button type="button" style="background: none; border: none; color: var(--text-muted); cursor: pointer; font-size: 1.2rem;" onclick="closeVotingModal()">&times;</button>
    </div>

    <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 14px;">
      Look at the camera. Your live facial embedding will be matched with your enrolled student profile descriptor.
    </p>

    <div class="camera-box" style="height: 240px; display: flex; align-items: center; justify-content: center; background: #000;">
      <video id="voteCamera" autoplay playsinline muted style="width: 100%; height: 100%; object-fit: cover;"></video>
      <div class="face-guide"></div>
    </div>
    <canvas id="voteCanvas" style="display: none;"></canvas>

    <form id="voteForm" action="${pageContext.request.contextPath}/vote" method="POST">
      <input type="hidden" name="proposalId" value="<%= proposal.getId() %>">
      <input type="hidden" id="voteChoiceInput" name="choice" value="">
      <input type="hidden" id="liveFaceDescriptorInput" name="faceDescriptor" value="">
    </form>

    <div style="display: flex; gap: 12px; margin-top: 20px;">
      <button type="button" class="btn btn-secondary" style="flex: 1;" onclick="closeVotingModal()">Cancel</button>
      <button type="button" class="btn btn-primary" style="flex: 2;" onclick="confirmVoteWithBiometrics()">
        📸 Verify Face &amp; Submit Vote
      </button>
    </div>
  </div>
</div>

<jsp:include page="common/footer.jsp"/>
