package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class TaskAssignmentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskRecord task;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private VolunteerProfile volunteer;

    private String status;

    public TaskAssignmentRecord() {}

    public TaskAssignmentRecord(TaskRecord task,
                                VolunteerProfile volunteer,
                                String status) {
        this.task = task;
        this.volunteer = volunteer;
        this.status = status;
    }

    public Long getId() { return id; }

    public TaskRecord getTask() { return task; }
    public void setTask(TaskRecord task) { this.task = task; }

    public VolunteerProfile getVolunteer() { return volunteer; }
    public void setVolunteer(VolunteerProfile volunteer) {
        this.volunteer = volunteer;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
