package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.TaskAssignmentService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.util.SkillLevelUtil;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskAssignmentServiceImpl implements TaskAssignmentService {
    
    private final TaskAssignmentRecordRepository taskAssignmentRecordRepository;
    private final TaskRecordRepository taskRecordRepository;
    private final VolunteerProfileRepository volunteerProfileRepository;
    private final VolunteerSkillRecordRepository volunteerSkillRecordRepository;
    
    public TaskAssignmentServiceImpl(
            TaskAssignmentRecordRepository taskAssignmentRecordRepository,
            TaskRecordRepository taskRecordRepository,
            VolunteerProfileRepository volunteerProfileRepository,
            VolunteerSkillRecordRepository volunteerSkillRecordRepository) {
        this.taskAssignmentRecordRepository = taskAssignmentRecordRepository;
        this.taskRecordRepository = taskRecordRepository;
        this.volunteerProfileRepository = volunteerProfileRepository;
        this.volunteerSkillRecordRepository = volunteerSkillRecordRepository;
    }
    
    @Override
    public TaskAssignmentRecord assignTask(Long taskId) {
        TaskRecord task = taskRecordRepository.findById(taskId)
                .orElseThrow(() -> new BadRequestException("Task not found"));
        
        if (!"OPEN".equals(task.getStatus())) {
            throw new BadRequestException("Task is not OPEN");
        }
        
        if (taskAssignmentRecordRepository.existsByTaskIdAndStatus(taskId, "ACTIVE")) {
            throw new BadRequestException("Task already has an ACTIVE assignment");
        }
        
        List<VolunteerProfile> availableVolunteers = volunteerProfileRepository
                .findByAvailabilityStatus("AVAILABLE");
        
        if (availableVolunteers.isEmpty()) {
            throw new BadRequestException("No AVAILABLE volunteers found");
        }
        
        Optional<VolunteerProfile> selectedVolunteer = availableVolunteers.stream()
                .filter(v -> hasRequiredSkill(v.getId(), task.getRequiredSkill(), 
                        task.getRequiredSkillLevel()))
                .findFirst();
        
        if (selectedVolunteer.isEmpty()) {
            throw new BadRequestException("No volunteer found with required skill level");
        }
        
        TaskAssignmentRecord assignment = new TaskAssignmentRecord();
        assignment.setTaskId(taskId);
        assignment.setVolunteerId(selectedVolunteer.get().getId());
        assignment.setStatus("ACTIVE");
        assignment.setAssignedAt(LocalDateTime.now());
        
        task.setStatus("ASSIGNED");
        task.setUpdatedAt(LocalDateTime.now());
        taskRecordRepository.save(task);
        
        return taskAssignmentRecordRepository.save(assignment);
    }
    
    private boolean hasRequiredSkill(Long volunteerId, String requiredSkill, 
                                     String requiredSkillLevel) {
        List<VolunteerSkillRecord> skills = volunteerSkillRecordRepository
                .findByVolunteerId(volunteerId);
        
        return skills.stream()
                .anyMatch(skill -> skill.getSkillName().equals(requiredSkill) &&
                        SkillLevelUtil.levelRank(skill.getSkillLevel()) >= 
                        SkillLevelUtil.levelRank(requiredSkillLevel));
    }
    
    @Override
    public List<TaskAssignmentRecord> getAllAssignments() {
        return taskAssignmentRecordRepository.findAll();
    }
    
    @Override
    public List<TaskAssignmentRecord> getAssignmentsByTask(Long taskId) {
        return taskAssignmentRecordRepository.findByTaskId(taskId);
    }
    
    @Override
    public List<TaskAssignmentRecord> getAssignmentsByVolunteer(Long volunteerId) {
        return taskAssignmentRecordRepository.findByVolunteerId(volunteerId);
    }
}