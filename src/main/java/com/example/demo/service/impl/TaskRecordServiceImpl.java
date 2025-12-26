package com.example.demo.service.impl;

import com.example.demo.model.TaskRecord;
import com.example.demo.repository.TaskRecordRepository;
import com.example.demo.service.TaskRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public TaskRecord updateTask(Long id, TaskRecord updated) {
        TaskRecord t = repository.findById(id).orElseThrow();
        t.setTaskName(updated.getTaskName());
        return repository.save(t);
    }

    @Override
    public TaskRecord getTask(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public TaskRecord getTaskByCode(String code) {
        return repository.findByTaskCode(code).orElseThrow();
    }

    @Override
    public List<TaskRecord> getAllTasks() {
        return repository.findAll();
    }
}
