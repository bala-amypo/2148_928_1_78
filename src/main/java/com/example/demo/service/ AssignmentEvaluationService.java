package com.example.demo.service;

import com.example.demo.dto.EvaluationRequest;
import com.example.demo.model.AssignmentEvaluationRecord;
import com.example.demo.repository.AssignmentEvaluationRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class AssignmentEvaluationService {

    private final AssignmentEvaluationRecordRepository repository;

    public AssignmentEvaluationService(AssignmentEvaluationRecordRepository repository) {
        this.repository = repository;
    }

    public AssignmentEvaluationRecord evaluate(EvaluationRequest request) {

        AssignmentEvaluationRecord record = new AssignmentEvaluationRecord();
        record.setAssignmentId(request.getAssignmentId());
        record.setFeedback(request.getFeedback());
        record.setRating(request.getRating());

        return repository.save(record);
    }
}
