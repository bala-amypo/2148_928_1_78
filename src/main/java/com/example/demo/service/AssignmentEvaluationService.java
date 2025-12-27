package com.example.demo.service;

import com.example.demo.model.AssignmentEvaluationRecord;
import java.util.List;

public interface AssignmentEvaluationService {
    AssignmentEvaluationRecord evaluateAssignment(AssignmentEvaluationRecord evaluationRecord);  // Changed from submitEvaluation
    List<AssignmentEvaluationRecord> getEvaluationsByAssignment(Long assignmentId);  // Changed from getEvaluations
}