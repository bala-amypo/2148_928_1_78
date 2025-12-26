package com.example.demo.controller;

import com.example.demo.model.VolunteerProfile;
import com.example.demo.service.VolunteerProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteers")
public class VolunteerProfileController {

    private final VolunteerProfileService service;

    public VolunteerProfileController(VolunteerProfileService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VolunteerProfile> create(@RequestBody VolunteerProfile profile) {
        return ResponseEntity.ok(service.createProfile(profile));
    }

    @GetMapping("/{volunteerId}")
    public ResponseEntity<VolunteerProfile> get(@PathVariable String volunteerId) {
        return service.getByVolunteerId(volunteerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
