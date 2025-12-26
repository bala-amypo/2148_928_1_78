package com.example.demo.service;

import com.example.demo.model.VolunteerSkillRecord;
import com.example.demo.repository.VolunteerSkillRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VolunteerSkillServiceImpl implements VolunteerSkillService {

    @Autowired
    private VolunteerSkillRecordRepository volunteerSkillRepository;

    @Override
    public VolunteerSkillRecord addOrUpdateSkill(VolunteerSkillRecord skill) {
        // Check if skill already exists for this volunteer
        Optional<VolunteerSkillRecord> existingSkill = 
            volunteerSkillRepository.findByVolunteerIdAndSkillName(skill.getVolunteerId(), skill.getSkillName());
        
        if (existingSkill.isPresent()) {
            // Update existing skill
            VolunteerSkillRecord updatedSkill = existingSkill.get();
            updatedSkill.setSkillLevel(skill.getSkillLevel());
            updatedSkill.setCertified(skill.getCertified());
            updatedSkill.setUpdatedAt(LocalDateTime.now());
            return volunteerSkillRepository.save(updatedSkill);
        } else {
            // Create new skill
            skill.setUpdatedAt(LocalDateTime.now());
            return volunteerSkillRepository.save(skill);
        }
    }

    @Override
    public List<VolunteerSkillRecord> getSkillsByVolunteerId(Long volunteerId) {
        return volunteerSkillRepository.findByVolunteerId(volunteerId);
    }

    @Override
    public List<VolunteerSkillRecord> getVolunteersBySkill(String skillName) {
        return volunteerSkillRepository.findBySkillName(skillName);
    }

    @Override
    public List<VolunteerSkillRecord> getVolunteersBySkillAndLevel(String skillName, Integer minLevel) {
        return volunteerSkillRepository.findBySkillNameAndSkillLevelGreaterThanEqual(skillName, minLevel);
    }

    @Override
    public List<VolunteerSkillRecord> getCertifiedVolunteersBySkill(String skillName) {
        return volunteerSkillRepository.findBySkillNameAndCertifiedTrue(skillName);
    }

    @Override
    public boolean deleteSkill(Long id) {
        if (volunteerSkillRepository.existsById(id)) {
            volunteerSkillRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<VolunteerSkillRecord> getSkillById(Long id) {
        return volunteerSkillRepository.findById(id);
    }
}