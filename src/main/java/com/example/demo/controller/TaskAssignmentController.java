package com.example.demo.controller;

import com.example.demo.model.TaskAssignmentRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@Tag(name = "Task Assignment", description = "Operations on task assignments")
public class TaskAssignmentController {

    @PostMapping("/assign/{taskId}")
    @Operation(summary = "Auto-assign task to volunteer")
    public ResponseEntity<TaskAssignmentRecord> autoAssign(@PathVariable Long taskId) {
        // TODO: implement auto-assign
        return ResponseEntity.ok(new TaskAssignmentRecord());
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update assignment status")
    public ResponseEntity<TaskAssignmentRecord> updateStatus(@PathVariable Long id, @RequestParam String status) {
        // TODO: implement status update
        return ResponseEntity.ok(new TaskAssignmentRecord());
    }

    @GetMapping("/volunteer/{volunteerId}")
    @Operation(summary = "Get assignments by volunteer")
    public ResponseEntity<List<TaskAssignmentRecord>> getByVolunteer(@PathVariable Long volunteerId) {
        // TODO: fetch assignments by volunteer
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/task/{taskId}")
    @Operation(summary = "Get assignments by task")
    public ResponseEntity<List<TaskAssignmentRecord>> getByTask(@PathVariable Long taskId) {
        // TODO: fetch assignments by task
        return ResponseEntity.ok(List.of());
    }

    @GetMapping
    @Operation(summary = "List all assignments")
    public ResponseEntity<List<TaskAssignmentRecord>> listAll() {
        // TODO: list all assignments
        return ResponseEntity.ok(List.of());
    }
}
