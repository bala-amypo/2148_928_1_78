package com.example.demo.service.impl;

import com.example.demo.model.VolunteerSkillRecord;
import com.example.demo.repository.VolunteerSkillRecordRepository;
import com.example.demo.service.VolunteerSkillService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public VolunteerSkillRecord getSkillById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public List<VolunteerSkillRecord> getSkillsByVolunteer(Long volunteerId) {
        return repository.findByVolunteerId(volunteerId);
    }

    @Override
    public List<VolunteerSkillRecord> getAllSkills() {
        return repository.findAll();
    }
}
