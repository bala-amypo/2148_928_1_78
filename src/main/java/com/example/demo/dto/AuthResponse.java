package com.example.demo.dto;

public class AuthResponse {

    private Long userId;
    private String message;

    public AuthResponse(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}
