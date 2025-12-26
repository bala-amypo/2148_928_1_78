package com.example.demo.service;

import com.example.demo.model.TaskRecord;
import com.example.demo.repository.TaskRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskRecordServiceImpl implements TaskRecordService {

    @Autowired
    private TaskRecordRepository taskRecordRepository;

    @Override
    public TaskRecord saveTask(TaskRecord task) {
        if (task.getCreatedAt() == null) {
            task.setCreatedAt(LocalDateTime.now());
        }
        task.setUpdatedAt(LocalDateTime.now());
        return taskRecordRepository.save(task);
    }

    @Override
    public TaskRecord updateTask(Long id, TaskRecord taskDetails) {
        Optional<TaskRecord> optionalTask = taskRecordRepository.findById(id);
        if (optionalTask.isPresent()) {
            TaskRecord task = optionalTask.get();
            
            // Update fields if provided
            if (taskDetails.getTitle() != null) {
                task.setTitle(taskDetails.getTitle());
            }
            if (taskDetails.getDescription() != null) {
                task.setDescription(taskDetails.getDescription());
            }
            if (taskDetails.getStatus() != null) {
                task.setStatus(taskDetails.getStatus());
            }
            if (taskDetails.getRequiredSkill() != null) {
                task.setRequiredSkill(taskDetails.getRequiredSkill());
            }
            if (taskDetails.getRequiredSkillLevel() != null) {
                task.setRequiredSkillLevel(taskDetails.getRequiredSkillLevel());
            }
            if (taskDetails.getPriority() != null) {
                task.setPriority(taskDetails.getPriority());
            }
            
            task.setUpdatedAt(LocalDateTime.now());
            return taskRecordRepository.save(task);
        }
        return null;
    }

    @Override
    public List<TaskRecord> getAllTasks() {
        return taskRecordRepository.findAll();
    }

    @Override
    public Optional<TaskRecord> getTaskById(Long id) {
        return taskRecordRepository.findById(id);
    }

    @Override
    public TaskRecord getTaskByCode(String taskCode) {
        return taskRecordRepository.findByTaskCode(taskCode).orElse(null);
    }

    @Override
    public List<TaskRecord> getOpenTasks() {
        return taskRecordRepository.findByStatus("OPEN");
    }

    @Override
    public boolean deleteTask(Long id) {
        if (taskRecordRepository.existsById(id)) {
            taskRecordRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<TaskRecord> getTasksBySkill(String skill) {
        return taskRecordRepository.findByRequiredSkill(skill);
    }

    @Override
    public List<TaskRecord> getTasksBySkillAndLevel(String skill, String level) {
        return taskRecordRepository.findByRequiredSkillAndRequiredSkillLevel(skill, level);
    }
}