package com.example.phygen_java.model;

public class Question {
    private String content;
    private String questionSource;
    private String difficultyLevel;
    private int lessonId;
    private int createdByUserId;

    public Question(String content, String questionSource, String difficultyLevel, int lessonId, int createdByUserId) {
        this.content = content;
        this.questionSource = questionSource;
        this.difficultyLevel = difficultyLevel;
        this.lessonId = lessonId;
        this.createdByUserId = createdByUserId;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getQuestionSource() {
        return questionSource;
    }
    public void setQuestionSource(String questionSource) {
        this.questionSource = questionSource;
    }
    public String getDifficultyLevel() {
        return difficultyLevel;
    }
    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    public int getLessonId() {
        return lessonId;
    }
    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }
    public int getCreatedByUserId() {
        return createdByUserId;
    }
    public void setCreatedByUserId(int createdByUserId) {
        this.createdByUserId = createdByUserId;
    }
}
