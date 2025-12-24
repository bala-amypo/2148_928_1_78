package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "task_records")
public class TaskRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;
    private String status;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskAssignmentRecord> assignments;

    public TaskRecord() {}

    public TaskRecord(String taskName, String status) {
        this.taskName = taskName;
        this.status = status;
    }

    public Long getId() { return id; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
