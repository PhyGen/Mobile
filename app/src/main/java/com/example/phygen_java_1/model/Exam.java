package com.example.phygen_java_1.model;

public class Exam {
    private int id;
    private String name;
    private int lessonId;
    private int examTypeId;
    private int createdByUserId;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public Exam() {}

    public Exam(int id, String name, int lessonId, int examTypeId, int createdByUserId, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.lessonId = lessonId;
        this.examTypeId = examTypeId;
        this.createdByUserId = createdByUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLessonId() {
        return lessonId;
    }

    public int getExamTypeId() {
        return examTypeId;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public void setExamTypeId(int examTypeId) {
        this.examTypeId = examTypeId;
    }

    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
