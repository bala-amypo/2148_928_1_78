package com.example.demo.controller;

import com.example.demo.model.VolunteerSkillRecord;
import com.example.demo.service.VolunteerSkillService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class VolunteerSkillController {

    private final VolunteerSkillService service;

    public VolunteerSkillController(VolunteerSkillService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VolunteerSkillRecord> addSkill(
            @RequestBody VolunteerSkillRecord record) {
        return ResponseEntity.ok(service.addSkill(record));
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<VolunteerSkillRecord>> getByVolunteer(
            @PathVariable Long volunteerId) {
        return ResponseEntity.ok(service.getSkillsByVolunteer(volunteerId));
    }
}
