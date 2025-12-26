package com.example.demo.service;

import com.example.demo.model.VolunteerSkillRecord;
import com.example.demo.repository.VolunteerSkillRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VolunteerSkillService {

    private final VolunteerSkillRecordRepository repository;

    public VolunteerSkillService(VolunteerSkillRecordRepository repository) {
        this.repository = repository;
    }

    public VolunteerSkillRecord addSkill(VolunteerSkillRecord record) {
        return repository.save(record);
    }

    public List<VolunteerSkillRecord> getSkillsByVolunteer(Long volunteerId) {
        return repository.findByVolunteerId(volunteerId);
    }

    public List<VolunteerSkillRecord> findMatchingSkills(String skillName, String skillLevel) {
        return repository.findBySkillNameIgnoreCase(skillName)
                .stream()
                .filter(skill -> skillLevel == null ||
                        skill.getSkillLevel().equalsIgnoreCase(skillLevel))
                .collect(Collectors.toList());
    }
}
