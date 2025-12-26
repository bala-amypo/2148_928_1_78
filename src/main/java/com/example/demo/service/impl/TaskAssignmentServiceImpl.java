package com.example.demo.service.impl;

import com.example.demo.model.TaskAssignmentRecord;
import com.example.demo.repository.TaskAssignmentRecordRepository;
import com.example.demo.repository.VolunteerProfileRepository;
import com.example.demo.service.TaskAssignmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    private final TaskAssignmentRecordRepository assignmentRepo;
    private final VolunteerProfileRepository volunteerRepo;

    public TaskAssignmentServiceImpl(
            TaskAssignmentRecordRepository assignmentRepo,
            VolunteerProfileRepository volunteerRepo
    ) {
        this.assignmentRepo = assignmentRepo;
        this.volunteerRepo = volunteerRepo;
    }

    @Override
    public TaskAssignmentRecord assignTaskToVolunteer(Long taskId) {
        TaskAssignmentRecord record = new TaskAssignmentRecord();
        record.setTaskId(taskId);
        record.setStatus("ASSIGNED");
        return assignmentRepo.save(record);
    }

    @Override
    public Optional<TaskAssignmentRecord> getAssignmentById(Long id) {
        return assignmentRepo.findById(id);
    }

    @Override
    public List<TaskAssignmentRecord> getAllAssignments() {
        return assignmentRepo.findAll();
    }

    @Override
    public List<TaskAssignmentRecord> getAssignmentsByTaskId(Long taskId) {
        return assignmentRepo.findByTaskId(taskId);
    }

    @Override
    public TaskAssignmentRecord updateAssignmentStatus(Long id, String status) {
        TaskAssignmentRecord record = assignmentRepo.findById(id).orElseThrow();
        record.setStatus(status);
        return assignmentRepo.save(record);
    }
}
