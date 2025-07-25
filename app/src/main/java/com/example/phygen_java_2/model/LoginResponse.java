package com.example.phygen_java_2.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String token;
    private int userId;
    private String role; // Thêm trường role

    // Getters và setters
    public String getToken() { return token; }
    public int getUserId() { return userId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}