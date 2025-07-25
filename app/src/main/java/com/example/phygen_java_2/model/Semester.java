package com.example.phygen_java_2.model;

public class Semester {
    private int id;
    private String name;
    private int gradeId;
    private String createdAt;
    private String updatedAt;

    // Constructor
    public Semester(int id, String name, int gradeId, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.gradeId = gradeId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getGradeId() { return gradeId; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGradeId(int gradeId) { this.gradeId = gradeId; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt;}
}