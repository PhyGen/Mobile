package com.example.phygen_java_1.model;

public class NotificationItem {
    private int id;
    private String title;
    private String message;

    public NotificationItem(int id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
