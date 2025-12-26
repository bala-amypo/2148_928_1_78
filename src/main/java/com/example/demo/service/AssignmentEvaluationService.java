package com.example.demo.service;

import com.example.demo.model.AssignmentEvaluationRecord;
import java.util.Optional;

public interface AssignmentEvaluationService {
    AssignmentEvaluationRecord evaluateAssignment(Long assignmentId, String feedback);
    AssignmentEvaluationRecord evaluateAssignment(Long assignmentId, String feedback, Integer rating);
    Optional<AssignmentEvaluationRecord> getEvaluationByAssignmentId(Long assignmentId);
    Optional<AssignmentEvaluationRecord> getEvaluationById(Long id);
    boolean deleteEvaluation(Long id);
}