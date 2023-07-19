package com.nqmgaming.lab2_minhnqph31902_todoapp.DTO;

public class TodoDTO {
    private int id;
    private String title;
    private String description;
    private String date;
    private String type;
    private int status;

    public TodoDTO() {
    }

    public TodoDTO( String title, String description, String date,String type, int status) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
