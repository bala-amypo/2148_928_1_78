package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.VolunteerProfile;
import com.example.demo.repository.VolunteerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerProfileService {

    private final VolunteerProfileRepository repository;

    public VolunteerProfileService(VolunteerProfileRepository repository) {
        this.repository = repository;
    }

    public VolunteerProfile createVolunteer(VolunteerProfile profile) {

        if (repository.existsByEmail(profile.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        if (repository.existsByPhone(profile.getPhone())) {
            throw new BadRequestException("Phone already exists");
        }

        return repository.save(profile);
    }

    public List<VolunteerProfile> getAllVolunteers() {
        return repository.findAll();
    }

    public VolunteerProfile getVolunteerById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Volunteer not found"));
    }

    public VolunteerProfile updateAvailability(Long id, String status) {

        VolunteerProfile profile = getVolunteerById(id);
        profile.setAvailabilityStatus(status);
        return repository.save(profile);
    }
}
