package com.example.demo.service;

import com.example.demo.model.TaskAssignmentRecord;
import com.example.demo.repository.TaskAssignmentRecordRepository;
import com.example.demo.repository.TaskRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    @Autowired
    private TaskAssignmentRecordRepository taskAssignmentRepository;

    @Autowired
    private TaskRecordRepository taskRecordRepository;

    @Override
    public TaskAssignmentRecord assignTask(Long taskId, Long volunteerId) {
        // Check if task is already assigned
        if (taskAssignmentRepository.existsByTaskIdAndStatus(taskId, "ASSIGNED")) {
            throw new RuntimeException("Task is already assigned");
        }

        // Create new assignment
        TaskAssignmentRecord assignment = new TaskAssignmentRecord();
        assignment.setTaskId(taskId);
        assignment.setVolunteerId(volunteerId);
        assignment.setStatus("ASSIGNED");
        assignment.setAssignedAt(LocalDateTime.now());

        // Update task status
        taskRecordRepository.findById(taskId).ifPresent(task -> {
            task.setStatus("ASSIGNED");
            task.setUpdatedAt(LocalDateTime.now());
            taskRecordRepository.save(task);
        });

        return taskAssignmentRepository.save(assignment);
    }

    @Override
    public TaskAssignmentRecord completeAssignment(Long assignmentId) {
        Optional<TaskAssignmentRecord> optionalAssignment = taskAssignmentRepository.findById(assignmentId);
        if (optionalAssignment.isPresent()) {
            TaskAssignmentRecord assignment = optionalAssignment.get();
            assignment.setStatus("COMPLETED");
            assignment.setCompletedAt(LocalDateTime.now());

            // Update task status
            taskRecordRepository.findById(assignment.getTaskId()).ifPresent(task -> {
                task.setStatus("COMPLETED");
                task.setUpdatedAt(LocalDateTime.now());
                taskRecordRepository.save(task);
            });

            return taskAssignmentRepository.save(assignment);
        }
        return null;
    }

    @Override
    public List<TaskAssignmentRecord> getAssignmentsByTask(Long taskId) {
        return taskAssignmentRepository.findByTaskId(taskId);
    }

    @Override
    public List<TaskAssignmentRecord> getAssignmentsByVolunteer(Long volunteerId) {
        return taskAssignmentRepository.findByVolunteerId(volunteerId);
    }

    @Override
    public List<TaskAssignmentRecord> getAllAssignments() {
        return taskAssignmentRepository.findAll();
    }

    @Override
    public Optional<TaskAssignmentRecord> getAssignmentById(Long id) {
        return taskAssignmentRepository.findById(id);
    }

    @Override
    public List<TaskAssignmentRecord> getAssignmentsByStatus(String status) {
        return taskAssignmentRepository.findByStatus(status);
    }
}