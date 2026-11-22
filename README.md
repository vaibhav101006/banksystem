# Smart Collective Bunk System (SCBS)
### Advanced Java Technology (AJT – 01CE0308) | Final Project Submission & Deployment

**Student Name:** Vaibhav Rabadiya  
**Enrollment Number:** 92610120040  
**Department:** Department of Computer Engineering  
**Institution:** Faculty of Engineering and Technology, Marwadi University  
**GitHub Repository:** [https://github.com/vaibhav101006/banksystem](https://github.com/vaibhav101006/banksystem)  
**Live Deployed Application:** [https://banksystem-scbs.onrender.com](https://banksystem-scbs.onrender.com)  

---

## 📌 1. Project Title & Overview

The **Smart Collective Bunk System (SCBS)** is a secure, full-stack enterprise web application engineered with pure Java technologies (**Servlets 3.1+, JSP with JSTL/EL, JDBC, and Embedded H2 / PostgreSQL**). SCBS replaces chaotic group chats and attendance disputes with a mathematically sound, biometrically authenticated voting and consensus engine. 

### Key Capabilities:
- **Biometric Anti-Proxy Authentication**: Client-side face landmark descriptor vector extraction with server-side Euclidean distance verification preventing duplicate or proxy balloting.
- **Mathematical Quorum & Supermajority Engine**: Automates a strict two-stage democratic barrier (minimum 60% class quorum and 75% affirmative consensus) before certifying any collective bunk proposal.
- **Credit-Based Micro-Economy**: Gamified bunk credit ledger (10 credits consumed to propose, 5 credits rewarded for voting participation) incentivizing democratic attendance coordination.
- **Role-Based Access Control (RBAC)**: Enforced via `AuthenticationFilter` and `AdminFilter` isolating student actions from administrative audits and roster controls.

---

## 🛠️ 2. Technologies & Tools Used

| Layer | Technologies & Frameworks | Description / Role |
|---|---|---|
| **Programming Language** | Java (JDK 17 LTS / JDK 11+) | Core enterprise backend runtime |
| **Web Tier & Controllers** | Java Servlets (`HttpServlet`, `doGet`, `doPost`) | MVC controller handling HTTP requests and navigation |
| **Security & Interceptors** | Java Servlet Filters (`Filter`, `FilterChain`) | `AuthenticationFilter`, `AdminFilter` for session & RBAC |
| **Application Lifecycle** | `ServletContextListener` (`AppContextListener`) | Automated schema creation and initial database seeding |
| **Presentation Tier** | JavaServer Pages (`.jsp`), JSTL Core, EL | Server-side template rendering with zero scriptlets |
| **UI & Styling** | HTML5, CSS3 Custom Properties, Responsive Grid | Fluid desktop & mobile layout with modern dark theme |
| **Client Biometrics** | HTML5 MediaDevices API, WebCam Canvas, JS | Real-time face detection and 128-d vector embedding |
| **Persistence / ORM** | Pure JDBC (`PreparedStatement`, `ResultSet`, Transactions) | High-performance SQL abstraction and connection management |
| **Database** | Embedded H2 Database / PostgreSQL / SQLite | Multi-mode zero-config embedded persistence with AUTO_SERVER |
| **Build & Dependency Mgmt** | Apache Maven 3.9+ (`pom.xml`) | Lifecycle management, compilation, and WAR packaging |
| **Application Server** | Embedded Apache Tomcat 8.5 / Standalone Runner | Instant executable servlet container |
| **Containerization & Cloud** | Docker, Procfile, Render Cloud | Multi-platform production deployment |

---

## ✨ 3. Features & Functionalities

### 1. Student Portal
- **Secure Authentication & Registration**: Validated form submission with SHA-256 password hashing.
- **Interactive Timetable Browser**: Dynamic day-by-day weekly timetable mapped to class sections.
- **Proposal Creation Engine**: Propose a collective leave for any active lecture slot with credit verification and automated 24-hour expiration.
- **Live Ballot Room**: Real-time progress bar tracking total votes cast, quorum percentage, and supermajority consensus status.
- **Webcam Biometric Face Enrollment**: Capture face landmarks directly from browser camera into biometric vector representation.

### 2. Administrator Console
- **Student Approval Queue**: Review, approve, or reject new student registrations.
- **Class Section Management**: Create and configure class sections, semesters, and capacity quotas.
- **Timetable Slot Scheduler**: Add, modify, or retire course slots, faculty assignments, and classroom numbers.
- **Proposal Audit & Override**: Full audit trail of active, passed, and rejected ballots with administrative override capability.
- **User Roster Ledger**: View all registered students, credit balances, and reliability scores.

---

## 👥 4. Pre-Seeded Demo Credentials

| Role | Email Address | Password | Roll Number | Account Details & Permissions |
|---|---|---|---|---|
| **Admin** | `admin@scbs.edu` | `admin123` | `ADMIN-001` | Full administrative console access and audit controls |
| **Student** | `alex@scbs.edu` | `student123` | `CSE-2024-001` | 120 Bunk Credits, Biometrics enrolled, Active proposer |
| **Student** | `sarah@scbs.edu` | `student123` | `CSE-2024-002` | 95 Bunk Credits, Biometrics enrolled, Active voter |
| **Student** | `jordan@scbs.edu` | `student123` | `CSE-2024-003` | 85 Bunk Credits, Face enrollment pending |
| **Student** | `liam@scbs.edu` | `student123` | `CSE-2024-004` | Pending Admin Approval demonstration account |

---

## 🗄️ 5. Database Configuration & Setup

### Database Architecture
The application runs on an embedded **H2 Database** engine (`./scbs_db.mv.db`) with zero external installation required. The schema and initial data are automatically created upon application startup by `AppContextListener`.

### Standalone SQL Script:
A comprehensive standalone SQL script is included in the project root:
- File: `database_setup.sql`
- Contains: Full DDL (`CREATE TABLE`), foreign key constraints, indexes, and initial DML seed data.

### Manual SQL Setup (MySQL / PostgreSQL / Oracle):
If deploying to external database servers:
1. Open MySQL Workbench, pgAdmin, or your SQL CLI.
2. Create database:
   ```sql
   CREATE DATABASE scbs_db;
   ```
3. Execute `database_setup.sql` to instantiate the 5 relational tables:
   - `classes`
   - `students`
   - `timetable_slots`
   - `bunk_proposals`
   - `bunk_votes`
4. Update JDBC credentials in `DBConnectionManager.java` or pass system properties:
   ```bash
   -Ddb.url="jdbc:postgresql://localhost:5432/scbs_db" -Ddb.user="postgres" -Ddb.password="yourpassword"
   ```

---

## 🚀 6. Installation & Execution Instructions

### Prerequisites
- **Java Development Kit (JDK 11 or 17 LTS)** installed (`java -version`).
- **Apache Maven 3.8+** installed (`mvn -v`) or use the included Maven wrapper.
- Internet connectivity for initial dependency resolution.

### Method 1: Instant Standalone Execution (Recommended)
Clone the repository and run using Maven:
```bash
# 1. Clone repository
git clone https://github.com/vaibhav101006/banksystem.git
cd banksystem

# 2. Compile and launch embedded server
mvn clean compile exec:java
```
Once the log prints `Server started on http://localhost:8080/scbs`, open your web browser and navigate to:
**`http://localhost:8080/scbs/login`**

### Method 2: Package & Deploy to Apache Tomcat
To generate a production `.war` archive:
```bash
mvn clean package
```
1. Copy the generated `target/scbs.war` into your Tomcat installation's `webapps/` directory.
2. Start Tomcat (`bin/startup.sh` or `bin/startup.bat`).
3. Access: `http://localhost:8080/scbs/login`

### Method 3: Run with Docker
```bash
docker build -t scbs-app .
docker run -p 8080:8080 scbs-app
```

---

## 🌐 7. Cloud Deployment Information

- **Hosting Environment**: Render Cloud Application Platform / Docker Container
- **Deployed Web App URL**: [https://banksystem-scbs.onrender.com](https://banksystem-scbs.onrender.com)
- **Deployment Configuration**: Configured via `Dockerfile` and `Procfile` utilizing Eclipse Temurin 17 JRE.
- **Health Check Endpoint**: `/login` (Status 200 OK)

---

## 📸 8. System Screenshots Overview

1. **Figure 1**: Student & Administrator Secure Login Console (`login.jsp`)
2. **Figure 2**: Responsive Student Registration with Class Selection (`register.jsp`)
3. **Figure 3**: Student Attendance Dashboard with Live Proposals (`dashboard.jsp`)
4. **Figure 4**: Weekly Timetable Matrix with Lecture Slots (`timetable.jsp`)
5. **Figure 5**: Bunk Proposal Creation Modal with Credit Audit (`propose.jsp`)
6. **Figure 6**: Live Democratic Ballot Detail & Quorum Consensus Meter (`proposal-detail.jsp`)
7. **Figure 7**: Webcam Biometric Face Enrollment & Reliability Ledger (`profile.jsp`)
8. **Figure 8**: Administrator Approval & User Governance Console (`admin/approvals.jsp`)

---

## 📂 9. Project Directory Structure

```
banksystem/
├── pom.xml                                // Maven dependencies, plugins & build specs
├── database_setup.sql                     // Standalone SQL DDL & DML initialization script
├── Dockerfile                             // Container deployment definition
├── Procfile                               // Cloud server process runner
├── README.md                              // Complete project documentation
├── src/
│   └── main/
│       ├── java/com/scbs/
│       │   ├── dao/                       // Pure JDBC DAOs & DBConnectionManager
│       │   ├── filter/                    // Security Filters (Authentication & Admin RBAC)
│       │   ├── listener/                  // AppContextListener (Startup DB Auto-Init)
│       │   ├── model/                     // Entity JavaBeans (Student, Proposal, Vote, Slot, Class)
│       │   ├── service/                   // Biometric Euclidean math & Consensus calculations
│       │   ├── servlet/                   // Java Servlets (Auth, Dashboard, Timetable, Vote, Admin)
│       │   ├── util/                      // SHA-256 Password Cryptography
│       │   └── StandaloneServer.java      // Embedded Tomcat launcher
│       └── webapp/
│           ├── index.jsp                  // Entry redirector
│           ├── WEB-INF/
│           │   ├── web.xml                // Servlet mappings & error page definitions
│           │   └── views/                 // Protected JSP views (common, auth, admin)
│           └── static/
│               ├── css/style.css          // Polished responsive modern theme
│               └── js/biometrics.js       // Live Webcam face landmark processor
```

---
*Submitted for Advance Java Technology (01CE0308) – Task 4 Final Submission.*
