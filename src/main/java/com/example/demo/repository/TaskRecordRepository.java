package com.example.demo.repository;

import com.example.demo.model.TaskRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRecordRepository extends JpaRepository<TaskRecord, Long> {
    // Add custom queries here if needed
}
