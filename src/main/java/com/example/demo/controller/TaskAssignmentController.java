package com.example.demo.controller;

import com.example.demo.dto.AssignmentStatusUpdateRequest;
import com.example.demo.model.TaskAssignmentRecord;
import com.example.demo.service.TaskAssignmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assignments")
public class TaskAssignmentController {

    private final TaskAssignmentService taskAssignmentService;

    public TaskAssignmentController(TaskAssignmentService taskAssignmentService) {
        this.taskAssignmentService = taskAssignmentService;
    }

    @PostMapping("/{taskId}")
    public TaskAssignmentRecord assignTask(@PathVariable Long taskId) {
        return taskAssignmentService.assignVolunteer(taskId);
    }

    @PutMapping("/{assignmentId}/status")
    public TaskAssignmentRecord updateStatus(
            @PathVariable Long assignmentId,
            @RequestBody AssignmentStatusUpdateRequest request) {
        return taskAssignmentService.updateStatus(
                assignmentId,
                request.getStatus()
        );
    }
}
