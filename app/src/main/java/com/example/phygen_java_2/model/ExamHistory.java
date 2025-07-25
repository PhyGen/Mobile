package com.example.phygen_java_2.model;

public class ExamHistory {
    private String userId;
    private String username;
    private String createdAt;
    private ExamFormat format;

    public static class ExamFormat {
        private String type;
        private int questions;
        private boolean hasImage;

        public ExamFormat(String type, int questions, boolean hasImage) {
            this.type = type;
            this.questions = questions;
            this.hasImage = hasImage;
        }

        public String getType() { return type; }
        public int getQuestions() { return questions; }
        public boolean isHasImage() { return hasImage; }
    }

    public ExamHistory(String userId, String username, String createdAt, ExamFormat format) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
        this.format = format;
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getCreatedAt() { return createdAt; }
    public ExamFormat getFormat() { return format; }
}