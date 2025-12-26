package com.example.demo.service;

import com.example.demo.model.TaskRecord;
import java.util.List;

public interface TaskRecordService {
    TaskRecord createTask(TaskRecord task);
    TaskRecord updateTask(Long id, TaskRecord updated);
    TaskRecord getTask(Long id);
    TaskRecord getTaskByCode(String code);
    List<TaskRecord> getAllTasks();
}
