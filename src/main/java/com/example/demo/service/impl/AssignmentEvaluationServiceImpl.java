package com.example.demo.service;

import com.example.demo.model.AssignmentEvaluationRecord;
import com.example.demo.repository.AssignmentEvaluationRecordRepository;
import com.example.demo.repository.TaskAssignmentRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class AssignmentEvaluationServiceImpl implements AssignmentEvaluationService {

    @Autowired
    private AssignmentEvaluationRecordRepository evaluationRepository;

    @Autowired
    private TaskAssignmentRecordRepository assignmentRepository;

    @Override
    public AssignmentEvaluationRecord evaluateAssignment(Long assignmentId, String feedback) {
        return evaluateAssignment(assignmentId, feedback, null);
    }

    @Override
    public AssignmentEvaluationRecord evaluateAssignment(Long assignmentId, String feedback, Integer rating) {
        // Check if assignment exists and is completed
        if (!assignmentRepository.existsByIdAndStatus(assignmentId, "COMPLETED")) {
            throw new RuntimeException("Assignment must be completed before evaluation");
        }

        // Check if already evaluated
        Optional<AssignmentEvaluationRecord> existingEval = 
            evaluationRepository.findByAssignmentId(assignmentId);
        
        AssignmentEvaluationRecord evaluation;
        if (existingEval.isPresent()) {
            // Update existing evaluation
            evaluation = existingEval.get();
            evaluation.setFeedback(feedback);
            if (rating != null) {
                evaluation.setRating(rating);
            }
        } else {
            // Create new evaluation
            evaluation = new AssignmentEvaluationRecord();
            evaluation.setAssignmentId(assignmentId);
            evaluation.setFeedback(feedback);
            evaluation.setRating(rating != null ? rating : 0);
        }
        
        evaluation.setEvaluatedAt(LocalDateTime.now());
        return evaluationRepository.save(evaluation);
    }

    @Override
    public Optional<AssignmentEvaluationRecord> getEvaluationByAssignmentId(Long assignmentId) {
        return evaluationRepository.findByAssignmentId(assignmentId);
    }

    @Override
    public Optional<AssignmentEvaluationRecord> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
    }

    @Override
    public boolean deleteEvaluation(Long id) {
        if (evaluationRepository.existsById(id)) {
            evaluationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}