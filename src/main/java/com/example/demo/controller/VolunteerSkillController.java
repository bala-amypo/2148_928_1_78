package com.example.demo.controller;

import com.example.demo.model.VolunteerSkillRecord;
import com.example.demo.service.VolunteerSkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@Tag(name = "Volunteer Skills", description = "APIs for managing volunteer skills")
public class VolunteerSkillController {

    @Autowired
    private VolunteerSkillService service;

    @PostMapping
    @Operation(summary = "Add or update a skill")
    public ResponseEntity<VolunteerSkillRecord> addOrUpdateSkill(
            @RequestBody VolunteerSkillRecord skillRecord) {
        VolunteerSkillRecord result = service.addOrUpdateSkill(skillRecord);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/volunteer/{volunteerId}")
    @Operation(summary = "Get skills by volunteer ID")
    public ResponseEntity<List<VolunteerSkillRecord>> getSkillsByVolunteer(
            @PathVariable Long volunteerId) {
        List<VolunteerSkillRecord> skills = service.getSkillsByVolunteer(volunteerId);
        return ResponseEntity.ok(skills);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        service.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
}