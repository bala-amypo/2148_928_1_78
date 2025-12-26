package com.example.demo.repository;

import com.example.demo.model.VolunteerSkillRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VolunteerSkillRecordRepository
        extends JpaRepository<VolunteerSkillRecord, Long> {

    List<VolunteerSkillRecord> findByVolunteerId(Long volunteerId);

    // ✅ REQUIRED
    Optional<VolunteerSkillRecord> findByVolunteerIdAndSkillName(
            Long volunteerId, String skillName);

    List<VolunteerSkillRecord> findBySkillName(String skillName);

    List<VolunteerSkillRecord> findBySkillNameAndSkillLevel(
            String skillName, String skillLevel);

    // ✅ REQUIRED (used in service)
    List<VolunteerSkillRecord> findBySkillNameAndSkillLevelGreaterThanEqual(
            String skillName, Integer level);

    // ✅ REQUIRED
    List<VolunteerSkillRecord> findBySkillNameAndCertifiedTrue(
            String skillName);
}
