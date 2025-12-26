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
        if (task.getStatus() == null) {
            task.setStatus("OPEN"); // required by tests
        }
        return repository.save(task);
    }

    @Override
    public TaskRecord updateTask(Long id, TaskRecord updated) {
        TaskRecord existing = repository.findById(id).orElseThrow();

        existing.setTaskName(updated.getTaskName());
        existing.setRequiredSkill(updated.getRequiredSkill());
        existing.setRequiredSkillLevel(updated.getRequiredSkillLevel());
        existing.setPriority(updated.getPriority());
        existing.setStatus(updated.getStatus());

        return repository.save(existing);
    }

    @Override
    public TaskRecord getTask(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Optional<TaskRecord> getTaskByCode(String code) {
        return repository.findByTaskCode(code);
    }

    @Override
    public List<TaskRecord> getAllTasks() {
        return repository.findAll();
    }

    @Override
    public List<TaskRecord> getOpenTasks() {
        return repository.findByStatus("OPEN");
    }
}
