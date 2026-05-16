package com.lab.model;

/**
 * Appointment Model Class - Represents an appointment in the Academic Advisor System
 * 
 * @author isyra
 */
public class Appointment {
    private int appointmentId;
    private String title;
    private String type;           // e.g., "Academic Planning", "Course Selection", "Mentoring"
    private int duration;          // Duration in minutes
    private int availableSlots;    // Number of available appointment slots

    /**
     * Constructor with all fields
     */
    public Appointment(int appointmentId, String title, String type, int duration, int availableSlots) {
        this.appointmentId = appointmentId;
        this.title = title;
        this.type = type;
        this.duration = duration;
        this.availableSlots = availableSlots;
    }

    /**
     * Constructor without ID (for new appointments)
     */
    public Appointment(String title, String type, int duration, int availableSlots) {
        this.title = title;
        this.type = type;
        this.duration = duration;
        this.availableSlots = availableSlots;
    }

    // Getters and Setters
    
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", duration=" + duration +
                ", availableSlots=" + availableSlots +
                '}';
    }
}
