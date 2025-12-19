package com.example.demo.service;

import com.example.demo.model.TaskRecord;
import com.example.demo.repository.TaskRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskRecordService {

    @Autowired
    private TaskRecordRepository taskRecordRepository;

    public TaskRecord createTask(TaskRecord task) {
        return taskRecordRepository.save(task);
    }

    public List<TaskRecord> getAllTasks() {
        return taskRecordRepository.findAll();
    }
}
