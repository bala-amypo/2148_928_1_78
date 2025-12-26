package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class AvailabilityUpdateRequest {

    @NotBlank(message = "Availability status is required")
    private String availabilityStatus;

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
