package com.example.demo.service;

import com.example.demo.model.VolunteerSkillRecord;
import java.util.List;
import java.util.Optional;

public interface VolunteerSkillService {
    VolunteerSkillRecord addOrUpdateSkill(VolunteerSkillRecord skill);
    List<VolunteerSkillRecord> getSkillsByVolunteerId(Long volunteerId);
    List<VolunteerSkillRecord> getVolunteersBySkill(String skillName);
    List<VolunteerSkillRecord> getVolunteersBySkillAndLevel(String skillName, Integer minLevel);
    List<VolunteerSkillRecord> getCertifiedVolunteersBySkill(String skillName);
    boolean deleteSkill(Long id);
    Optional<VolunteerSkillRecord> getSkillById(Long id);
}