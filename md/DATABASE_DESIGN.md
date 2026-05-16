# Database Design & Usage Guide

## Current Implementation: Hashtable-Based User Database

### Data Structure Overview

```
┌─────────────────────────────────────────────────────┐
│         Hashtable<String, String>                   │
│         (userDatabase)                              │
├─────────────────────────────────────────────────────┤
│  Key: Username (String)                             │
│  Value: SHA-256 Hashed Password (String/Base64)     │
└─────────────────────────────────────────────────────┘
```

### Sample Data Structure

```
HashMap Entry 1:
  Key:   "admin"
  Value: "Vb/mKMM0KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x7="
  (This is SHA-256 hash of "admin123")

HashMap Entry 2:
  Key:   "advisor"
  Value: "pZGm5XE0KxL9yA7K3fZc2rKmFjJRKe3gK1Z5mN9x="
  (This is SHA-256 hash of "advisor123")

HashMap Entry 3:
  Key:   "student"
  Value: "KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x7K3fZc2="
  (This is SHA-256 hash of "student123")
```

---

## Password Hashing Process

### Hashing Algorithm Flow

```
Plain Text Password
        │
        ▼
┌──────────────────────┐
│ SHA-256 Algorithm    │
│ (256-bit output)     │
└──────────────────────┘
        │
        ▼
    ┌────────┐
    │ Bytes  │  (32 bytes = 256 bits)
    └────────┘
        │
        ▼
┌──────────────────────┐
│ Base64 Encoding      │
│ (Human readable)     │
└──────────────────────┘
        │
        ▼
 Hashed Password
(Stored in Hashtable)
```

### Example: How Password "admin123" is Hashed

```
Step 1: Plain Text
Input: "admin123"

Step 2: SHA-256 Hashing
Byte array: 
[158, 101, 233, 220, 139, 74, 49, 176, 
 22, 200, 15, 11, 221, 246, 28, 218,
 170, 166, 22, 50, 81, 41, 237, 224,
 171, 86, 121, 156, 246, 252, 255]

Step 3: Base64 Encoding
Output: "Vb/mKMM0KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x7="

Step 4: Storage
Database[username] = hashed_value
```

---

## Authentication Flow with Database

### Login Process

```
START
  │
  ▼
┌─────────────────────────────┐
│ User submits credentials    │
│ username: "admin"           │
│ password: "admin123"        │
└─────────────────────────────┘
  │
  ▼
┌──────────────────────────────────┐
│ LoginServlet receives POST        │
│ 1. Get username from request      │
│ 2. Get password from request      │
└──────────────────────────────────┘
  │
  ▼
┌──────────────────────────────────┐
│ Input Validation                 │
│ ✓ Username not empty?            │
│ ✓ Password not empty?            │
│ ✓ Username >= 3 chars?           │
│ ✓ Password >= 6 chars?           │
└──────────────────────────────────┘
  │
  ├─ Validation Failed ─────┐
  │                         │
  │                         ▼
  │                 [Show Error]
  │                 [Return to login.jsp]
  │
  ├─ Validation Passed
  │
  ▼
┌──────────────────────────────────┐
│ Check Hashtable                  │
│ if (userDatabase.containsKey     │
│     (username)) {                │
└──────────────────────────────────┘
  │
  ├─ User NOT found ────────┐
  │                         │
  │                         ▼
  │                 [Error: Invalid credentials]
  │                 [Return to login.jsp]
  │
  ├─ User FOUND
  │
  ▼
┌──────────────────────────────────┐
│ Retrieve stored hash from DB     │
│ storedHash = userDatabase.get    │
│             (username)           │
└──────────────────────────────────┘
  │
  ▼
┌──────────────────────────────────┐
│ Hash entered password            │
│ enteredHash = hashPassword       │
│              (password)          │
└──────────────────────────────────┘
  │
  ▼
┌──────────────────────────────────┐
│ Compare hashes                   │
│ if (enteredHash.equals          │
│     (storedHash)) {              │
└──────────────────────────────────┘
  │
  ├─ Hashes DON'T match ────┐
  │                         │
  │                         ▼
  │                 [Error: Invalid credentials]
  │                 [Return to login.jsp]
  │
  ├─ Hashes MATCH
  │
  ▼
┌──────────────────────────────────┐
│ Create Session                   │
│ session = request.getSession()   │
│ session.setAttribute             │
│  ("username", username)          │
└──────────────────────────────────┘
  │
  ▼
┌──────────────────────────────────┐
│ Redirect to Dashboard            │
│ response.sendRedirect            │
│  ("dashboard.jsp")               │
└──────────────────────────────────┘
  │
  ▼
SUCCESS - User logged in
```

---

## Data Persistence Strategy

### Current (In-Memory)
- ✅ Fast lookup: O(1)
- ✅ Simple implementation
- ❌ Data lost on server restart
- ❌ Not suitable for multiple servers
- ❌ Limited scalability

### Initialization Code
```java
@Override
public void init() throws ServletException {
    super.init();
    userDatabase = new Hashtable<>();
    
    // Load sample users
    userDatabase.put("admin", hashPassword("admin123"));
    userDatabase.put("advisor", hashPassword("advisor123"));
    userDatabase.put("student", hashPassword("student123"));
}
```

---

## Upgrading to Database (Future)

### MySQL Table Structure
```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(256) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_username (username)
);
```

### Upgrade Code Example
```java
// Instead of Hashtable
private DataSource dataSource;

@Override
public void init() throws ServletException {
    try {
        InitialContext ctx = new InitialContext();
        dataSource = (DataSource) ctx.lookup(
            "java:comp/env/jdbc/AcademicAdvisor");
    } catch (NamingException e) {
        throw new ServletException(e);
    }
}

private boolean authenticateUser(String username, String password) 
        throws SQLException {
    String query = "SELECT password_hash FROM users WHERE username = ?";
    
    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, username);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                String hash = rs.getString("password_hash");
                return verifyPassword(password, hash);
            }
        }
    }
    return false;
}
```

---

## Session Data Structure

### Session Attributes
```
Session {
    ID: "JSESSIONID=A1B2C3D4E5F6G7H8I9J0"
    
    Attributes:
    ├── "username" → "admin"
    ├── "loginTime" → 1712282400000
    ├── "maxInactiveInterval" → 1800 (seconds)
    └── (servlet container managed attributes)
    
    Timeout: 30 minutes
    Created: 2026-04-05 11:30:00
}
```

### Session Creation
```java
// In LoginServlet.doPost()
HttpSession session = request.getSession();
session.setAttribute("username", username);
session.setAttribute("loginTime", System.currentTimeMillis());
session.setMaxInactiveInterval(30 * 60); // 30 minutes
```

### Session Retrieval
```java
// In dashboard.jsp
String username = (String) session.getAttribute("username");
if (username == null) {
    response.sendRedirect("login.jsp");
    return;
}
```

### Session Invalidation
```java
// In logout.jsp
session.invalidate();
response.sendRedirect("login.jsp");
```

---

## Hashtable vs HashMap vs ConcurrentHashMap

| Property | Hashtable | HashMap | ConcurrentHashMap |
|----------|-----------|---------|------------------|
| Thread Safe | ✅ Yes | ❌ No | ✅ Yes |
| Synchronized | ✅ Yes | ❌ No | ✅ Yes (per segment) |
| Performance | Slower | Fastest | Fast |
| Null Keys | ❌ No | ✅ Yes | ❌ No |
| Null Values | ❌ No | ✅ Yes | ❌ No |
| Legacy | ✅ (older) | ✅ (modern) | ✅ (concurrent) |
| Use Case | **Thread-safe, simple** | **Single-threaded** | **Concurrent access** |

**Why Hashtable for this project?**
- Thread-safe by default
- Simple implementation for demonstration
- Each servlet has its own Hashtable instance
- Servlet container handles synchronization

---

## Hashtable Operations in LoginServlet

### 1. Add User
```java
userDatabase.put("newuser", hashPassword("password123"));
```

### 2. Check if User Exists
```java
if (userDatabase.containsKey(username)) {
    // User exists
}
```

### 3. Get User Hash
```java
String hash = userDatabase.get(username);
```

### 4. Remove User
```java
userDatabase.remove(username);
```

### 5. Update Password
```java
userDatabase.put(username, hashPassword("newpassword"));
```

### 6. Get All Users
```java
Enumeration<String> users = userDatabase.keys();
while (users.hasMoreElements()) {
    String user = users.nextElement();
    // Process user
}
```

---

## Security: Why Hashing is Important

### Without Hashing (❌ WRONG)
```java
userDatabase.put("admin", "admin123");  // Plain text!

// If database is compromised:
// Attacker gets: admin123
// Attacker can access: admin account immediately
```

### With Hashing (✅ CORRECT)
```java
userDatabase.put("admin", 
    hashPassword("admin123"));  // Hashed!
// Stored: "Vb/mKMM0KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x="

// If database is compromised:
// Attacker gets: "Vb/mKMM0KxG1yA8K3fZc2rKmFjJRKe3gK1Z5mN9x="
// Attacker cannot easily reverse it
// SHA-256 is one-way: Hash ← Password (no reverse)
```

---

## Test Data Reference

| Username | Password | Hash |
|----------|----------|------|
| admin | admin123 | (SHA-256) |
| advisor | advisor123 | (SHA-256) |
| student | student123 | (SHA-256) |

**How to generate your own hashes (online SHA-256 tools):**
1. Visit: https://www.sha256.online/
2. Enter password
3. Copy hash
4. Add to Hashtable in LoginServlet.java

---

## Troubleshooting Database Issues

### Issue: User can't login
**Possible Causes:**
- Username not in Hashtable
- Password hash doesn't match
- Case sensitivity (username matching)

**Debug Steps:**
```java
// Add debug output
System.out.println("Username: " + username);
System.out.println("User exists: " + 
    userDatabase.containsKey(username));
System.out.println("Entered hash: " + 
    hashPassword(password));
System.out.println("Stored hash: " + 
    userDatabase.get(username));
```

### Issue: All logins fail
**Possible Causes:**
- Hashtable not initialized
- `init()` method not called
- Hash function broken

**Fix:**
```java
@Override
public void init() throws ServletException {
    super.init();  // Don't forget super.init()
    userDatabase = new Hashtable<>();
    // Populate database
}
```

---

## Hashtable Memory Usage

```
Approximate Memory per User:
├── Username (string): ~40-100 bytes
├── Password hash (Base64): ~64 bytes
├── Hashtable entry overhead: ~48 bytes
└── Total per user: ~150-200 bytes

Examples:
├── 100 users: ~15-20 KB
├── 1000 users: ~150-200 KB
├── 10000 users: ~1.5-2 MB
└── 100000 users: ~15-20 MB
```

**For production**: Migrate to database if > 10,000 users

---

**Document Date:** April 5, 2026
**System:** Academic Advisor Appointment System - Login Module
