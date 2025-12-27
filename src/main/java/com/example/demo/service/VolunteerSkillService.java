package com.example.demo.service;

import com.example.demo.model.VolunteerSkillRecord;
import java.util.List;

public interface VolunteerSkillService {
    VolunteerSkillRecord addOrUpdateSkill(VolunteerSkillRecord skillRecord);
    List<VolunteerSkillRecord> getSkillsByVolunteer(Long volunteerId);
    void deleteSkill(Long id);
}