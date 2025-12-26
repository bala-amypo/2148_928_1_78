package com.example.demo.service;

import com.example.demo.model.TaskAssignmentRecord;

import java.util.List;

public interface TaskAssignmentService {

    TaskAssignmentRecord assignTask(Long taskId);

    TaskAssignmentRecord updateAssignmentStatus(Long id, String status);

    List<TaskAssignmentRecord> getAssignmentsByVolunteer(Long volunteerId);

    List<TaskAssignmentRecord> getAssignmentsByTask(Long taskId);

    List<TaskAssignmentRecord> getAllAssignments();
}
