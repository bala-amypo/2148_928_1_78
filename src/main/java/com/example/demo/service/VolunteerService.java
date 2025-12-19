package com.example.demo.service;

import com.example.demo.model.VolunteerProfile;

import java.util.List;

public interface VolunteerService {

    VolunteerProfile createVolunteer(VolunteerProfile volunteer);

    VolunteerProfile updateAvailability(Long id, String availabilityStatus);

    List<VolunteerProfile> getAllVolunteers();
}
