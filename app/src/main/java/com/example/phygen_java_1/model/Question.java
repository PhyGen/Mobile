package com.example.phygen_java_1.model;

public class Question {
    private int id;
    private String content;
    private String questionSource;
    private String difficultyLevel;
    private int lessonId;
    private int createdByUserId;
    private String createdAt;
    private String updatedAt;
    private String createdByUserName;
    private String lessonName;

    public Question(String content, String questionSource, String difficultyLevel, int lessonId, int createdByUserId) {
        this.content = content;
        this.questionSource = questionSource;
        this.difficultyLevel = difficultyLevel;
        this.lessonId = lessonId;
        this.createdByUserId = createdByUserId;
    }

    public int getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public String getQuestionSource() {
        return questionSource;
    }
    public String getDifficultyLevel() {
        return difficultyLevel;
    }
    public int getLessonId() {
        return lessonId;
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
    public String getCreatedByUserName() {
        return createdByUserName;
    }
    public String getLessonName() {
        return lessonName;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setQuestionSource(String questionSource) {
        this.questionSource = questionSource;
    }
    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
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
    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }
    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }
}
