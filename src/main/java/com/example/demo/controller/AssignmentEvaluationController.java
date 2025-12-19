package com.example.demo.controller;

import com.example.demo.model.AssignmentEvaluationRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@Tag(name = "Assignment Evaluation", description = "Operations for submitting evaluations")
public class AssignmentEvaluationController {

    @PostMapping
    @Operation(summary = "Submit evaluation for an assignment")
    public ResponseEntity<AssignmentEvaluationRecord> submitEvaluation(@RequestBody AssignmentEvaluationRecord evaluation) {
        // TODO: implement submit evaluation
        return ResponseEntity.ok(evaluation);
    }

    @GetMapping("/assignment/{assignmentId}")
    @Operation(summary = "Get evaluation by assignment")
    public ResponseEntity<AssignmentEvaluationRecord> getByAssignment(@PathVariable Long assignmentId) {
        // TODO: implement get by assignment
        return ResponseEntity.ok(new AssignmentEvaluationRecord());
    }

    @GetMapping
    @Operation(summary = "List all evaluations")
    public ResponseEntity<List<AssignmentEvaluationRecord>> listAll() {
        // TODO: implement list all
        return ResponseEntity.ok(List.of());
    }
}
