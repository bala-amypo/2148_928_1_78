package com.example.demo.repository;

import com.example.demo.model.VolunteerSkillRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerSkillRecordRepository extends JpaRepository<VolunteerSkillRecord, Long> {
    List<VolunteerSkillRecord> findByVolunteerId(Long volunteerId);
    List<VolunteerSkillRecord> findBySkillName(String skillName);
    List<VolunteerSkillRecord> findBySkillNameAndSkillLevelGreaterThanEqual(String skillName, Integer minLevel);
    List<VolunteerSkillRecord> findBySkillNameAndCertifiedTrue(String skillName);
    Optional<VolunteerSkillRecord> findByVolunteerIdAndSkillName(Long volunteerId, String skillName);
}