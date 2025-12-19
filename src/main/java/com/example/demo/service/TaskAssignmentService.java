package com.example.demo.service;

public interface TaskAssignmentService {

    String assignTask(Long taskId, Long volunteerId);

    String updateStatus(Long assignmentId, String status);
}
