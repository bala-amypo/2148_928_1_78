package com.example.demo.controller;

import com.example.demo.model.TaskRecord;
import com.example.demo.service.TaskRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskRecordController {

    private final TaskRecordService service;

    public TaskRecordController(TaskRecordService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TaskRecord> create(@RequestBody TaskRecord task) {
        return ResponseEntity.ok(service.createTask(task));
    }

    @GetMapping("/{taskCode}")
    public ResponseEntity<TaskRecord> get(@PathVariable String taskCode) {
        return service.getByTaskCode(taskCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
