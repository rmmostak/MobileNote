package com.rmproduct.mobilenote;

public class Model {

    public Model() {
    }

    private String date, time, title, body;
    private int id;

    public Model(String date, String time, String title, String body, int id) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.body = body;
        this.id = id;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
