package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluation")
public class AssignmentEvaluationController {

    @PostMapping("/evaluate")
    public String evaluate(@RequestParam Long assignmentId,
                           @RequestParam String remarks) {
        return "Evaluation saved for assignment " + assignmentId;
    }
}
