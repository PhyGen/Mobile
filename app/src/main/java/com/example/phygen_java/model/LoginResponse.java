package com.example.phygen_java.model;

public class LoginResponse {
    private boolean success;
    private String token;
    private User user;

    public LoginResponse() {

    }
    public LoginResponse(boolean success, String token, User user) {
        this.success = success;
        this.token = token;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
