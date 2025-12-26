package com.example.demo.controller;

import com.example.demo.model.AssignmentEvaluationRecord;
import com.example.demo.service.AssignmentEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluations")
public class AssignmentEvaluationController {

    private final AssignmentEvaluationService service;

    public AssignmentEvaluationController(AssignmentEvaluationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AssignmentEvaluationRecord> submit(
            @RequestBody AssignmentEvaluationRecord record) {
        return ResponseEntity.ok(service.submitEvaluation(record));
    }

    @GetMapping("/{assignmentId}")
    public ResponseEntity<List<AssignmentEvaluationRecord>> get(
            @PathVariable Long assignmentId) {
        return ResponseEntity.ok(service.getEvaluations(assignmentId));
    }
}
