package com.example.demo.service;

import com.example.demo.model.TaskRecord;
import java.util.List;
import java.util.Optional;

public interface TaskRecordService {
    TaskRecord createTask(TaskRecord taskRecord);
    TaskRecord getTaskById(Long id);
    List<TaskRecord> getAllTasks();
    TaskRecord updateTask(Long id, TaskRecord taskRecord);
    void deleteTask(Long id);
    List<TaskRecord> getOpenTasks();
    Optional<TaskRecord> getTaskByCode(String taskCode);
}