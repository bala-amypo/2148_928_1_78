package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignments")
public class TaskAssignmentController {

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long assignmentId,
                               @RequestParam String status) {
        return "Status updated to " + status;
    }
}
