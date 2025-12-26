package com.example.demo.service.impl;

import com.example.demo.model.VolunteerProfile;
import com.example.demo.repository.VolunteerProfileRepository;
import com.example.demo.service.VolunteerProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerProfileServiceImpl implements VolunteerProfileService {

    private final VolunteerProfileRepository repository;

    public VolunteerProfileServiceImpl(VolunteerProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public VolunteerProfile createVolunteerProfile(VolunteerProfile profile) {
        return repository.save(profile);
    }

    @Override
    public Optional<VolunteerProfile> getVolunteerProfileById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<VolunteerProfile> getVolunteerProfileByVolunteerId(String volunteerId) {
        return repository.findByVolunteerId(volunteerId);
    }

    @Override
    public List<VolunteerProfile> getVolunteersByAvailabilityStatus(String status) {
        return repository.findByAvailabilityStatus(status);
    }

    @Override
    public VolunteerProfile updateVolunteerProfile(Long id, VolunteerProfile updatedProfile) {
        updatedProfile.setId(id);
        return repository.save(updatedProfile);
    }

    @Override
    public void deleteVolunteerProfile(Long id) {
        repository.deleteById(id);
    }
}
