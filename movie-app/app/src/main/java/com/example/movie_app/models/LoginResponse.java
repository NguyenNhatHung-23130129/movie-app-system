package com.example.movie_app.models;

public class LoginResponse {
    private boolean success;
    private String message;
    private String token; // Chuỗi JWT dùng để xác thực các API khác

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}