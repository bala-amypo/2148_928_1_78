package com.example.demo.controller;

import com.example.demo.model.VolunteerProfile;
import com.example.demo.service.VolunteerProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/volunteers")
@Tag(name = "Volunteer Management", description = "APIs for managing volunteers")
public class VolunteerProfileController {

    @Autowired
    private VolunteerProfileService service;

    @PostMapping
    @Operation(summary = "Create a new volunteer")
    public ResponseEntity<VolunteerProfile> createVolunteer(@RequestBody VolunteerProfile volunteerProfile) {
        VolunteerProfile created = service.createVolunteer(volunteerProfile);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/id/{volunteerId}")
    @Operation(summary = "Get volunteer by volunteer ID")
    public ResponseEntity<VolunteerProfile> findByVolunteerId(@PathVariable String volunteerId) {
        Optional<VolunteerProfile> volunteer = service.findByVolunteerId(volunteerId);
        return volunteer.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get volunteer by ID")
    public ResponseEntity<VolunteerProfile> getVolunteerById(@PathVariable Long id) {
        VolunteerProfile volunteer = service.getVolunteerById(id);
        return ResponseEntity.ok(volunteer);
    }

    @GetMapping
    @Operation(summary = "Get all volunteers")
    public ResponseEntity<List<VolunteerProfile>> getAllVolunteers() {
        List<VolunteerProfile> volunteers = service.getAllVolunteers();
        return ResponseEntity.ok(volunteers);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a volunteer")
    public ResponseEntity<VolunteerProfile> updateVolunteer(@PathVariable Long id, 
                                                            @RequestBody VolunteerProfile volunteerProfile) {
        VolunteerProfile updated = service.updateVolunteer(id, volunteerProfile);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a volunteer")
    public ResponseEntity<Void> deleteVolunteer(@PathVariable Long id) {
        service.deleteVolunteer(id);
        return ResponseEntity.noContent().build();
    }
}