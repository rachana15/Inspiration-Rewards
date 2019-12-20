package com.rachana.inspirationrewards;

import java.io.Serializable;

public class RewardRecords implements Serializable {
    public String studentId = "";
    public String username = "";
    public String password = "";
    public String name= "";
    public String date = "";
    public String notes = "";
    public int value = 0;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUsernameRecord() {
        return username;
    }

    public void setUsernameRecord(String username) {
        this.username = username;
    }

    public String getPasswordRecord() {
        return password;
    }

    public void setPasswordRecord(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
