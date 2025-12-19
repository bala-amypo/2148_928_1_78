package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer")
public class VolunteerProfileController {

    @PostMapping("/availability")
    public String updateAvailability(@RequestParam Long volunteerId,
                                     @RequestParam boolean available) {
        return "Availability updated";
    }
}
