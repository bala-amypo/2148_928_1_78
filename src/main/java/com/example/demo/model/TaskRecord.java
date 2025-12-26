package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TaskRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskCode;
    private String taskName;
    private String requiredSkill;
    private String requiredSkillLevel;
    private String priority;
    private String status;

    @PrePersist
    public void onCreate() {
        if (status == null) status = "OPEN";
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTaskCode() { return taskCode; }
    public void setTaskCode(String taskCode) { this.taskCode = taskCode; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getRequiredSkill() { return requiredSkill; }
    public void setRequiredSkill(String requiredSkill) { this.requiredSkill = requiredSkill; }

    public String getRequiredSkillLevel() { return requiredSkillLevel; }
    public void setRequiredSkillLevel(String requiredSkillLevel) { this.requiredSkillLevel = requiredSkillLevel; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
