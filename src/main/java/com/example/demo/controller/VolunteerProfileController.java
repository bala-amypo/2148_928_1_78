package com.example.demo.controller;

import com.example.demo.dto.AvailabilityUpdateRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.VolunteerProfile;
import com.example.demo.service.VolunteerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerProfileController {

    private final VolunteerService volunteerService;

    public VolunteerProfileController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    // Create a new volunteer
    @PostMapping
    public ResponseEntity<?> createVolunteer(@Valid @RequestBody VolunteerProfile volunteer) {
        try {
            VolunteerProfile savedVolunteer = volunteerService.createVolunteer(volunteer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVolunteer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating volunteer: " + e.getMessage());
        }
    }

    // Update availability using JSON body
    @PutMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityUpdateRequest request) {
        try {
            VolunteerProfile updatedVolunteer = volunteerService.updateAvailability(id, request.getAvailabilityStatus());
            return ResponseEntity.ok(updatedVolunteer);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating availability: " + e.getMessage());
        }
    }

    // Get all volunteers
    @GetMapping
    public ResponseEntity<List<VolunteerProfile>> getAllVolunteers() {
        return ResponseEntity.ok(volunteerService.getAllVolunteers());
    }
}
