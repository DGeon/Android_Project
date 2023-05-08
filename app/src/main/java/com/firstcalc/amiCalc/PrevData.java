package com.firstcalc.amiCalc;

public class PrevData {


    String date;
    String attendance;
    String leavework;
    String content;
    int overtime;

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public PrevData(){}

    public PrevData(String date, String attendance, String leavework, String content){
        this.date = date;
        this.attendance = attendance;
        this.leavework = leavework;
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getLeavework() {
        return leavework;
    }

    public void setLeavework(String leavework) {
        this.leavework = leavework;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
