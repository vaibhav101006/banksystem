# SCBS — Smart Collective Bunk System (Java Fullstack)

A secure, full-stack student attendance coordination and mass bunk voting platform written entirely in **Java (JDBC, Servlets, and JSP)**.

---

## 🛠️ Technology Stack

* **Backend & Controllers**: Java Servlets (`HttpServlet`, `doGet`, `doPost`), Servlet Filters (`AuthenticationFilter`, `AdminFilter`), and `ServletContextListener`.
* **Frontend Views**: JavaServer Pages (`.jsp`), JSTL & Expression Language (EL), Modern Responsive CSS3 design system, and Biometric Web Camera interface.
* **Persistence & Database**: Pure JDBC (`PreparedStatement`, `ResultSet`, `Connection`) with Embedded H2 Database (auto-initialized and pre-seeded on startup).
* **Biometrics Engine**: Client-side face landmark/feature embedding vector extraction and Java-side Euclidean similarity verification.
* **Server**: Embedded Tomcat / Standalone Java Runner or any standard Servlet 3.1+ Container (Tomcat 8/9/10, Jetty, GlassFish).

---

## 🔑 Pre-Seeded Demo Accounts

| Role | Email | Password | Roll Number | Notes |
|---|---|---|---|---|
| **Admin** | `admin@scbs.edu` | `admin123` | `ADMIN-001` | Full administrative access to approvals, user management, timetable editor, and proposal audits. |
| **Student** | `alex@scbs.edu` | `student123` | `CSE-2024-001` | Pre-approved student with 120 Bunk Credits and enrolled face biometrics. |
| **Student** | `sarah@scbs.edu` | `student123` | `CSE-2024-002` | Pre-approved student with 95 Bunk Credits and enrolled face biometrics. |
| **Student** | `jordan@scbs.edu` | `student123` | `CSE-2024-003` | Pre-approved student (Face enrollment pending). |
| **Student** | `liam@scbs.edu` | `student123` | `CSE-2024-004` | Registration pending approval in Admin console. |

---

## 🚀 How to Run

### Method 1: Standalone Maven / Embedded Runner
```bash
cd scbs-jsp-servlet
mvn clean compile exec:java -Dexec.mainClass="com.scbs.StandaloneServer"
```
Or package as a standard `.war` file:
```bash
mvn clean package
```

### Method 2: Deploy to any standard Tomcat server
Deploy the generated `target/scbs.war` into your Tomcat `webapps/` folder and visit `http://localhost:8080/scbs/login`.

---

## 🗺️ Application Architecture

```
scbs-jsp-servlet/
├── src/main/java/com/scbs/
│   ├── model/                          // Data Models (Student, ClassSection, TimetableSlot, BunkProposal, BunkVote)
│   ├── dao/                            // Pure JDBC Data Access Objects (DBConnectionManager, StudentDAO, ...)
│   ├── service/                        // Consensus, Reputation, and Biometric verification logic
│   ├── filter/                         // Authentication & Admin Security Filters
│   ├── servlet/                        // Java Controllers (Auth, Dashboard, Timetable, Propose, Vote, Admin...)
│   └── listener/                       // AppContextListener (Auto DB Schema & Seed on Startup)
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── web.xml                     // Web Application Descriptor
│   │   └── views/                      // Protected JSP Views
│   │       ├── auth/                   // login.jsp, register.jsp
│   │       ├── dashboard.jsp           // Metric Cards & Active Proposals
│   │       ├── timetable.jsp           // Weekly Schedule Grid
│   │       ├── propose.jsp             // Proposal Creation with Face Check
│   │       ├── proposal-detail.jsp     // Live Consensus Gauge & Voting Modal
│   │       ├── profile.jsp             // Face Enrollment & Student Stats
│   │       └── admin/                  // Approvals, Users, Timetable, Classes, Proposals
│   └── static/
│       ├── css/style.css               // Polished Modern UI Theme
│       └── js/biometrics.js            // Webcam Face Descriptor Extraction
└── pom.xml
```

---

## ⚡ Core Business Rules
1. **60% Quorum**: A bunk proposal requires at least 60% student participation from the class section.
2. **75% Supermajority**: A proposal only passes if 75% or more of cast votes are "Agree".
3. **Biometric Face Verification**: Live camera checks ensure one student cannot cast a proxy vote for another.
4. **Credit Economics**: Proposing costs 10 credits; voting awards +5 credits.
