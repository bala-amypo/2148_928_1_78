package com.example.demo.repository;

import com.example.demo.model.VolunteerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolunteerProfileRepository extends JpaRepository<VolunteerProfile, Long> {

    Optional<VolunteerProfile> findByVolunteerId(String volunteerId);

    Optional<VolunteerProfile> findByEmail(String email);

    Optional<VolunteerProfile> findByPhone(String phone);
}
