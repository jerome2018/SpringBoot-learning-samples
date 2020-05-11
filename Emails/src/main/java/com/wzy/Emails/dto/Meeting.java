package com.wzy.Emails.dto;

import java.util.Date;

public class Meeting {

    private String meetName;
    private String meetUser;
    private String meetDate;

    public String getMeetName() {
        return meetName;
    }

    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    public String getMeetUser() {
        return meetUser;
    }

    public void setMeetUser(String meetUser) {
        this.meetUser = meetUser;
    }

    public String getMeetDate() {
        return meetDate;
    }

    public void setMeetDate(String meetDate) {
        this.meetDate = meetDate;
    }
}
