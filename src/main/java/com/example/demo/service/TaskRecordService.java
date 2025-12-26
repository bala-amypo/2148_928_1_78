package com.example.demo.service;

import com.example.demo.model.TaskRecord;

import java.util.List;
import java.util.Optional;

public interface TaskRecordService {

    TaskRecord createTask(TaskRecord task);

    TaskRecord updateTask(Long id, TaskRecord updated);

    TaskRecord getTask(Long id);

    Optional<TaskRecord> getTaskByCode(String code);

    List<TaskRecord> getAllTasks();

    List<TaskRecord> getOpenTasks();
}
