package com.example.demo.service;

import com.example.demo.model.TaskAssignmentRecord;
import java.util.List;
import java.util.Optional;

public interface TaskAssignmentService {
    TaskAssignmentRecord assignTask(Long taskId, Long volunteerId);
    TaskAssignmentRecord completeAssignment(Long assignmentId);
    List<TaskAssignmentRecord> getAssignmentsByTask(Long taskId);
    List<TaskAssignmentRecord> getAssignmentsByVolunteer(Long volunteerId);
    List<TaskAssignmentRecord> getAllAssignments();
    Optional<TaskAssignmentRecord> getAssignmentById(Long id);
    List<TaskAssignmentRecord> getAssignmentsByStatus(String status);
}