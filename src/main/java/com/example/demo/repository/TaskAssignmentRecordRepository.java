package com.example.demo.repository;

import com.example.demo.model.TaskAssignmentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAssignmentRecordRepository
        extends JpaRepository<TaskAssignmentRecord, Long> {

    boolean existsByTaskIdAndStatus(Long taskId, String status);

    List<TaskAssignmentRecord> findByVolunteerId(Long volunteerId);

    List<TaskAssignmentRecord> findByTaskId(Long taskId);
}
