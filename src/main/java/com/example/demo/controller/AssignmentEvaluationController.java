package com.example.demo.controller;

import com.example.demo.model.AssignmentEvaluationRecord;
import com.example.demo.service.AssignmentEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@Tag(name = "Assignment Evaluation", description = "APIs for evaluating assignments")
public class AssignmentEvaluationController {

    @Autowired
    private AssignmentEvaluationService service;

    @PostMapping
    @Operation(summary = "Evaluate an assignment")
    public ResponseEntity<AssignmentEvaluationRecord> evaluateAssignment(
            @RequestBody AssignmentEvaluationRecord evaluation) {
        AssignmentEvaluationRecord result = service.evaluateAssignment(evaluation);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/assignment/{assignmentId}")
    @Operation(summary = "Get evaluations by assignment ID")
    public ResponseEntity<List<AssignmentEvaluationRecord>> getEvaluationsByAssignment(
            @PathVariable Long assignmentId) {
        List<AssignmentEvaluationRecord> evaluations = service.getEvaluationsByAssignment(assignmentId);
        return ResponseEntity.ok(evaluations);
    }
}