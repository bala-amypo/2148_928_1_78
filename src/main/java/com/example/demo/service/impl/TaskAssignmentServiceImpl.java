package com.example.demo.service.impl;

import com.example.demo.model.TaskAssignmentRecord;
import com.example.demo.model.TaskRecord;
import com.example.demo.model.VolunteerProfile;
import com.example.demo.repository.TaskAssignmentRecordRepository;
import com.example.demo.repository.TaskRecordRepository;
import com.example.demo.repository.VolunteerProfileRepository;
import com.example.demo.repository.VolunteerSkillRecordRepository;
import com.example.demo.service.TaskAssignmentService;
import com.example.demo.util.SkillLevelUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssignmentServiceImpl implements TaskAssignmentService {

    private final TaskAssignmentRecordRepository assignmentRepo;
    private final TaskRecordRepository taskRepo;
    private final VolunteerProfileRepository volunteerRepo;
    private final VolunteerSkillRecordRepository skillRepo;

    // ⚠️ CONSTRUCTOR ORDER MUST MATCH SPEC
    public TaskAssignmentServiceImpl(
            TaskAssignmentRecordRepository assignmentRepo,
            TaskRecordRepository taskRepo,
            VolunteerProfileRepository volunteerRepo,
            VolunteerSkillRecordRepository skillRepo) {

        this.assignmentRepo = assignmentRepo;
        this.taskRepo = taskRepo;
        this.volunteerRepo = volunteerRepo;
        this.skillRepo = skillRepo;
    }

    @Override
    public TaskAssignmentRecord assignTask(Long taskId) {
        TaskRecord task = taskRepo.findById(taskId).orElseThrow();

        if (assignmentRepo.existsByTaskIdAndStatus(taskId, "ACTIVE")) {
            throw new RuntimeException("ACTIVE assignment");
        }

        List<VolunteerProfile> volunteers =
                volunteerRepo.findByAvailabilityStatus("AVAILABLE");

        if (volunteers.isEmpty()) {
            throw new RuntimeException("No AVAILABLE volunteers");
        }

        for (VolunteerProfile v : volunteers) {
            var skills = skillRepo.findByVolunteerIdAndSkillName(
                    v.getId(), task.getRequiredSkill());

            if (!skills.isEmpty()) {
                int vRank = SkillLevelUtil.levelRank(skills.get(0).getSkillLevel());
                int tRank = SkillLevelUtil.levelRank(task.getRequiredSkillLevel());

                if (vRank >= tRank) {
                    TaskAssignmentRecord ar = new TaskAssignmentRecord();
                    ar.setTaskId(taskId);
                    ar.setVolunteerId(v.getId());
                    ar.setStatus("ACTIVE");
                    return assignmentRepo.save(ar);
                }
            }
        }

        throw new RuntimeException("required skill level");
    }

    @Override
    public TaskAssignmentRecord updateAssignmentStatus(Long id, String status) {
        TaskAssignmentRecord ar = assignmentRepo.findById(id).orElseThrow();
        ar.setStatus(status);
        return assignmentRepo.save(ar);
    }

    @Override
    public List<TaskAssignmentRecord> getAssignmentsByVolunteer(Long volunteerId) {
        return assignmentRepo.findByVolunteerId(volunteerId);
    }

    @Override
    public List<TaskAssignmentRecord> getAssignmentsByTask(Long taskId) {
        return assignmentRepo.findByTaskId(taskId);
    }

    @Override
    public List<TaskAssignmentRecord> getAllAssignments() {
        return assignmentRepo.findAll();
    }
}
