package com.example.phygen_java_2.model;

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
