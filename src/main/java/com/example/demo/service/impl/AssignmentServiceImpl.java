package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImpl {

    public String assignTask(Long taskId, Long volunteerId) {
        return "Task " + taskId + " assigned to volunteer " + volunteerId;
    }
}
