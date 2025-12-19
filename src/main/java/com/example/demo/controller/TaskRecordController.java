package com.example.demo.controller;

import com.example.demo.model.TaskRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Record", description = "Operations on tasks")
public class TaskRecordController {

    @PostMapping
    @Operation(summary = "Create a new task")
    public ResponseEntity<TaskRecord> createTask(@RequestBody TaskRecord task) {
        // TODO: implement task creation
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task")
    public ResponseEntity<TaskRecord> updateTask(@PathVariable Long id, @RequestBody TaskRecord task) {
        // TODO: implement update
        return ResponseEntity.ok(task);
    }

    @GetMapping("/open")
    @Operation(summary = "List all open tasks")
    public ResponseEntity<List<TaskRecord>> listOpenTasks() {
        // TODO: implement fetch open tasks
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    public ResponseEntity<TaskRecord> getTask(@PathVariable Long id) {
        // TODO: implement fetch by ID
        return ResponseEntity.ok(new TaskRecord());
    }

    @GetMapping
    @Operation(summary = "List all tasks")
    public ResponseEntity<List<TaskRecord>> listAllTasks() {
        // TODO: implement list all
        return ResponseEntity.ok(List.of());
    }
}
