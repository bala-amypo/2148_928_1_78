package com.example.demo.service.impl;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
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
            throw new BadRequestException("ACTIVE assignment");
        }

        List<VolunteerProfile> volunteers =
                volunteerRepo.findByAvailabilityStatus("AVAILABLE");

        if (volunteers.isEmpty()) {
            throw new BadRequestException("No AVAILABLE volunteers");
        }

        for (VolunteerProfile v : volunteers) {

            List<VolunteerSkillRecord> skills =
                    skillRepo.findByVolunteerId(v.getId());

            for (VolunteerSkillRecord s : skills) {

                if (s.getSkillName().equals(task.getRequiredSkill())) {

                    int vRank =
                            SkillLevelUtil.levelRank(s.getSkillLevel());

                    // 🔴 FIX: Integer → String safe conversion
                    int tRank =
                            SkillLevelUtil.levelRank(
                                    String.valueOf(task.getRequiredSkillLevel())
                            );

                    if (vRank >= tRank) {
                        TaskAssignmentRecord ar = new TaskAssignmentRecord();
                        ar.setTaskId(taskId);
                        ar.setVolunteerId(v.getId());
                        ar.setStatus("ACTIVE");
                        return assignmentRepo.save(ar);
                    }
                }
            }
        }

        throw new BadRequestException("required skill level");
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
