package com.example.demo.controller;

import com.example.demo.model.VolunteerSkillRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
@Tag(name = "Volunteer Skill", description = "Operations on volunteer skills")
public class VolunteerSkillController {

    @PostMapping
    @Operation(summary = "Add or update a volunteer skill")
    public ResponseEntity<VolunteerSkillRecord> addOrUpdateSkill(@RequestBody VolunteerSkillRecord skill) {
        // TODO: implement add/update skill
        return ResponseEntity.ok(skill);
    }

    @GetMapping("/volunteer/{volunteerId}")
    @Operation(summary = "Get all skills for a volunteer")
    public ResponseEntity<List<VolunteerSkillRecord>> getSkillsByVolunteer(@PathVariable Long volunteerId) {
        // TODO: implement fetch skills by volunteer
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a skill by ID")
    public ResponseEntity<VolunteerSkillRecord> getSkill(@PathVariable Long id) {
        // TODO: implement get skill by ID
        return ResponseEntity.ok(new VolunteerSkillRecord());
    }

    @GetMapping
    @Operation(summary = "List all volunteer skills")
    public ResponseEntity<List<VolunteerSkillRecord>> listSkills() {
        // TODO: implement list all skills
        return ResponseEntity.ok(List.of());
    }
}
