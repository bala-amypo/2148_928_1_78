package com.example.demo.controller;

import com.example.demo.model.TaskAssignmentRecord;
import com.example.demo.service.TaskAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@Tag(name = "Task Assignments", description = "APIs for managing task assignments")
public class TaskAssignmentController {

    @Autowired
    private TaskAssignmentService service;

    @PostMapping("/task/{taskId}")
    @Operation(summary = "Assign a task to a volunteer")
    public ResponseEntity<TaskAssignmentRecord> assignTask(@PathVariable Long taskId) {
        TaskAssignmentRecord assignment = service.assignTask(taskId);
        return ResponseEntity.ok(assignment);
    }

    @GetMapping
    @Operation(summary = "Get all assignments")
    public ResponseEntity<List<TaskAssignmentRecord>> getAllAssignments() {
        List<TaskAssignmentRecord> assignments = service.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get assignments by task ID")
    public ResponseEntity<List<TaskAssignmentRecord>> getAssignmentsByTask(@PathVariable Long taskId) {
        List<TaskAssignmentRecord> assignments = service.getAssignmentsByTask(taskId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/volunteer/{volunteerId}")
    @Operation(summary = "Get assignments by volunteer ID")
    public ResponseEntity<List<TaskAssignmentRecord>> getAssignmentsByVolunteer(@PathVariable Long volunteerId) {
        List<TaskAssignmentRecord> assignments = service.getAssignmentsByVolunteer(volunteerId);
        return ResponseEntity.ok(assignments);
    }
}