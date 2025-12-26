package com.example.demo.service;

import com.example.demo.model.TaskRecord;
import java.util.List;
import java.util.Optional;

public interface TaskRecordService {
    TaskRecord saveTask(TaskRecord task);
    TaskRecord updateTask(Long id, TaskRecord task);
    List<TaskRecord> getOpenTasks();
    TaskRecord getTaskByCode(String taskCode);
    Optional<TaskRecord> getTaskById(Long id);
}