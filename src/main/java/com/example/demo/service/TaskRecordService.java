package com.example.demo.service;

import com.example.demo.model.TaskRecord;
import com.example.demo.repository.TaskRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskRecordService {

    private final TaskRecordRepository repository;

    public TaskRecordService(TaskRecordRepository repository) {
        this.repository = repository;
    }

    public TaskRecord createTask(TaskRecord task) {
        task.setStatus("OPEN");
        return repository.save(task);
    }

    public List<TaskRecord> getAllTasks() {
        return repository.findAll();
    }
}
