package com.example.demo.controller;

import com.example.demo.model.TaskAssignmentRecord;
import com.example.demo.service.TaskAssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
public class TaskAssignmentController {

    private final TaskAssignmentService service;

    public TaskAssignmentController(TaskAssignmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TaskAssignmentRecord> assign(
            @RequestParam Long taskId,
            @RequestParam Long volunteerId) {
        return ResponseEntity.ok(service.assignTask(taskId, volunteerId));
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<TaskAssignmentRecord>> byVolunteer(
            @PathVariable Long volunteerId) {
        return ResponseEntity.ok(service.getAssignmentsByVolunteer(volunteerId));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskAssignmentRecord>> byTask(
            @PathVariable Long taskId) {
        return ResponseEntity.ok(service.getAssignmentsByTask(taskId));
    }
}
