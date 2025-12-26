package com.example.demo.service.impl;

import com.example.demo.model.AssignmentEvaluationRecord;
import com.example.demo.repository.AssignmentEvaluationRecordRepository;
import com.example.demo.service.AssignmentEvaluationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentEvaluationServiceImpl implements AssignmentEvaluationService {

    private final AssignmentEvaluationRecordRepository repository;

    public AssignmentEvaluationServiceImpl(AssignmentEvaluationRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public AssignmentEvaluationRecord evaluateAssignment(AssignmentEvaluationRecord evaluation) {
        return repository.save(evaluation);
    }

    @Override
    public Optional<AssignmentEvaluationRecord> getEvaluationById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<AssignmentEvaluationRecord> getEvaluationsByAssignmentId(Long assignmentId) {
        return repository.findByAssignmentId(assignmentId);
    }

    @Override
    public AssignmentEvaluationRecord updateEvaluation(Long id, AssignmentEvaluationRecord evaluation) {
        evaluation.setId(id);
        return repository.save(evaluation);
    }

    @Override
    public void deleteEvaluation(Long id) {
        repository.deleteById(id);
    }
}
