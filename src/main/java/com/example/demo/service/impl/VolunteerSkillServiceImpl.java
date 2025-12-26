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
    public VolunteerSkillRecord addOrUpdateSkill(VolunteerSkillRecord skill) {

        VolunteerSkillRecord existing =
                repository.findByVolunteerIdAndSkillName(
                                skill.getVolunteerId(),
                                skill.getSkillName())
                        .orElse(skill);

        existing.setSkillLevel(skill.getSkillLevel());
        existing.setCertified(skill.getCertified());
        existing.setUpdatedAt(LocalDateTime.now());

        return repository.save(existing);
    }

    @Override
    public Optional<VolunteerSkillRecord> getSkillById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<VolunteerSkillRecord> getSkillsByVolunteer(Long volunteerId) {
        return repository.findByVolunteerId(volunteerId);
    }

    @Override
    public List<VolunteerSkillRecord> getAllSkills() {
        return repository.findAll();
    }

    // 🔴 REQUIRED BY INTERFACE
    @Override
    public List<VolunteerSkillRecord> getCertifiedVolunteersBySkill(
            String skillName) {
        return repository.findBySkillNameAndCertifiedTrue(skillName);
    }

    @Override
    public boolean deleteSkill(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
