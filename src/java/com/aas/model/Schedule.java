package com.aas.model;

import java.sql.Date;
import java.sql.Time;

public class Schedule {

    private int scheduleId;
    private int advisorId;
    private Date scheduleDate;
    private Time startTime;
    private Time endTime;
    private String status;
    private String location;

    public Schedule() {
    }

    public Schedule(int scheduleId, int advisorId, Date scheduleDate, Time startTime, Time endTime, String status, String location) {
        this.scheduleId = scheduleId;
        this.advisorId = advisorId;
        this.scheduleDate = scheduleDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.location = location;
    }

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

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
