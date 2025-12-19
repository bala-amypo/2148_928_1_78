package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

@Service
public class VolunteerServiceImpl {

    public String registerVolunteer(Long userId) {
        return "Volunteer registered for user id: " + userId;
    }
}
