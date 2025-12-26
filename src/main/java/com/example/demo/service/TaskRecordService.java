package com.example.demo.service;

import com.example.demo.model.TaskRecord;
import java.util.List;
import java.util.Optional;

public interface TaskRecordService {
    TaskRecord saveTask(TaskRecord task);
    TaskRecord updateTask(Long id, TaskRecord task);
    List<TaskRecord> getAllTasks();
    Optional<TaskRecord> getTaskById(Long id);
    TaskRecord getTaskByCode(String taskCode);
    List<TaskRecord> getOpenTasks();
    boolean deleteTask(Long id);
    List<TaskRecord> getTasksBySkill(String skill);
    List<TaskRecord> getTasksBySkillAndLevel(String skill, String level);
}