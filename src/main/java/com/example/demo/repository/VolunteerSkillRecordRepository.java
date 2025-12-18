package com.example.demo.repository;

import com.example.demo.model.VolunteerSkillRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerSkillRecordRepository
        extends JpaRepository<VolunteerSkillRecord, Long> {

    List<VolunteerSkillRecord> findByVolunteerId(Long volunteerId);

    List<VolunteerSkillRecord> findBySkillName(String skillName);
}
