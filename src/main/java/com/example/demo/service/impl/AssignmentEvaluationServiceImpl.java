package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.AssignmentEvaluationRecord;
import com.example.demo.repository.AssignmentEvaluationRecordRepository;
import com.example.demo.repository.TaskAssignmentRecordRepository;
import com.example.demo.service.AssignmentEvaluationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentEvaluationServiceImpl
        implements AssignmentEvaluationService {

    private final AssignmentEvaluationRecordRepository evaluationRepo;
    private final TaskAssignmentRecordRepository assignmentRepo;

    public AssignmentEvaluationServiceImpl(
            AssignmentEvaluationRecordRepository evaluationRepo,
            TaskAssignmentRecordRepository assignmentRepo) {
        this.evaluationRepo = evaluationRepo;
        this.assignmentRepo = assignmentRepo;
    }

    @Override
    public AssignmentEvaluationRecord evaluateAssignment(
            AssignmentEvaluationRecord evaluation) {

        if (!assignmentRepo.existsByIdAndStatus(
                evaluation.getAssignmentId(), "COMPLETED")) {
            throw new BadRequestException("Assignment not completed");
        }

        return evaluationRepo.save(evaluation);
    }

    @Override
    public Optional<AssignmentEvaluationRecord> getEvaluationById(Long id) {
        return evaluationRepo.findById(id);
    }

    @Override
    public List<AssignmentEvaluationRecord> getEvaluationsByAssignment(
            Long assignmentId) {
        return evaluationRepo.findByAssignmentId(assignmentId);
    }

    @Override
    public List<AssignmentEvaluationRecord> getAllEvaluations() {
        return evaluationRepo.findAll();
    }

    @Override
    public boolean deleteEvaluation(Long id) {
        if (!evaluationRepo.existsById(id)) {
            return false;
        }
        evaluationRepo.deleteById(id);
        return true;
    }
}
