package com.example.demo.service.impl;

import com.example.demo.dto.AvailabilityUpdateRequest;
import com.example.demo.exception.ResourceNotFoundException;
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
        // Optional: Add validation here if needed
        return repository.save(volunteer);
    }

    @Override
    public VolunteerProfile updateAvailability(Long id, String availabilityStatus) {
        // Check if volunteer exists, throw ResourceNotFoundException if not
        VolunteerProfile volunteer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));

        volunteer.setAvailabilityStatus(availabilityStatus);
        return repository.save(volunteer);
    }

    @Override
    public List<VolunteerProfile> getAllVolunteers() {
        return repository.findAll();
    }
}
