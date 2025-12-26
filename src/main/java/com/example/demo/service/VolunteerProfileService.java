package com.example.demo.service;

import com.example.demo.model.VolunteerProfile;
import com.example.demo.repository.VolunteerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VolunteerProfileService {

    private final VolunteerProfileRepository repository;

    public VolunteerProfileService(VolunteerProfileRepository repository) {
        this.repository = repository;
    }

    public VolunteerProfile createProfile(VolunteerProfile profile) {
        if (profile.getAvailabilityStatus() == null) {
            profile.setAvailabilityStatus("AVAILABLE");
        }
        return repository.save(profile);
    }

    public Optional<VolunteerProfile> getByVolunteerId(String volunteerId) {
        return repository.findByVolunteerId(volunteerId);
    }
}
