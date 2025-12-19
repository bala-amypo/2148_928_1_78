package com.example.demo.service.impl;

import com.example.demo.model.VolunteerProfile;
import com.example.demo.repository.VolunteerProfileRepository;
import com.example.demo.service.VolunteerProfileService;
import com.example.demo.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class VolunteerServiceImpl implements VolunteerProfileService {

    private final VolunteerProfileRepository repository;

    public VolunteerServiceImpl(VolunteerProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public VolunteerProfile createVolunteer(VolunteerProfile volunteer) {
        return repository.save(volunteer);
    }

    @Override
    public VolunteerProfile updateAvailability(Long id, String availabilityStatus) {
        VolunteerProfile volunteer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found"));

        volunteer.setAvailabilityStatus(availabilityStatus);
        return repository.save(volunteer);
    }

    @Override
    public VolunteerProfile getVolunteerById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found"));
    }
}
