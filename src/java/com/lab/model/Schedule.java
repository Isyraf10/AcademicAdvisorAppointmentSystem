package com.lab.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Schedule Model Class - Represents an advisor's available consultation slots
 * 
 * @author isyra
 */
public class Schedule {
    private int scheduleId;
    private int advisorId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;        // Available, Booked, Unavailable
    private int maxCapacity;
    private int currentBookings;
    private String notes;
    private String createdAt;
    private String updatedAt;

    /**
     * Full constructor
     */
    public Schedule(int scheduleId, int advisorId, LocalDate date, LocalTime startTime, 
                   LocalTime endTime, String status, int maxCapacity, int currentBookings, 
                   String notes, String createdAt, String updatedAt) {
        this.scheduleId = scheduleId;
        this.advisorId = advisorId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.maxCapacity = maxCapacity;
        this.currentBookings = currentBookings;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Constructor for creating new schedule (without ID and timestamps)
     */
    public Schedule(int advisorId, LocalDate date, LocalTime startTime, LocalTime endTime, 
                   String status, int maxCapacity, String notes) {
        this.advisorId = advisorId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.maxCapacity = maxCapacity;
        this.currentBookings = 0;
        this.notes = notes;
    }

    // Getters and Setters

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(int advisorId) {
        this.advisorId = advisorId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCurrentBookings() {
        return currentBookings;
    }

    public void setCurrentBookings(int currentBookings) {
        this.currentBookings = currentBookings;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Check if slot is available for booking
     */
    public boolean isAvailableForBooking() {
        return "Available".equals(status) && currentBookings < maxCapacity;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", advisorId=" + advisorId +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status='" + status + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", currentBookings=" + currentBookings +
                '}';
    }
}
