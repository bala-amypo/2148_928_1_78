package com.example.demo.controller;

import com.example.demo.model.TaskRecord;
import com.example.demo.service.TaskRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Management", description = "APIs for managing tasks")
public class TaskRecordController {

    @Autowired
    private TaskRecordService service;

    @GetMapping("/code/{taskCode}")
    @Operation(summary = "Get task by code")
    public ResponseEntity<TaskRecord> getTaskByCode(@PathVariable String taskCode) {
        Optional<TaskRecord> task = service.getTaskByCode(taskCode);
        return task.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/open")
    @Operation(summary = "Get all open tasks")
    public ResponseEntity<List<TaskRecord>> getOpenTasks() {
        List<TaskRecord> tasks = service.getOpenTasks();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskRecord> createTask(@RequestBody TaskRecord taskRecord) {
        TaskRecord created = service.createTask(taskRecord);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<TaskRecord> getTaskById(@PathVariable Long id) {
        TaskRecord task = service.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    @Operation(summary = "Get all tasks")
    public ResponseEntity<List<TaskRecord>> getAllTasks() {
        List<TaskRecord> tasks = service.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task")
    public ResponseEntity<TaskRecord> updateTask(@PathVariable Long id, 
                                                 @RequestBody TaskRecord taskRecord) {
        TaskRecord updated = service.updateTask(id, taskRecord);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}