package com.nqmgaming.lab2_minhnqph31902_todoapp.dto;

import java.util.HashMap;

public class TodoDTO {
    private String id;
    private String title;
    private String description;
    private String date;
    private String type;
    private int status;

    public TodoDTO() {
    }

    public TodoDTO(String id,String title, String description, String date, String type, int status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.type = type;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    //hashmap
   public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("title", title);
        hashMap.put("description", description);
        hashMap.put("date", date);
        hashMap.put("type", type);
        hashMap.put("status", status);
        return hashMap;
    }

    @Override
    public String toString() {
        return "TodoDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                '}';
    }
}
