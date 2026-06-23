package com.aas.controller;

import com.aas.dao.ScheduleDAO;
import com.aas.model.Schedule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ScheduleServlet")
public class ScheduleServlet extends HttpServlet {
    private ScheduleDAO scheduleDAO;

    @Override
    public void init() { scheduleDAO = new ScheduleDAO(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/Login/login.jsp");
            return;
        }

        String userRole = (String) session.getAttribute("role");
        if ("student".equalsIgnoreCase(userRole)) {
            response.sendRedirect(request.getContextPath() + "/ViewAppointmentServlet");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) { 
            action = "admin".equalsIgnoreCase(userRole) ? "adminDashboard" : "dashboard";
        }

        try {
            switch (action) {
                case "checkDate": checkBookedSlots(request, response); break;
                case "dashboard": showDashboard(request, response); break;
                case "new": showNewForm(request, response); break;
                case "edit": showEditForm(request, response); break;
                case "delete": deleteSchedule(request, response); break;
                case "list": listSchedule(request, response); break;
                case "adminDashboard": showAdminDashboard(request, response); break;
                case "adminList": listAdminSchedule(request, response); break;
                case "adminNew": showAdminNewForm(request, response); break;
                default: response.sendRedirect(request.getContextPath() + "/index.jsp"); break;
            }
        } catch (Exception ex) { throw new ServletException(ex); }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("insert".equals(action)) { insertSchedule(request, response); }
            else if ("update".equals(action)) { updateSchedule(request, response); }
            else if ("deleteBulk".equals(action)) { deleteBulk(request, response); }
        } catch (Exception ex) { throw new ServletException(ex); }
    }

    private void checkBookedSlots(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String advIdParam = request.getParameter("advisorId");
        HttpSession session = request.getSession(false);
        int advisorId = (advIdParam != null && !advIdParam.isEmpty()) ? Integer.parseInt(advIdParam) : (Integer) session.getAttribute("userId"); 
        String dateStr = request.getParameter("date");
        if (dateStr != null && !dateStr.isEmpty()) {
            Date date = Date.valueOf(dateStr);
            List<String> bookedSlots = scheduleDAO.getBookedTimeSlots(advisorId, date);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(String.join(",", bookedSlots));
        }
    }

    private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        int advisorId = (Integer) session.getAttribute("userId"); // DYNAMIC CALL FIXED
        request.setAttribute("totalAvailable", scheduleDAO.countSchedulesByStatus(advisorId, "Available"));
        request.setAttribute("totalBusy", scheduleDAO.countSchedulesByStatus(advisorId, "Busy"));
        List<Schedule> freeSchedules = new ArrayList<>();
        for (Schedule s : scheduleDAO.getAllSchedulesByAdvisor(advisorId)) {
            if ("Available".equals(s.getStatus())) { freeSchedules.add(s); }
        }
        request.setAttribute("freeSchedules", freeSchedules); 
        request.getRequestDispatcher("/Schedule/advisor/advisorDashboard.jsp").forward(request, response);
    }

    private void listSchedule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        int advisorId = (Integer) session.getAttribute("userId");
        request.setAttribute("listSchedule", scheduleDAO.getAllSchedulesByAdvisor(advisorId));
        request.getRequestDispatcher("/Schedule/scheduleList.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/Schedule/addSchedule.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        request.setAttribute("schedule", scheduleDAO.getScheduleById(id));
        request.getRequestDispatcher("true".equals(request.getParameter("isAdmin")) ? "/Schedule/admin/adminEditSchedule.jsp" : "/Schedule/editSchedule.jsp").forward(request, response);
    }

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

    private void insertSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        String advIdParam = request.getParameter("advisorId");
        int advisorId = (advIdParam != null && !advIdParam.isEmpty()) ? Integer.parseInt(advIdParam) : (Integer) session.getAttribute("userId"); 
        Date scheduleDate = Date.valueOf(request.getParameter("scheduleDate"));
        String timeSlot = request.getParameter("timeSlot");
        java.sql.Time startTime, endTime;

        if ("custom".equals(timeSlot)) {
            String customStart = request.getParameter("customStartTime");
            String customEnd = request.getParameter("customEndTime");
            
            // Auto-check length: kalau format HH:mm (5 huruf), baru append :00 saat
            startTime = java.sql.Time.valueOf(customStart.length() == 5 ? customStart + ":00" : customStart);
            endTime = java.sql.Time.valueOf(customEnd.length() == 5 ? customEnd + ":00" : customEnd);
        } else {
            String[] times = timeSlot.split("-");
            startTime = java.sql.Time.valueOf(times[0]);
            endTime = java.sql.Time.valueOf(times[1]); // FIX: Letak java.sql.Time penuh kat sini supaya tak error
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
        HttpSession session = request.getSession(false);
        int id = Integer.parseInt(request.getParameter("id"));
        String advIdParam = request.getParameter("advisorId");
        int advisorId = (advIdParam != null && !advIdParam.isEmpty()) ? Integer.parseInt(advIdParam) : (Integer) session.getAttribute("userId"); 
        Date scheduleDate = Date.valueOf(request.getParameter("scheduleDate"));
        String timeSlot = request.getParameter("timeSlot");
        java.sql.Time startTime, endTime;

        if ("custom".equals(timeSlot)) {
            String customStart = request.getParameter("customStartTime");
            String customEnd = request.getParameter("customEndTime");
            startTime = java.sql.Time.valueOf(customStart.length() == 5 ? customStart + ":00" : customStart);
            endTime = java.sql.Time.valueOf(customEnd.length() == 5 ? customEnd + ":00" : customEnd);
        } else {
            String[] times = timeSlot.split("-");
            startTime = java.sql.Time.valueOf(times[0]);
            endTime = java.sql.Time.valueOf(times[1]);
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
        response.sendRedirect(request.getContextPath() + ("true".equals(request.getParameter("isAdmin")) ? "/ScheduleServlet?action=adminList" : "/ScheduleServlet?action=list"));
    }

    private void deleteBulk(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] ids = request.getParameterValues("scheduleIds");
        if (ids != null && ids.length > 0) {
            scheduleDAO.deleteBulkSchedules(ids);
            request.getSession().setAttribute("successMsg", "Success: " + ids.length + " schedules deleted!");
        } else {
            request.getSession().setAttribute("errorMsg", "Error: No schedules selected.");
        }
        response.sendRedirect(request.getContextPath() + ("true".equals(request.getParameter("isAdmin")) ? "/ScheduleServlet?action=adminList" : "/ScheduleServlet?action=list"));
    }
}