package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "task_records")
public class TaskRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;

    private String status;

    // Constructors
    public TaskRecord() {}

    public TaskRecord(String taskName, String status) {
        this.taskName = taskName;
        this.status = status;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
