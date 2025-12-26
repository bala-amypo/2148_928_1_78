package com.example.demo.service.impl;

import com.example.demo.model.VolunteerSkillRecord;
import com.example.demo.repository.VolunteerSkillRecordRepository;
import com.example.demo.service.VolunteerSkillService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VolunteerSkillServiceImpl implements VolunteerSkillService {

    private final VolunteerSkillRecordRepository repository;

    public VolunteerSkillServiceImpl(VolunteerSkillRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public VolunteerSkillRecord addSkill(VolunteerSkillRecord skill) {
        skill.setUpdatedAt(LocalDateTime.now());
        return repository.save(skill);
    }

    @Override
    public Optional<VolunteerSkillRecord> getSkillById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<VolunteerSkillRecord> getSkillsByVolunteerId(Long volunteerId) {
        return repository.findByVolunteerId(volunteerId);
    }

    @Override
    public List<VolunteerSkillRecord> getSkillsBySkillName(String skillName) {
        return repository.findBySkillName(skillName);
    }

    @Override
    public List<VolunteerSkillRecord> getSkillsBySkillNameAndMinLevel(String skillName, Integer minLevel) {
        return repository.findBySkillNameAndSkillLevelGreaterThanEqual(skillName, minLevel);
    }

    @Override
    public List<VolunteerSkillRecord> getCertifiedSkillsBySkillName(String skillName) {
        return repository.findBySkillNameAndCertifiedTrue(skillName);
    }

    @Override
    public VolunteerSkillRecord updateSkill(Long id, VolunteerSkillRecord skill) {
        skill.setId(id);
        skill.setUpdatedAt(LocalDateTime.now());
        return repository.save(skill);
    }

    @Override
    public void deleteSkill(Long id) {
        repository.deleteById(id);
    }
}
