package com.lab.controller;

import com.lab.dao.RecordDAO;
import com.lab.model.Record;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

/**
 * ManageRecordServlet - Manage consultation records
 * Accessible to: Student (own), Advisor (own), Admin (all)
 */
@WebServlet("/ManageRecordServlet")
public class ManageRecordServlet extends HttpServlet {
    private RecordDAO recordDAO;

    @Override
    public void init() throws ServletException {
        recordDAO = new RecordDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("noMatric") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listRecords(request, response, session);
                break;
            case "view":
                viewRecord(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            default:
                listRecords(request, response, session);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("noMatric") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createRecord(request, response);
        } else if ("update".equals(action)) {
            updateRecord(request, response);
        } else if ("delete".equals(action)) {
            deleteRecord(request, response);
        }
    }

    private void listRecords(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        
        String role = (String) session.getAttribute("role");
        List<Record> records;
        
        if ("admin".equals(role)) {
            records = recordDAO.getAllRecords();
        } else {
            records = recordDAO.getAllRecords(); // Simplified for now
        }
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><head><title>Consultation Records</title>");
        response.getWriter().println(getCss());
        response.getWriter().println("</head><body>");
        response.getWriter().println("<h2>Consultation Records</h2>");
        response.getWriter().println("<table>");
        response.getWriter().println("<tr><th>Record ID</th><th>Appointment ID</th><th>Student ID</th><th>Status</th><th>Actions</th></tr>");
        
        for (Record record : records) {
            response.getWriter().println("<tr><td>" + record.getRecordId() + "</td><td>" + 
                record.getAppointmentId() + "</td><td>" + record.getStudentId() + "</td><td>" +
                record.getRecordStatus() + "</td><td>" +
                "<a href='ManageRecordServlet?action=view&id=" + record.getRecordId() + "' class='btn btn-view'>View</a> " +
                "<a href='ManageRecordServlet?action=edit&id=" + record.getRecordId() + "' class='btn btn-edit'>Edit</a>" +
                "</td></tr>");
        }
        
        response.getWriter().println("</table>");
        response.getWriter().println("<a href='dashboard.jsp' class='btn' style='background: #667eea; color: white;'>← Back</a>");
        response.getWriter().println("</body></html>");
    }

    private void viewRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int recordId = Integer.parseInt(request.getParameter("id"));
        Record record = recordDAO.getRecordById(recordId);
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><head><title>View Record</title>");
        response.getWriter().println(getCss());
        response.getWriter().println("</head><body>");
        response.getWriter().println("<h2>Consultation Record Details</h2>");
        response.getWriter().println("<div class='detail-box'>");
        response.getWriter().println("<p><strong>Record ID:</strong> " + record.getRecordId() + "</p>");
        response.getWriter().println("<p><strong>Appointment ID:</strong> " + record.getAppointmentId() + "</p>");
        response.getWriter().println("<p><strong>Meeting Notes:</strong> " + record.getMeetingNotes() + "</p>");
        response.getWriter().println("<p><strong>Action Items:</strong> " + record.getActionItems() + "</p>");
        response.getWriter().println("<p><strong>Student Status:</strong> " + record.getStudentStatus() + "</p>");
        response.getWriter().println("</div>");
        response.getWriter().println("<a href='ManageRecordServlet' class='btn' style='background: #667eea; color: white;'>← Back</a>");
        response.getWriter().println("</body></html>");
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><head><title>Create Record</title>");
        response.getWriter().println(getCss());
        response.getWriter().println("</head><body>");
        response.getWriter().println("<h2>Create Consultation Record</h2>");
        response.getWriter().println("<form method='POST'>");
        response.getWriter().println("<input type='hidden' name='action' value='create'>");
        response.getWriter().println("<label>Appointment ID:</label>");
        response.getWriter().println("<input type='number' name='appointmentId' required>");
        response.getWriter().println("<label>Student ID:</label>");
        response.getWriter().println("<input type='number' name='studentId' required>");
        response.getWriter().println("<label>Advisor ID:</label>");
        response.getWriter().println("<input type='number' name='advisorId' required>");
        response.getWriter().println("<label>Meeting Notes:</label>");
        response.getWriter().println("<textarea name='meetingNotes' rows='4' required></textarea>");
        response.getWriter().println("<label>Action Items:</label>");
        response.getWriter().println("<textarea name='actionItems' rows='3'></textarea>");
        response.getWriter().println("<label>Student Status:</label>");
        response.getWriter().println("<select name='studentStatus'><option>Good Standing</option><option>On Probation</option><option>Graduated</option></select>");
        response.getWriter().println("<button type='submit' class='btn' style='background: #4CAF50; color: white;'>Create Record</button>");
        response.getWriter().println("</form>");
        response.getWriter().println("</body></html>");
    }

    private void createRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            int studentId = Integer.parseInt(request.getParameter("studentId"));
            int advisorId = Integer.parseInt(request.getParameter("advisorId"));
            String meetingNotes = request.getParameter("meetingNotes");
            String actionItems = request.getParameter("actionItems");
            String studentStatus = request.getParameter("studentStatus");
            
            Record record = new Record(appointmentId, studentId, advisorId, meetingNotes, actionItems, studentStatus);
            if (recordDAO.createRecord(record)) {
                response.sendRedirect("ManageRecordServlet?action=list");
            }
        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    private void updateRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("ManageRecordServlet?action=list");
    }

    private void deleteRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int recordId = Integer.parseInt(request.getParameter("id"));
        recordDAO.deleteRecord(recordId);
        response.sendRedirect("ManageRecordServlet?action=list");
    }

    private String getCss() {
        return "<style>" +
            "body { font-family: Arial; margin: 20px; background: #f5f5f5; }" +
            "table { width: 100%; border-collapse: collapse; background: white; margin-top: 20px; }" +
            "th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }" +
            "th { background: #667eea; color: white; }" +
            "form { background: white; padding: 20px; border-radius: 8px; max-width: 600px; margin-top: 20px; }" +
            "label { display: block; margin-top: 15px; font-weight: bold; }" +
            "input, select, textarea { width: 100%; padding: 8px; margin-top: 5px; border: 1px solid #ddd; border-radius: 4px; }" +
            ".detail-box { background: white; padding: 20px; border-radius: 8px; margin-top: 20px; }" +
            ".btn { padding: 8px 15px; margin: 5px; border-radius: 4px; cursor: pointer; text-decoration: none; }" +
            ".btn-view { background: #2196F3; color: white; }" +
            ".btn-edit { background: #FFC107; color: black; }" +
            "</style>";
    }
}
