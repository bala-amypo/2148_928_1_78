package com.example.demo.service;

import com.example.demo.model.TaskAssignmentRecord;
import java.util.List;

public interface TaskAssignmentService {
    TaskAssignmentRecord assignTask(Long taskId);
    List<TaskAssignmentRecord> getAllAssignments();
    List<TaskAssignmentRecord> getAssignmentsByTask(Long taskId);
    List<TaskAssignmentRecord> getAssignmentsByVolunteer(Long volunteerId);
}