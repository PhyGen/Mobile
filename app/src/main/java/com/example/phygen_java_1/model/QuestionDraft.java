package com.example.phygen_java_1.model;

public class QuestionDraft {
    private int gradeId;
    private int semesterId;
    private int chapterId;
    private int lessonId;
    private String difficulty;

    public QuestionDraft() {
        // Default constructor
    }
    public QuestionDraft(int gradeId, int semesterId, int chapterId, int lessonId, String difficulty) {
        this.gradeId = gradeId;
        this.semesterId = semesterId;
        this.chapterId = chapterId;
        this.lessonId = lessonId;
        this.difficulty = difficulty;
    }
    // Getters and Setters
    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(int semesterId) {
        this.semesterId = semesterId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}