package com.aas.controller;

import com.aas.dao.ScheduleDAO;
import com.aas.model.Schedule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ScheduleServlet")
public class ScheduleServlet extends HttpServlet {
    private ScheduleDAO scheduleDAO;

    @Override
    public void init() { 
        scheduleDAO = new ScheduleDAO(); 
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Grab session and check login state
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/Login/login.jsp");
            return;
        }

        // 2. Read 'role' (matching the key inside LoginServlet)
        String userRole = (String) session.getAttribute("role");

        // 3. Authorization Gate: Students should never access schedule management controls
        if ("student".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/ViewAppointmentServlet");
            return;
        }

        String action = request.getParameter("action");
        
        // Auto-route based on Session Role if no action is provided
        if (action == null) { 
            if ("admin".equalsIgnoreCase(userRole)) { 
                action = "adminDashboard"; 
            } else { 
                action = "dashboard"; 
            }
        }

        try {
            switch (action) {
                // --- AJAX LIVE CHECK ---
                case "checkDate": 
                    checkBookedSlots(request, response); 
                    break;

                // --- ADVISOR ROUTES ---
                case "dashboard": 
                    showDashboard(request, response); 
                    break;
                case "new": 
                    showNewForm(request, response); 
                    break;
                case "edit": 
                    showEditForm(request, response); 
                    break;
                case "delete": 
                    deleteSchedule(request, response); 
                    break;
                case "list": 
                    listSchedule(request, response); 
                    break;
                
                // --- ADMIN ROUTES ---
                case "adminDashboard": 
                    showAdminDashboard(request, response); 
                    break;
                case "adminList": 
                    listAdminSchedule(request, response); 
                    break;
                case "adminNew": 
                    showAdminNewForm(request, response); 
                    break;
                
                default: 
                    response.sendRedirect(request.getContextPath() + "/index.jsp"); 
                    break;
            }
        } catch (Exception ex) { 
            throw new ServletException(ex); 
        }
    } // <-- Properly closes the doGet method now!

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("insert".equals(action)) { 
                insertSchedule(request, response); 
            } else if ("update".equals(action)) { 
                updateSchedule(request, response); 
            } else if ("deleteBulk".equals(action)) { 
                deleteBulk(request, response); 
            }
        } catch (Exception ex) { 
            throw new ServletException(ex); 
        }
    }

    // ==========================================
    // AJAX BACKEND FUNCTION
    // ==========================================
    private void checkBookedSlots(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String advIdParam = request.getParameter("advisorId");
        int advisorId = (advIdParam != null && !advIdParam.isEmpty()) ? Integer.parseInt(advIdParam) : 1; 
        String dateStr = request.getParameter("date");
        
        if (dateStr != null && !dateStr.isEmpty()) {
            Date date = Date.valueOf(dateStr);
            List<String> bookedSlots = scheduleDAO.getBookedTimeSlots(advisorId, date);
            
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.join(",", bookedSlots));
        }
    }

    // ==========================================
    // ADVISOR FUNCTIONS
    // ==========================================
    private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int advisorId = 1; 
        request.setAttribute("totalAvailable", scheduleDAO.countSchedulesByStatus(advisorId, "Available"));
        request.setAttribute("totalBusy", scheduleDAO.countSchedulesByStatus(advisorId, "Busy"));
        
        List<Schedule> freeSchedules = new ArrayList<>();
        for (Schedule s : scheduleDAO.getAllSchedulesByAdvisor(advisorId)) {
            if ("Available".equals(s.getStatus())) { 
                freeSchedules.add(s); 
            }
        }
        request.setAttribute("freeSchedules", freeSchedules); 
        request.getRequestDispatcher("/Schedule/advisor/advisorDashboard.jsp").forward(request, response);
    }

    private void listSchedule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("listSchedule", scheduleDAO.getAllSchedulesByAdvisor(1));
        request.getRequestDispatcher("/Schedule/scheduleList.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/Schedule/addSchedule.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("schedule", scheduleDAO.getScheduleById(id));
        if ("true".equals(request.getParameter("isAdmin"))) {
            request.getRequestDispatcher("/Schedule/admin/adminEditSchedule.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/Schedule/editSchedule.jsp").forward(request, response);
        }
    }

    // ==========================================
    // ADMIN FUNCTIONS
    // ==========================================
    private void showAdminDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("totalAvailable", scheduleDAO.countAllSchedulesByStatus("Available"));
        request.setAttribute("totalBusy", scheduleDAO.countAllSchedulesByStatus("Busy"));
        request.getRequestDispatcher("/Schedule/admin/adminDashboard.jsp").forward(request, response);
    }

    private void listAdminSchedule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("listSchedule", scheduleDAO.getAllSchedules());
        request.getRequestDispatcher("/Schedule/admin/adminScheduleList.jsp").forward(request, response);
    }

    private void showAdminNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/Schedule/admin/adminAddSchedule.jsp").forward(request, response);
    }

    // ==========================================
    // CRUD OPERATIONS
    // ==========================================
    private void insertSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String advIdParam = request.getParameter("advisorId");
        int advisorId = (advIdParam != null && !advIdParam.isEmpty()) ? Integer.parseInt(advIdParam) : 1; 
        Date scheduleDate = Date.valueOf(request.getParameter("scheduleDate"));
        String timeSlot = request.getParameter("timeSlot");
        Time startTime, endTime;

        if ("custom".equals(timeSlot)) {
            startTime = Time.valueOf(request.getParameter("customStartTime") + ":00");
            endTime = Time.valueOf(request.getParameter("customEndTime") + ":00");
        } else {
            String[] times = timeSlot.split("-");
            startTime = Time.valueOf(times[0]);
            endTime = Time.valueOf(times[1]);
        }
        
        String returnView = (advIdParam != null) ? "ScheduleServlet?action=adminList" : "ScheduleServlet?action=list";

        if (scheduleDAO.isOverlap(advisorId, scheduleDate, startTime, endTime, 0)) {
            request.getSession().setAttribute("errorMsg", "Failed: Time overlaps with an existing schedule!");
            response.sendRedirect(returnView);
            return;
        }

        Schedule newSchedule = new Schedule(0, advisorId, scheduleDate, startTime, endTime, "Available", request.getParameter("location"));
        scheduleDAO.addSchedule(newSchedule);
        request.getSession().setAttribute("successMsg", "Success: New schedule created!");
        response.sendRedirect(returnView);
    }

    private void updateSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String advIdParam = request.getParameter("advisorId");
        int advisorId = (advIdParam != null && !advIdParam.isEmpty()) ? Integer.parseInt(advIdParam) : 1; 
        Date scheduleDate = Date.valueOf(request.getParameter("scheduleDate"));
        String timeSlot = request.getParameter("timeSlot");
        Time startTime, endTime;

        if ("custom".equals(timeSlot)) {
            String customStart = request.getParameter("customStartTime");
            String customEnd = request.getParameter("customEndTime");
            startTime = Time.valueOf(customStart.length() == 5 ? customStart + ":00" : customStart);
            endTime = Time.valueOf(customEnd.length() == 5 ? customEnd + ":00" : customEnd);
        } else {
            String[] times = timeSlot.split("-");
            startTime = Time.valueOf(times[0]);
            endTime = Time.valueOf(times[1]);
        }
        
        String returnView = (advIdParam != null) ? "ScheduleServlet?action=adminList" : "ScheduleServlet?action=list";

        if (scheduleDAO.isOverlap(advisorId, scheduleDate, startTime, endTime, id)) {
            request.getSession().setAttribute("errorMsg", "Failed: Time overlaps with another schedule!");
            response.sendRedirect(returnView);
            return;
        }

        Schedule updatedSchedule = new Schedule(id, advisorId, scheduleDate, startTime, endTime, request.getParameter("status"), request.getParameter("location"));
        scheduleDAO.updateSchedule(updatedSchedule);
        request.getSession().setAttribute("successMsg", "Success: Schedule updated!");
        response.sendRedirect(returnView);
    }

    private void deleteSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
        scheduleDAO.deleteSchedule(Integer.parseInt(request.getParameter("id")));
        request.getSession().setAttribute("successMsg", "Success: Schedule deleted!");
        if ("true".equals(request.getParameter("isAdmin"))) { 
            response.sendRedirect(request.getContextPath() + "/ScheduleServlet?action=adminList"); 
        } else { 
            response.sendRedirect(request.getContextPath() + "/ScheduleServlet?action=list"); 
        }
    }

    private void deleteBulk(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] ids = request.getParameterValues("scheduleIds");
        if (ids != null && ids.length > 0) {
            scheduleDAO.deleteBulkSchedules(ids);
            request.getSession().setAttribute("successMsg", "Success: " + ids.length + " schedules deleted!");
        } else {
            request.getSession().setAttribute("errorMsg", "Error: No schedules selected for deletion.");
        }
        if ("true".equals(request.getParameter("isAdmin"))) { 
            response.sendRedirect(request.getContextPath() + "/ScheduleServlet?action=adminList"); 
        } else { 
            response.sendRedirect(request.getContextPath() + "/ScheduleServlet?action=list"); 
        }
    }
}