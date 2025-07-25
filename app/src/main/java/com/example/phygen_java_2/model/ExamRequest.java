package com.example.phygen_java_2.model;

import java.util.List;

public class ExamRequest {
    private String grade;
    private String semester;
    private String type;
    private List<Integer> questions;

    public ExamRequest(String grade, String semester, String type, List<Integer> questions) {
        this.grade = grade;
        this.semester = semester;
        this.type = type;
        this.questions = questions;
    }

    // Getters và setters
    public String getGrade() { return grade; }
    public String getSemester() { return semester; }
    public String getType() { return type; }
    public List<Integer> getQuestions() { return questions; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setSemester(String semester) { this.semester = semester; }
    public void setType(String type) { this.type = type; }
    public void setQuestions(List<Integer> questions) { this.questions = questions; }
}
