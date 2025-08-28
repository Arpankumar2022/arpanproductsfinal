package com.arpanbags.products.arpanbagsproducts.dto;

public class RegisterResponse {
    private boolean success;
    private String message;

    // Constructors
    public RegisterResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters & setters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
