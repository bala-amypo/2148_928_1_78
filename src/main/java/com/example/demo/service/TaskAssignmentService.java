package com.example.demo.service;

import com.example.demo.model.TaskAssignmentRecord;
import com.example.demo.model.TaskRecord;
import com.example.demo.repository.TaskAssignmentRecordRepository;
import com.example.demo.repository.TaskRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssignmentService {

    private final TaskAssignmentRecordRepository assignmentRepo;
    private final TaskRecordRepository taskRepo;

    public TaskAssignmentService(TaskAssignmentRecordRepository assignmentRepo,
                                 TaskRecordRepository taskRepo) {
        this.assignmentRepo = assignmentRepo;
        this.taskRepo = taskRepo;
    }

    public TaskAssignmentRecord assignTask(Long taskId, Long volunteerId) {
        TaskRecord task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus("ASSIGNED");
        taskRepo.save(task);

        TaskAssignmentRecord record = new TaskAssignmentRecord();
        record.setTaskId(taskId);
        record.setVolunteerId(volunteerId);

        return assignmentRepo.save(record);
    }

    public List<TaskAssignmentRecord> getAssignmentsByVolunteer(Long volunteerId) {
        return assignmentRepo.findByVolunteerId(volunteerId);
    }

    public List<TaskAssignmentRecord> getAssignmentsByTask(Long taskId) {
        return assignmentRepo.findByTaskId(taskId);
    }
}
