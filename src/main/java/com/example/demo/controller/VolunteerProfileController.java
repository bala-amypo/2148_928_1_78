package com.example.demo.controller;

import com.example.demo.model.VolunteerProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
@Tag(name = "Volunteer Profile", description = "Operations on volunteer profiles")
public class VolunteerProfileController {

    @PostMapping
    @Operation(summary = "Create a new volunteer")
    public ResponseEntity<VolunteerProfile> createVolunteer(@RequestBody VolunteerProfile volunteer) {
        // TODO: implement creation logic
        return ResponseEntity.ok(volunteer);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a volunteer by ID")
    public ResponseEntity<VolunteerProfile> getVolunteer(@PathVariable Long id) {
        // TODO: implement get logic
        return ResponseEntity.ok(new VolunteerProfile());
    }

    @GetMapping
    @Operation(summary = "List all volunteers")
    public ResponseEntity<List<VolunteerProfile>> listVolunteers() {
        // TODO: implement listing logic
        return ResponseEntity.ok(List.of());
    }

    @PutMapping("/{id}/availability")
    @Operation(summary = "Update volunteer availability status")
    public ResponseEntity<VolunteerProfile> updateAvailability(@PathVariable Long id,
                                                               @RequestParam String status) {
        // TODO: implement update availability
        return ResponseEntity.ok(new VolunteerProfile());
    }

    @GetMapping("/lookup/{volunteerId}")
    @Operation(summary = "Lookup volunteer by volunteerId")
    public ResponseEntity<VolunteerProfile> lookupByVolunteerId(@PathVariable String volunteerId) {
        // TODO: implement lookup logic
        return ResponseEntity.ok(new VolunteerProfile());
    }
}
