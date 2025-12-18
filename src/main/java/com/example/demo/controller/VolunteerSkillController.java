package com.example.demo.controller;

import com.example.demo.model.VolunteerSkillRecord;
import com.example.demo.service.VolunteerSkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class VolunteerSkillController {

    private final VolunteerSkillService volunteerSkillService;

    public VolunteerSkillController(VolunteerSkillService volunteerSkillService) {
        this.volunteerSkillService = volunteerSkillService;
    }

    @PostMapping
    public VolunteerSkillRecord addSkill(@RequestBody VolunteerSkillRecord record) {
        return volunteerSkillService.addSkill(record);
    }

    @GetMapping("/volunteer/{volunteerId}")
    public List<VolunteerSkillRecord> getSkills(@PathVariable Long volunteerId) {
        return volunteerSkillService.getSkillsByVolunteer(volunteerId);
    }
}
