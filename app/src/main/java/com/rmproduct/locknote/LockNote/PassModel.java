package com.rmproduct.locknote.LockNote;

public class PassModel {

    public PassModel() {
    }

    int id, keys;
    String date, time, title, userId, pass;

    public PassModel(int id, int keys, String date, String time, String title, String userId, String pass) {
        this.id = id;
        this.keys = keys;
        this.date = date;
        this.time = time;
        this.title = title;
        this.userId = userId;
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
