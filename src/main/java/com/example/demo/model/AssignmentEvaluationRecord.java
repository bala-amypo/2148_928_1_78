package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class AssignmentEvaluationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private TaskAssignmentRecord assignment;

    private String feedback;
    private int rating;

    public AssignmentEvaluationRecord() {}

    public AssignmentEvaluationRecord(TaskAssignmentRecord assignment,
                                      String feedback, int rating) {
        this.assignment = assignment;
        this.feedback = feedback;
        this.rating = rating;
    }

    public Long getId() { return id; }

    public TaskAssignmentRecord getAssignment() { return assignment; }
    public void setAssignment(TaskAssignmentRecord assignment) {
        this.assignment = assignment;
    }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}
