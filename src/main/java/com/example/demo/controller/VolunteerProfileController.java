package com.example.demo.controller;

import com.example.demo.dto.AvailabilityUpdateRequest;
import com.example.demo.model.VolunteerProfile;
import com.example.demo.service.VolunteerProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerProfileController {

    private final VolunteerProfileService volunteerProfileService;

    public VolunteerProfileController(VolunteerProfileService volunteerProfileService) {
        this.volunteerProfileService = volunteerProfileService;
    }

    // CREATE volunteer
    @PostMapping
    public VolunteerProfile createVolunteer(
            @Valid @RequestBody VolunteerProfile volunteer) {
        return volunteerProfileService.createVolunteer(volunteer);
    }

    // UPDATE availability
    @PutMapping("/{id}/availability")
    public VolunteerProfile updateAvailability(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityUpdateRequest request) {

        return volunteerProfileService.updateAvailability(
                id,
                request.getAvailabilityStatus()
        );
    }

    // GET all volunteers
    @GetMapping
    public List<VolunteerProfile> getAllVolunteers() {
        return volunteerProfileService.getAllVolunteers();
    }

    // GET volunteer by ID (optional but recommended)
    @GetMapping("/{id}")
    public VolunteerProfile getVolunteerById(@PathVariable Long id) {
        return volunteerProfileService.getVolunteerById(id);
    }
}
