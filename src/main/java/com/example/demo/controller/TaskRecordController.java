package com.example.demo.controller;

import com.example.demo.model.TaskRecord;
import com.example.demo.service.TaskRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskRecordController {

    private final TaskRecordService taskRecordService;

    public TaskRecordController(TaskRecordService taskRecordService) {
        this.taskRecordService = taskRecordService;
    }

    @PostMapping
    public TaskRecord createTask(@RequestBody TaskRecord task) {
        return taskRecordService.createTask(task);
    }

    @GetMapping
    public List<TaskRecord> getAllTasks() {
        return taskRecordService.getAllTasks();
    }
}
