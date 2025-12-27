package com.example.demo.service.impl;

import com.example.demo.model.TaskRecord;
import com.example.demo.repository.TaskRecordRepository;
import com.example.demo.service.TaskRecordService;
import com.example.demo.exception.BadRequestException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskRecordServiceImpl implements TaskRecordService {
    
    private final TaskRecordRepository taskRecordRepository;
    
    public TaskRecordServiceImpl(TaskRecordRepository taskRecordRepository) {
        this.taskRecordRepository = taskRecordRepository;
    }
    
    @Override
    public TaskRecord createTask(TaskRecord taskRecord) {
        taskRecord.setStatus("OPEN");
        taskRecord.setCreatedAt(LocalDateTime.now());
        taskRecord.setUpdatedAt(LocalDateTime.now());
        return taskRecordRepository.save(taskRecord);
    }
    
    @Override
    public TaskRecord getTaskById(Long id) {
        return taskRecordRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Task not found"));
    }
    
    @Override
    public List<TaskRecord> getAllTasks() {
        return taskRecordRepository.findAll();
    }
    
    @Override
    public TaskRecord updateTask(Long id, TaskRecord taskRecord) {
        TaskRecord existing = getTaskById(id);
        
        if (taskRecord.getTaskName() != null) {
            existing.setTaskName(taskRecord.getTaskName());
        }
        if (taskRecord.getRequiredSkill() != null) {
            existing.setRequiredSkill(taskRecord.getRequiredSkill());
        }
        if (taskRecord.getRequiredSkillLevel() != null) {
            existing.setRequiredSkillLevel(taskRecord.getRequiredSkillLevel());
        }
        if (taskRecord.getPriority() != null) {
            existing.setPriority(taskRecord.getPriority());
        }
        if (taskRecord.getStatus() != null) {
            existing.setStatus(taskRecord.getStatus());
        }
        
        existing.setUpdatedAt(LocalDateTime.now());
        return taskRecordRepository.save(existing);
    }
    
    @Override
    public void deleteTask(Long id) {
        if (!taskRecordRepository.existsById(id)) {
            throw new BadRequestException("Task not found");
        }
        taskRecordRepository.deleteById(id);
    }
    
    @Override
    public List<TaskRecord> getOpenTasks() {
        return taskRecordRepository.findByStatus("OPEN");
    }
    
    @Override
    public Optional<TaskRecord> getTaskByCode(String taskCode) {
        return taskRecordRepository.findByTaskCode(taskCode);
    }
}