package com.example.demo.controller;

import com.example.demo.model.VolunteerProfile;
import com.example.demo.service.VolunteerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerProfileController {

    private final VolunteerService volunteerService;

    public VolunteerProfileController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping
    public VolunteerProfile createVolunteer(@Valid @RequestBody VolunteerProfile volunteer) {
        return volunteerService.createVolunteer(volunteer);
    }

    @PutMapping("/{id}/availability")
    public VolunteerProfile updateAvailability(
            @PathVariable Long id,
            @RequestParam String status) {

        return volunteerService.updateAvailability(id, status);
    }

    @GetMapping
    public List<VolunteerProfile> getAllVolunteers() {
        return volunteerService.getAllVolunteers();
    }
}
