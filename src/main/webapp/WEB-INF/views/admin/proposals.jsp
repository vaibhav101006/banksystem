<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.scbs.model.BunkProposal" %>
<%
  List<BunkProposal> proposals = (List<BunkProposal>) request.getAttribute("proposals");
  request.setAttribute("pageTitle", "Admin Proposals — SCBS");
%>
<jsp:include page="../common/header.jsp"/>
<jsp:include page="../common/navbar.jsp"/>

<div class="container">

  <!-- Admin Sub Navigation -->
  <div style="display: flex; gap: 8px; margin-bottom: 24px; border-bottom: 1px solid var(--border-color); padding-bottom: 12px; flex-wrap: wrap;">
    <a href="${pageContext.request.contextPath}/admin/approvals" class="nav-link">Pending Approvals</a>
    <a href="${pageContext.request.contextPath}/admin/users" class="nav-link">Student Management</a>
    <a href="${pageContext.request.contextPath}/admin/timetable" class="nav-link">Timetable Editor</a>
    <a href="${pageContext.request.contextPath}/admin/classes" class="nav-link">Class Sections</a>
    <a href="${pageContext.request.contextPath}/admin/proposals" class="nav-link active">Proposals Audit (<%= proposals != null ? proposals.size() : 0 %>)</a>
  </div>

  <% if (request.getParameter("msg") != null) { %>
    <div class="alert alert-success"><%= request.getParameter("msg") %></div>
  <% } %>

  <div class="card">
    <div class="card-header">
      <div>
        <h2 class="card-title">All Bunk Proposals Audit Log</h2>
        <div class="card-subtitle">Comprehensive record of all active and past collective bunk voting outcomes</div>
      </div>
    </div>

    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th>Status</th>
            <th>Class</th>
            <th>Subject</th>
            <th>Date &amp; Slot</th>
            <th>Proposer</th>
            <th>Votes (A/R)</th>
            <th>Quorum</th>
            <th>Approval</th>
            <th style="text-align: right;">Actions</th>
          </tr>
        </thead>
        <tbody>
          <% if (proposals != null) {
              for (BunkProposal p : proposals) {
          %>
            <tr>
              <td>
                <span class="badge badge-<%= p.getStatus().toLowerCase() %>"><%= p.getStatus() %></span>
              </td>
              <td style="font-weight: 500;"><%= p.getClassName() %></td>
              <td style="font-weight: 600;"><%= p.getSlot() != null ? p.getSlot().getSubjectName() : "Slot" %></td>
              <td style="font-size: 0.85rem; color: var(--text-muted);">
                <%= p.getBunkDate() %><br>
                <span style="color: var(--text-dim);"><%= p.getSlot() != null ? p.getSlot().getFormattedTime() : "" %></span>
              </td>
              <td style="font-size: 0.85rem;">
                <%= p.getCreatorName() %><br>
                <span style="color: var(--text-dim); font-size: 0.78rem;"><%= p.getCreatorRoll() %></span>
              </td>
              <td style="font-weight: 600;">
                <span style="color: #34d399;"><%= p.getAgreeVotes() %></span> / <span style="color: #f87171;"><%= p.getRejectVotes() %></span>
              </td>
              <td><%= p.getQuorumPercentage() %>%</td>
              <td style="font-weight: 600; color: <%= p.getApprovalPercentage() >= 75.0 ? "#34d399" : "#f87171" %>;">
                <%= p.getApprovalPercentage() %>%
              </td>
              <td style="text-align: right;">
                <div style="display: inline-flex; gap: 6px;">
                  <a href="${pageContext.request.contextPath}/proposal?id=<%= p.getId() %>" class="btn btn-secondary btn-sm">
                    View
                  </a>
                  <% if (p.isActive()) { %>
                    <form action="${pageContext.request.contextPath}/admin/proposals" method="POST">
                      <input type="hidden" name="proposalId" value="<%= p.getId() %>">
                      <input type="hidden" name="status" value="PASSED">
                      <button type="submit" class="btn btn-success btn-sm">Pass</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/admin/proposals" method="POST">
                      <input type="hidden" name="proposalId" value="<%= p.getId() %>">
                      <input type="hidden" name="status" value="REJECTED">
                      <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                    </form>
                  <% } %>
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
