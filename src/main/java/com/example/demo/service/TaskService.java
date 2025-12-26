package com.example.demo.service;

import com.example.demo.model.TaskRecord;
import com.example.demo.repository.TaskRecordRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskRecordService {

    private final TaskRecordRepository repository;

    public TaskRecordService(TaskRecordRepository repository) {
        this.repository = repository;
    }

    public TaskRecord createTask(TaskRecord task) {
        return repository.save(task);
    }

    public Optional<TaskRecord> getByTaskCode(String taskCode) {
        return repository.findByTaskCode(taskCode);
    }

    public TaskRecord updateStatus(Long taskId, String status) {
        TaskRecord task = repository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setStatus(status);
        return repository.save(task);
    }
}
