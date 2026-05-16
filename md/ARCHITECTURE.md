# System Architecture & Design Document

## 1. System Overview

```
┌─────────────────────────────────────────────────────────────────┐
│               Academic Advisor Appointment System              │
│                     Login Module (Phase 1)                      │
└─────────────────────────────────────────────────────────────────┘
                              │
                    ┌─────────┴─────────┐
                    │                   │
            ┌──────────────┐    ┌──────────────┐
            │   Frontend   │    │   Backend    │
            │   (JSP/HTML) │    │   (Servlet)  │
            └──────────────┘    └──────────────┘
                    │                   │
        ┌───────────┴───────────┐      │
        │                       │      │
    ┌────────┐          ┌──────────────┐
    │ login  │          │   Database   │
    │ .jsp   │          │  (Hashtable) │
    └────────┘          └──────────────┘
        │                      │
        ├─ Error Display       ├─ SHA-256 Hashing
        ├─ Form Validation     ├─ Password Verification
        └─ User Input          └─ Session Management
```

---

## 2. Component Architecture

### A. Presentation Layer (Frontend)
```
Presentation Layer
├── login.jsp
│   ├── HTML Structure
│   ├── CSS Styling
│   │   └── Responsive design
│   ├── JavaScript
│   │   ├── Form validation
│   │   └── Password toggle
│   └── Error Display
│       └── Server-side error messages
│
├── dashboard.jsp
│   ├── Session validation
│   ├── User information display
│   └── Feature cards
│
├── logout.jsp
│   ├── Session invalidation
│   └── Redirect to login
│
└── index.html
    └── Auto-redirect to login.jsp
```

### B. Business Logic Layer (Backend)
```
Business Logic Layer
│
└── LoginServlet.java
    ├── init() Method
    │   ├── Initialize Hashtable
    │   ├── Load sample users
    │   └── Hash passwords
    │
    ├── doGet() Method
    │   └── Redirect to login.jsp
    │
    ├── doPost() Method
    │   ├── Get request parameters
    │   ├── Input validation
    │   ├── Hashtable lookup
    │   ├── Password verification
    │   ├── Session creation
    │   └── Redirect/Forward
    │
    ├── hashPassword() Method
    │   ├── SHA-256 algorithm
    │   └── Base64 encoding
    │
    └── verifyPassword() Method
        ├── Hash entered password
        └── Compare with stored hash
```

### C. Data Layer (Storage)
```
Data Layer
│
└── Hashtable (In-Memory)
    │
    ├── Structure: Hashtable<String, String>
    │
    └── Entries:
        ├── admin    → SHA256("admin123")
        ├── advisor  → SHA256("advisor123")
        └── student  → SHA256("student123")
```

---

## 3. Detailed Data Flow

### Complete Authentication Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                        USER BROWSER                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  1. User navigates to:                                          │
│     http://localhost:8080/AcademicAdvisor/login.jsp             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    WEB SERVER (Tomcat)                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  2. Servlet Container receives request                          │
│     GET /AcademicAdvisor/login.jsp                              │
│                                                                  │
│  3. Returns login.jsp → Browser                                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    USER BROWSER (Again)                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  4. User sees login form with:                                  │
│     ┌────────────────────────────────┐                         │
│     │ Username: [____________]       │                         │
│     │ Password: [____________] 👁️    │                         │
│     │ □ Remember me                  │                         │
│     │ [Sign In] [Forgot Password?]   │                         │
│     └────────────────────────────────┘                         │
│                                                                  │
│  5. User enters credentials:                                    │
│     Username: admin                                             │
│     Password: admin123                                          │
│                                                                  │
│  6. Form submitted (onclick: validateForm())                    │
│     ✓ Username not empty                                        │
│     ✓ Password not empty                                        │
│     ✓ Username >= 3 chars                                       │
│     ✓ Password >= 6 chars                                       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    WEB SERVER (Tomcat)                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  7. Receives POST request:                                      │
│     POST /AcademicAdvisor/LoginServlet                          │
│     Body: username=admin&password=admin123                      │
│                                                                  │
│  8. LoginServlet.doPost() executes:                             │
│     String username = request.getParameter("username");         │
│     String password = request.getParameter("password");         │
│                                                                  │
│  9. Server-side validation:                                     │
│     ✓ username != null && !username.isEmpty()                   │
│     ✓ password != null && !password.isEmpty()                   │
│                                                                  │
│  10. Check Hashtable:                                           │
│      if (userDatabase.containsKey("admin"))                     │
│      → TRUE (User exists)                                       │
│                                                                  │
│  11. Get stored hash:                                           │
│      storedHash = userDatabase.get("admin");                    │
│      → "Vb/mKMM0KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x="             │
│                                                                  │
│  12. Hash entered password:                                     │
│      enteredHash = hashPassword("admin123");                    │
│      → Process:                                                 │
│         • SHA-256("admin123") → byte array                      │
│         • Base64.encode() → String                              │
│      → Result: "Vb/mKMM0KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x="     │
│                                                                  │
│  13. Verify password:                                           │
│      if (enteredHash.equals(storedHash))                        │
│      → TRUE (Password matches)                                  │
│                                                                  │
│  14. Create session:                                            │
│      HttpSession session = request.getSession();               │
│      session.setAttribute("username", "admin");                │
│      session.setMaxInactiveInterval(1800); // 30 min           │
│                                                                  │
│  15. Redirect to dashboard:                                     │
│      response.sendRedirect("dashboard.jsp");                    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    USER BROWSER (Final)                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  16. Browser receives redirect (302)                            │
│      Location: /AcademicAdvisor/dashboard.jsp                   │
│      Cookie: JSESSIONID=A1B2C3D4E5F6G7H8I9J0                    │
│                                                                  │
│  17. Browser requests dashboard:                                │
│      GET /AcademicAdvisor/dashboard.jsp                         │
│      Cookie: JSESSIONID=A1B2C3D4E5F6G7H8I9J0                    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    WEB SERVER (Tomcat)                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  18. Dashboard.jsp processes:                                   │
│      String username = (String) session.getAttribute("username");
│      if (username == null)                                      │
│         → Not null, so proceed ✓                                │
│                                                                  │
│  19. Generate dashboard HTML with user data                     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    USER BROWSER (SUCCESS!)                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  20. Dashboard displays:                                        │
│      ┌─────────────────────────────────────────┐               │
│      │ Academic Advisor System                 │               │
│      │ Welcome, admin            [Logout]      │               │
│      ├─────────────────────────────────────────┤               │
│      │                                         │               │
│      │  Welcome to Academic Advisor...         │               │
│      │  User: admin                            │               │
│      │  Login Time: 2026-04-05 11:30:00        │               │
│      │                                         │               │
│      │ ┌──────────────┐ ┌──────────────┐     │               │
│      │ │ Schedule ... │ │ My Appts ... │     │               │
│      │ └──────────────┘ └──────────────┘     │               │
│      │ ... more cards ...                     │               │
│      │                                         │               │
│      └─────────────────────────────────────────┘               │
│                                                                  │
│  ✅ LOGIN SUCCESSFUL                                           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4. Class Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                     LoginServlet                                │
├─────────────────────────────────────────────────────────────────┤
│ - static Hashtable<String, String> userDatabase                │
├─────────────────────────────────────────────────────────────────┤
│ + init() : void                                                 │
│ + doGet(HttpServletRequest, HttpServletResponse) : void         │
│ + doPost(HttpServletRequest, HttpServletResponse) : void        │
│ + getServletInfo() : String                                     │
│ - hashPassword(String) : String                                 │
│ - verifyPassword(String, String) : boolean                      │
└─────────────────────────────────────────────────────────────────┘
         ▲
         │ extends
         │
┌────────┴───────┐
│ HttpServlet    │
└────────────────┘


┌─────────────────────────────────────────────────────────────────┐
│                        Hashtable                                │
│              <String (username), String (hash)>                 │
├─────────────────────────────────────────────────────────────────┤
│ Entry 1: "admin"   → "Vb/mKMM0KxG1yA8K3fZc2rKmFjJRKe3gK1Z..."  │
│ Entry 2: "advisor" → "pZGm5XE0KxL9yA7K3fZc2rKmFjJRKe3gK1Z..."  │
│ Entry 3: "student" → "KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x7K3..."  │
└─────────────────────────────────────────────────────────────────┘
         ▲
         │ contains
         │
   LoginServlet

┌──────────────────────────────────────────────┐
│          HttpSession                         │
├──────────────────────────────────────────────┤
│ - ID: JSESSIONID                             │
│ - Attributes:                                │
│   • username: String                         │
│   • loginTime: long                          │
│   • maxInactiveInterval: int (1800 seconds)  │
└──────────────────────────────────────────────┘
         ▲
         │ created by
         │
   LoginServlet
```

---

## 5. Sequence Diagram

```
User        Browser         Tomcat       LoginServlet    Hashtable
 │             │               │              │              │
 │ 1. navigate  │               │              │              │
 ├─────────────→│               │              │              │
 │ (to login)   │ 2. GET        │              │              │
 │              ├──────────────→│              │              │
 │              │               │ 3. doGet()   │              │
 │              │               ├─────────────→│              │
 │              │               │ 4. forward() │              │
 │              │               │←─────────────┤              │
 │              │ 5. login.jsp  │              │              │
 │              │←──────────────┤              │              │
 │ 6. sees form │               │              │              │
 │←─────────────┤               │              │              │
 │              │               │              │              │
 │ 7. enters    │               │              │              │
 │ credentials  │               │              │              │
 │              │ 8. POST       │              │              │
 │              ├──────────────→│              │              │
 │              │               │ 9. doPost()  │              │
 │              │               ├─────────────→│              │
 │              │               │              │ 10. get()    │
 │              │               │              ├─────────────→│
 │              │               │              │ (lookup user)│
 │              │               │              │←─────────────┤
 │              │               │              │ (found: true)│
 │              │               │              │              │
 │              │               │              │ 11. get()    │
 │              │               │              ├─────────────→│
 │              │               │              │ (get hash)   │
 │              │               │              │←─────────────┤
 │              │               │              │ (hash)       │
 │              │               │              │              │
 │              │               │              │ 12. verify() │
 │              │               │              │ (compare)    │
 │              │               │ 13. create   │              │
 │              │               │ session      │              │
 │              │               │←─────────────┤              │
 │              │               │              │              │
 │              │ 14. redirect  │              │              │
 │              │←──────────────┤              │              │
 │ 15. receives │               │              │              │
 │ dashboard    │               │              │              │
 │←─────────────┤               │              │              │
 │              │ ✅ SUCCESS    │              │              │
```

---

## 6. MVC Architecture Mapping

```
MODEL (Data Layer)
│
├── Hashtable<String, String>
│   └── User credentials storage
│
└── User
    ├── username: String
    └── password_hash: String


VIEW (Presentation Layer)
│
├── login.jsp
│   ├── Login form HTML
│   ├── CSS styling
│   ├── JavaScript validation
│   └── Error display
│
├── dashboard.jsp
│   ├── Dashboard HTML
│   ├── User information display
│   └── Feature cards
│
└── logout.jsp
    └── Session cleanup


CONTROLLER (Business Logic Layer)
│
└── LoginServlet
    ├── Receives user input
    ├── Validates input
    ├── Authenticates user
    ├── Manages session
    └── Routes to next view
```

---

## 7. Security Architecture

```
SECURITY LAYERS
│
├── Layer 1: Client-Side Validation
│   ├── Empty field checks
│   ├── Length validation
│   ├── Format validation
│   └── Purpose: Quick feedback, UX
│
├── Layer 2: Transport Security
│   ├── HTTPS (production only)
│   ├── Secure session cookies
│   └── Purpose: Protect credentials in transit
│
├── Layer 3: Server-Side Validation
│   ├── Null/empty checks
│   ├── Whitespace trimming
│   ├── Type checking
│   └── Purpose: Security barrier
│
├── Layer 4: Authentication
│   ├── Hashtable lookup
│   ├── SHA-256 hashing
│   ├── Hash comparison
│   └── Purpose: Verify user identity
│
├── Layer 5: Authorization
│   ├── Session creation
│   ├── Session attributes
│   ├── Session timeout
│   └── Purpose: Control access
│
└── Layer 6: Data Protection
    ├── Session server-side storage
    ├── No sensitive data in URL
    ├── Generic error messages
    └── Purpose: Protect stored data
```

---

## 8. Deployment Architecture

```
PRODUCTION ENVIRONMENT
│
┌─────────────────────────────────────────┐
│         Client Tier (Browser)            │
│  ┌─────────────────────────────────────┐ │
│  │  • login.jsp (HTTP request)         │ │
│  │  • dashboard.jsp (authenticated)    │ │
│  │  • logout.jsp (redirect)            │ │
│  └─────────────────────────────────────┘ │
└─────────────────────────────────────────┘
              ▲         │
              │ HTTPS   │
              │         ▼
┌─────────────────────────────────────────┐
│    Application Tier (Web Server)         │
│  ┌─────────────────────────────────────┐ │
│  │  • Tomcat / GlassFish               │ │
│  │  • Servlet Container                │ │
│  │  • Session Manager                  │ │
│  │  ┌─────────────────────────────────┐ │ │
│  │  │     LoginServlet                │ │ │
│  │  │  • Authentication logic         │ │ │
│  │  │  • Password verification        │ │ │
│  │  │  • Session management          │ │ │
│  │  └─────────────────────────────────┘ │ │
│  └─────────────────────────────────────┘ │
└─────────────────────────────────────────┘
              ▲         │
              │ JDBC    │
              │         ▼
┌─────────────────────────────────────────┐
│    Data Tier (Database)                  │
│  ┌─────────────────────────────────────┐ │
│  │  • MySQL / PostgreSQL               │ │
│  │  • Users table                      │ │
│  │  • Credentials (hashed)             │ │
│  │  • Audit logs                       │ │
│  └─────────────────────────────────────┘ │
└─────────────────────────────────────────┘
```

---

## 9. Error Handling Flow

```
ERROR HANDLING ARCHITECTURE
│
├── Client-Side Errors
│   ├── Empty fields → Alert message
│   ├── Invalid format → Alert message
│   ├── Min length → Alert message
│   └── No page reload (return false)
│
├── Server-Side Validation Errors
│   ├── Null/empty values
│   │  └── Display error, return to login
│   │
│   ├── User not found
│   │  └── "Invalid username or password"
│   │
│   ├── Password mismatch
│   │  └── "Invalid username or password"
│   │
│   └── Generic message (security)
│       └── Prevent username enumeration
│
├── System Errors
│   ├── Hashtable initialization failure
│   │  └── Exception handling → Log error
│   │
│   ├── Database connection error
│   │  └── Exception handling → Error page
│   │
│   └── Session creation failure
│      └── Exception handling → Error page
│
└── Error Messages
    ├── Sent to JSP as request attribute
    ├── Displayed to user in error box
    ├── Generic for security
    └── Never expose implementation details
```

---

## 10. Technology Stack

```
TECHNOLOGY STACK
│
├── Backend
│   ├── Java (JDK 8+)
│   ├── Servlet API 3.0+
│   ├── HTTP protocol
│   ├── SHA-256 hashing
│   └── Base64 encoding
│
├── Frontend
│   ├── HTML5
│   ├── CSS3
│   │   ├── Flexbox
│   │   ├── Grid
│   │   └── Gradients
│   ├── JavaScript (Vanilla)
│   │   ├── DOM manipulation
│   │   └── Form validation
│   └── Emoji icons
│
├── Server
│   ├── Apache Tomcat 9+
│   └── GlassFish 5+
│
├── Session Management
│   ├── HttpSession API
│   ├── JSESSIONID cookie
│   └── Server-side storage
│
└── Security
    ├── MessageDigest (SHA-256)
    ├── Base64 encoding
    ├── Session timeout
    └── Input validation
```

---

## 11. Key Design Patterns

### 1. MVC Pattern
- **Model**: Hashtable (user data)
- **View**: JSP pages (presentation)
- **Controller**: LoginServlet (business logic)

### 2. Singleton Pattern
- **Hashtable**: Single instance per servlet
- **Created**: In `init()` method
- **Scope**: Application level

### 3. Template Method Pattern
- **HttpServlet**: Defines flow (template)
- **LoginServlet**: Implements `doPost()` and `doGet()`

### 4. Factory Pattern
- **Session**: Created by servlet container
- **HttpSession**: Created on demand via `getSession()`

### 5. Decorator Pattern
- **Authentication**: Decorates request with session
- **Session**: Wraps user identity information

---

## 12. Scalability Considerations

### Current (Development)
```
✅ Pros:
├── Simple implementation
├── Fast development
├── No external dependencies
└── Good for learning

❌ Cons:
├── In-memory storage (lost on restart)
├── Not scalable (100+ users)
├── Single server only
└── No persistence
```

### Production (Recommended)
```
✅ Improvements:
├── Database backend
├── Connection pooling
├── Load balancing
├── Distributed sessions
├── Caching layer
└── Monitoring & logging
```

---

## 13. Future Enhancements

```
ENHANCEMENT ROADMAP
│
├── Phase 2: User Management
│   ├── User registration
│   ├── Password reset
│   ├── Profile management
│   └── Account settings
│
├── Phase 3: Appointment System
│   ├── Schedule appointments
│   ├── View appointments
│   ├── Cancel appointments
│   └── Reschedule appointments
│
├── Phase 4: Messaging
│   ├── Send messages
│   ├── View messages
│   ├── Message notifications
│   └── Attachment support
│
├── Phase 5: Academic Tracking
│   ├── View grades
│   ├── Track progress
│   ├── Course recommendations
│   └── Academic alerts
│
└── Phase 6: Admin Features
    ├── User management
    ├── System settings
    ├── Reports & analytics
    └── Audit logs
```

---

**Architecture Document**
**Date:** April 5, 2026
**Project:** Academic Advisor Appointment System - Login Module
**Version:** 1.0
**Status:** Complete & Documented
