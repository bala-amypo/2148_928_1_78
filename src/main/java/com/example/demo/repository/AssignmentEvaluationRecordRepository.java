package com.example.demo.repository;

import com.example.demo.model.AssignmentEvaluationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentEvaluationRecordRepository
        extends JpaRepository<AssignmentEvaluationRecord, Long> {
}
