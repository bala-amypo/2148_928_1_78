package com.example.demo.service.impl;

import com.example.demo.model.VolunteerProfile;
import com.example.demo.repository.VolunteerProfileRepository;
import com.example.demo.service.VolunteerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerServiceImpl implements VolunteerService {

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
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        volunteer.setAvailabilityStatus(availabilityStatus);
        return repository.save(volunteer);
    }

    @Override
    public List<VolunteerProfile> getAllVolunteers() {
        return repository.findAll();
    }
}
