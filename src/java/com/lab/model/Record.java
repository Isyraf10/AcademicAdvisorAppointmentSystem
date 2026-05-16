package com.lab.model;

/**
 * Record Model Class - Represents consultation records and academic progress tracking
 * 
 * @author isyra
 */
public class Record {
    private int recordId;
    private int appointmentId;
    private int studentId;
    private int advisorId;
    private String meetingNotes;      // Meeting summary/notes from advisor
    private String feedback;           // Student feedback on the session
    private String actionItems;        // Action items from the meeting
    private String studentStatus;      // e.g., Graduated, Good Standing, On Probation
    private String recordStatus;       // Active, Archived, etc.
    private String createdAt;
    private String updatedAt;

    /**
     * Full constructor
     */
    public Record(int recordId, int appointmentId, int studentId, int advisorId, 
                 String meetingNotes, String feedback, String actionItems, 
                 String studentStatus, String recordStatus, String createdAt, String updatedAt) {
        this.recordId = recordId;
        this.appointmentId = appointmentId;
        this.studentId = studentId;
        this.advisorId = advisorId;
        this.meetingNotes = meetingNotes;
        this.feedback = feedback;
        this.actionItems = actionItems;
        this.studentStatus = studentStatus;
        this.recordStatus = recordStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Constructor for creating new record (without ID and timestamps)
     */
    public Record(int appointmentId, int studentId, int advisorId, String meetingNotes, 
                 String actionItems, String studentStatus) {
        this.appointmentId = appointmentId;
        this.studentId = studentId;
        this.advisorId = advisorId;
        this.meetingNotes = meetingNotes;
        this.actionItems = actionItems;
        this.studentStatus = studentStatus;
        this.recordStatus = "Active";
    }

    // Getters and Setters

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(int advisorId) {
        this.advisorId = advisorId;
    }

    public String getMeetingNotes() {
        return meetingNotes;
    }

    public void setMeetingNotes(String meetingNotes) {
        this.meetingNotes = meetingNotes;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getActionItems() {
        return actionItems;
    }

    public void setActionItems(String actionItems) {
        this.actionItems = actionItems;
    }

    public String getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
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

    @Override
    public String toString() {
        return "Record{" +
                "recordId=" + recordId +
                ", appointmentId=" + appointmentId +
                ", studentId=" + studentId +
                ", advisorId=" + advisorId +
                ", studentStatus='" + studentStatus + '\'' +
                ", recordStatus='" + recordStatus + '\'' +
                '}';
    }
}
