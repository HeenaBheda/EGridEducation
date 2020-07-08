package com.cloudentro.taegrid;

public class ListClass_Pojo {

    String school_id,subject,standard,time,meetingUrl,className;
    int active;

    public ListClass_Pojo(){

    }

    public ListClass_Pojo(String school_id, String subject, String standard, String time, int active) {
        this.school_id = school_id;
        this.subject = subject;
        this.standard = standard;
        this.time = time;
        this.active = active;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMeetingUrl() {
        return meetingUrl;
    }

    public void setMeetingUrl(String meetingUrl) {
        this.meetingUrl = meetingUrl;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
