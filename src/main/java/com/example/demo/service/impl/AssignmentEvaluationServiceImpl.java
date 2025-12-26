package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.AssignmentEvaluationRecord;
import com.example.demo.repository.AssignmentEvaluationRecordRepository;
import com.example.demo.repository.TaskAssignmentRecordRepository;
import com.example.demo.service.AssignmentEvaluationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssignmentEvaluationServiceImpl implements AssignmentEvaluationService {

    private final AssignmentEvaluationRecordRepository evaluationRepository;
    private final TaskAssignmentRecordRepository assignmentRepository;

    public AssignmentEvaluationServiceImpl(
            AssignmentEvaluationRecordRepository evaluationRepository,
            TaskAssignmentRecordRepository assignmentRepository) {
        this.evaluationRepository = evaluationRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public AssignmentEvaluationRecord evaluateAssignment(
            Long assignmentId, String feedback) {

        return evaluateAssignment(assignmentId, feedback, null);
    }

    @Override
    public AssignmentEvaluationRecord evaluateAssignment(
            Long assignmentId, String feedback, Integer rating) {

        if (!assignmentRepository.existsByIdAndStatus(assignmentId, "COMPLETED")) {
            throw new BadRequestException("Assignment not completed");
        }

        AssignmentEvaluationRecord record = new AssignmentEvaluationRecord();
        record.setAssignmentId(assignmentId);
        record.setFeedback(feedback);
        record.setRating(rating);

        return evaluationRepository.save(record);
    }

    @Override
    public Optional<AssignmentEvaluationRecord> getEvaluationByAssignmentId(
            Long assignmentId) {

        return evaluationRepository.findByAssignmentId(assignmentId);
    }

    @Override
    public Optional<AssignmentEvaluationRecord> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
    }

    @Override
    public boolean deleteEvaluation(Long id) {
        if (!evaluationRepository.existsById(id)) {
            return false;
        }
        evaluationRepository.deleteById(id);
        return true;
    }
}
