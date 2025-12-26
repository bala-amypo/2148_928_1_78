package com.example.demo.service.impl;

import com.example.demo.model.TaskRecord;
import com.example.demo.repository.TaskRecordRepository;
import com.example.demo.service.TaskRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskRecordServiceImpl implements TaskRecordService {

    private final TaskRecordRepository repository;

    public TaskRecordServiceImpl(TaskRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public TaskRecord createTask(TaskRecord task) {
        return repository.save(task);
    }

    @Override
    public Optional<TaskRecord> getTaskById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<TaskRecord> getAllTasks() {
        return repository.findAll();
    }

    @Override
    public List<TaskRecord> getTasksByStatus(String status) {
        return repository.findByStatus(status);
    }

    @Override
    public TaskRecord updateTask(Long id, TaskRecord updatedTask) {
        updatedTask.setId(id);
        return repository.save(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        repository.deleteById(id);
    }
}
