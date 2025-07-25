package com.example.phygen_java_2.model;

public class Solution {
    private int id;
    private int questionId;
    private String content;
    private String explanation;
    private int createdByUserId;

    public Solution(int questionId, String content, String explanation, int createdByUserId) {
        this.questionId = questionId;
        this.content = content;
        this.explanation = explanation;
        this.createdByUserId = createdByUserId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getQuestionId() {
        return questionId;
    }

    public String getContent() {
        return content;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }
}
