package com.example.demo.service;

import com.example.demo.model.VolunteerProfile;

public interface VolunteerProfileService {

    VolunteerProfile createVolunteer(VolunteerProfile volunteer);

    VolunteerProfile updateAvailability(Long id, String availabilityStatus);

    VolunteerProfile getVolunteerById(Long id);
}
