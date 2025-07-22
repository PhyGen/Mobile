package com.example.phygen_java_1.model;

public class GoogleLoginRequest {
    private String credential;

    public GoogleLoginRequest(String credential) {
        this.credential = credential;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}
