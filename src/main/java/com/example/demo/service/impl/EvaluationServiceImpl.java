package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

@Service
public class EvaluationServiceImpl {

    public String evaluateAssignment(Long assignmentId, String remarks) {
        return "Assignment " + assignmentId + " evaluated. Remarks: " + remarks;
    }
}
