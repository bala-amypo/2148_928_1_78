package com.example.demo.service.impl;

import com.example.demo.model.VolunteerProfile;
import com.example.demo.repository.VolunteerProfileRepository;
import com.example.demo.service.VolunteerProfileService;
import com.example.demo.exception.BadRequestException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VolunteerProfileServiceImpl implements VolunteerProfileService {
    
    private final VolunteerProfileRepository volunteerProfileRepository;
    
    public VolunteerProfileServiceImpl(VolunteerProfileRepository volunteerProfileRepository) {
        this.volunteerProfileRepository = volunteerProfileRepository;
    }
    
    @Override
    public VolunteerProfile createVolunteer(VolunteerProfile volunteerProfile) {
        if (volunteerProfileRepository.existsByVolunteerId(volunteerProfile.getVolunteerId())) {
            throw new BadRequestException("Volunteer ID already exists");
        }
        if (volunteerProfileRepository.existsByEmail(volunteerProfile.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (volunteerProfileRepository.existsByPhone(volunteerProfile.getPhone())) {
            throw new BadRequestException("Phone already exists");
        }
        
        volunteerProfile.setCreatedAt(LocalDateTime.now());
        volunteerProfile.setUpdatedAt(LocalDateTime.now());
        return volunteerProfileRepository.save(volunteerProfile);
    }
    
    @Override
    public VolunteerProfile getVolunteerById(Long id) {
        return volunteerProfileRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Volunteer not found"));
    }
    
    @Override
    public List<VolunteerProfile> getAllVolunteers() {
        return volunteerProfileRepository.findAll();
    }
    
    @Override
    public VolunteerProfile updateVolunteer(Long id, VolunteerProfile volunteerProfile) {
        VolunteerProfile existing = getVolunteerById(id);
        
        if (volunteerProfile.getFullName() != null) {
            existing.setFullName(volunteerProfile.getFullName());
        }
        if (volunteerProfile.getEmail() != null) {
            existing.setEmail(volunteerProfile.getEmail());
        }
        if (volunteerProfile.getPhone() != null) {
            existing.setPhone(volunteerProfile.getPhone());
        }
        if (volunteerProfile.getAvailabilityStatus() != null) {
            existing.setAvailabilityStatus(volunteerProfile.getAvailabilityStatus());
        }
        
        existing.setUpdatedAt(LocalDateTime.now());
        return volunteerProfileRepository.save(existing);
    }
    
    @Override
    public void deleteVolunteer(Long id) {
        if (!volunteerProfileRepository.existsById(id)) {
            throw new BadRequestException("Volunteer not found");
        }
        volunteerProfileRepository.deleteById(id);
    }
    
    @Override
    public Optional<VolunteerProfile> findByVolunteerId(String volunteerId) {
        return volunteerProfileRepository.findByVolunteerId(volunteerId);
    }
}