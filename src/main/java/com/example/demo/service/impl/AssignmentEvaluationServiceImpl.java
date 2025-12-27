package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.AssignmentEvaluationService;
import com.example.demo.exception.BadRequestException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentEvaluationServiceImpl implements AssignmentEvaluationService {
    
    private final AssignmentEvaluationRecordRepository assignmentEvaluationRecordRepository;
    private final TaskAssignmentRecordRepository taskAssignmentRecordRepository;
    
    public AssignmentEvaluationServiceImpl(
            AssignmentEvaluationRecordRepository assignmentEvaluationRecordRepository,
            TaskAssignmentRecordRepository taskAssignmentRecordRepository) {
        this.assignmentEvaluationRecordRepository = assignmentEvaluationRecordRepository;
        this.taskAssignmentRecordRepository = taskAssignmentRecordRepository;
    }
    
    @Override
    public AssignmentEvaluationRecord evaluateAssignment(AssignmentEvaluationRecord evaluationRecord) {
        TaskAssignmentRecord assignment = taskAssignmentRecordRepository
                .findById(evaluationRecord.getAssignmentId())
                .orElseThrow(() -> new BadRequestException("Assignment not found"));
        
        if (!"COMPLETED".equals(assignment.getStatus())) {
            throw new BadRequestException("Assignment must be COMPLETED to evaluate");
        }
        
        evaluationRecord.setEvaluatedAt(LocalDateTime.now());
        return assignmentEvaluationRecordRepository.save(evaluationRecord);
    }
    
    @Override
    public List<AssignmentEvaluationRecord> getEvaluationsByAssignment(Long assignmentId) {
        return assignmentEvaluationRecordRepository.findByAssignmentId(assignmentId);
    }
}