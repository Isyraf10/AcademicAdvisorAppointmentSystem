package com.aas.model;

import java.sql.Timestamp;

/**
 * Record.java — Model (POJO / Java Bean)
 *
 * Represents a single Academic Consultation Log entry that is persisted in the
 * `records` table of the `academic_record_db` database.  The extra display
 * fields (studentName, advisorName, studentId, advisorId) are populated by the
 * DAO via SQL JOINs so that the JSP never has to execute secondary look-ups.
 */
public class Record {

    // ── Primary key ──────────────────────────────────────────────────────────
    private int recordId;

    // ── Foreign key to appointments table ────────────────────────────────────
    private int appointmentId;

    // ── Core consultation content ─────────────────────────────────────────────
    private String summary;
    private String feedback;
    private String actionPlan;

    // ── Operational status (Active / Graduated / Archived) ───────────────────
    private String recordStatus;

    // ── Audit timestamp ───────────────────────────────────────────────────────
    private Timestamp createdDate;

    // ── Denormalised display fields populated by JOIN queries ─────────────────
    private String studentName;
    private String advisorName;
    private int    studentId;
    private int    advisorId;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Record() {
        this.recordStatus = "Active";
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getRecordId() { return recordId; }
    public void setRecordId(int recordId) { this.recordId = recordId; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public String getActionPlan() { return actionPlan; }
    public void setActionPlan(String actionPlan) { this.actionPlan = actionPlan; }

    public String getRecordStatus() { return recordStatus; }
    public void setRecordStatus(String recordStatus) { this.recordStatus = recordStatus; }

    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getAdvisorId() { return advisorId; }
    public void setAdvisorId(int advisorId) { this.advisorId = advisorId; }

    @Override
    public String toString() {
        return "Record{recordId=" + recordId
                + ", appointmentId=" + appointmentId
                + ", studentName='" + studentName + '\''
                + ", recordStatus='" + recordStatus + '\''
                + ", createdDate=" + createdDate + '}';
    }
}
