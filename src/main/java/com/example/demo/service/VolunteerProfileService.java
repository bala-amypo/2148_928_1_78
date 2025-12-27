package com.example.demo.service;

import com.example.demo.model.VolunteerProfile;
import java.util.List;
import java.util.Optional;

public interface VolunteerProfileService {
    VolunteerProfile createVolunteer(VolunteerProfile volunteerProfile);
    VolunteerProfile getVolunteerById(Long id);
    List<VolunteerProfile> getAllVolunteers();
    VolunteerProfile updateVolunteer(Long id, VolunteerProfile volunteerProfile);
    void deleteVolunteer(Long id);
    Optional<VolunteerProfile> findByVolunteerId(String volunteerId);
}