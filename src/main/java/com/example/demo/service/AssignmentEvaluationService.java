package com.example.demo.service;

import com.example.demo.model.AssignmentEvaluationRecord;
import com.example.demo.repository.AssignmentEvaluationRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentEvaluationService {

    private final AssignmentEvaluationRecordRepository repository;

    public AssignmentEvaluationService(AssignmentEvaluationRecordRepository repository) {
        this.repository = repository;
    }

    public AssignmentEvaluationRecord submitEvaluation(AssignmentEvaluationRecord record) {
        return repository.save(record);
    }

    public List<AssignmentEvaluationRecord> getEvaluations(Long assignmentId) {
        return repository.findByAssignmentId(assignmentId);
    }
}
