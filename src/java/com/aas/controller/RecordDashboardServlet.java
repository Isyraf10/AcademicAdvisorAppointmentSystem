package com.aas.controller;

import com.aas.dao.RecordDAO;
import com.aas.model.Record;
import com.aas.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "RecordDashboardController", urlPatterns = {"/records/dashboard"})
public class RecordDashboardServlet extends HttpServlet {

    private final RecordDAO recordDAO = new RecordDAO();

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    HttpSession session = request.getSession(false);
    User currentUser = resolveUser(session);

    request.setAttribute("currentUser", currentUser);
    request.setAttribute("userRole",    currentUser.getRole());
    request.setAttribute("userName",    currentUser.getName());
    request.setAttribute("userId",      currentUser.getId());

    String role = currentUser.getRole();
    int    uid  = currentUser.getId();

    List<Record> records;
    int totalSessions  = 0;
    int totalAllRecords = 0;
    
    // 1. Create a variable to hold the dynamic target JSP path
    String targetJsp = "/Record/student/studentRecordView.jsp"; 

    switch (role.toUpperCase()) {
        case "ADMIN":
            records = recordDAO.getAllRecords();
            totalAllRecords = recordDAO.countAllRecords();
            request.setAttribute("totalAllRecords", totalAllRecords);
            targetJsp = "/Record/admin/adminRecordView.jsp"; // 2. Assign Admin View
            break;

        case "ADVISOR":
            records = recordDAO.getRecordsByAdvisor(uid);
            List<StudentProgress> progressList = buildProgressForAdvisor(records, uid);
            request.setAttribute("progressList", progressList);
            request.setAttribute("myStudents", recordDAO.getStudentsByAdvisor(uid));
            targetJsp = "/Record/advisor/advisorRecordView.jsp"; // 3. Assign Advisor View
            break;

        case "STUDENT":
        default:
            records = recordDAO.getRecordsByStudent(uid);
            totalSessions = recordDAO.countSessionsByStudent(uid);
            request.setAttribute("totalSessions", totalSessions);
            targetJsp = "/Record/student/studentRecordView.jsp"; // 4. Assign Student View
            break;
    }

    request.setAttribute("records", records);

    String msg = request.getParameter("msg");
    if (msg != null && !msg.isEmpty()) {
        request.setAttribute("flashMsg", msg);
    }

    // 5. Forward to the dynamic path instead of the hardcoded non-existent file
    request.getRequestDispatcher(targetJsp).forward(request, response);
}
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentUser    = resolveUser(session);
        String role         = currentUser.getRole().toUpperCase();
        String action       = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "create":
                if ("ADVISOR".equals(role) || "ADMIN".equals(role)) {
                    handleCreate(request, currentUser);
                    redirect(request, response, "msg=created");
                } else {
                    redirect(request, response, "msg=denied");
                }
                break;
            case "updateStatus":
                if ("ADMIN".equals(role)) {
                    handleUpdateStatus(request);
                    redirect(request, response, "msg=updated");
                } else {
                    redirect(request, response, "msg=denied");
                }
                break;
            case "delete":
                if ("ADMIN".equals(role)) {
                    handleDelete(request);
                    redirect(request, response, "msg=deleted");
                } else {
                    redirect(request, response, "msg=denied");
                }
                break;
            default:
                redirect(request, response, "msg=unknown");
        }
    }

    private void handleCreate(HttpServletRequest request, User creator) {
        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId").trim());
            String summary    = request.getParameter("summary");
            String feedback   = request.getParameter("feedback");
            String actionPlan = request.getParameter("actionPlan");

            Record record = new Record();
            record.setAppointmentId(appointmentId);
            record.setSummary(summary != null ? summary.trim() : "");
            record.setFeedback(feedback != null ? feedback.trim() : "");
            record.setActionPlan(actionPlan != null ? actionPlan.trim() : "");
            record.setRecordStatus("Active");

            recordDAO.createRecord(record);
        } catch (NumberFormatException ex) {}
    }

    private void handleUpdateStatus(HttpServletRequest request) {
        try {
            int recordId = Integer.parseInt(request.getParameter("recordId").trim());
            String newStatus = request.getParameter("newStatus");
            if (newStatus != null && !newStatus.trim().isEmpty()) {
                recordDAO.updateRecordStatus(recordId, newStatus.trim());
            }
        } catch (NumberFormatException ex) {}
    }

    private void handleDelete(HttpServletRequest request) {
        try {
            int recordId = Integer.parseInt(request.getParameter("recordId").trim());
            recordDAO.deleteRecord(recordId);
        } catch (NumberFormatException ex) {}
    }

    private User resolveUser(HttpSession session) {
    if (session != null && session.getAttribute("userId") != null) {
        User user = new User();
        user.setId((Integer) session.getAttribute("userId"));
        user.setName((String) session.getAttribute("name"));
        // Retrieve the lowercase role string ('student', 'advisor', 'admin')
        user.setRole((String) session.getAttribute("role")); 
        return user;
    }
    
    // Safety fallback only if session is entirely dead
    User mockUser = new User();
    mockUser.setId(10); 
    mockUser.setName("Guest");
    mockUser.setRole("STUDENT"); 
    return mockUser;
}

    private List<StudentProgress> buildProgressForAdvisor(List<Record> records, int advisorId) {
        java.util.Map<Integer, StudentProgress> map = new java.util.LinkedHashMap<>();
        for (Record r : records) {
            int sid = r.getStudentId();
            if (!map.containsKey(sid)) {
                map.put(sid, new StudentProgress(sid, r.getStudentName()));
            }
            map.get(sid).increment();
        }
        return new ArrayList<>(map.values());
    }

    public static class StudentProgress {
        private final int studentId;
        private final String studentName;
        private int sessionCount;
        public StudentProgress(int studentId, String studentName) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.sessionCount = 0;
        }
        public void increment() { this.sessionCount++; }
        public int getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public int getSessionCount() { return sessionCount; }
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String queryParams) throws IOException {
        String base = request.getContextPath() + "/records/dashboard";
        response.sendRedirect(queryParams.isEmpty() ? base : base + "?" + queryParams);
    }
}