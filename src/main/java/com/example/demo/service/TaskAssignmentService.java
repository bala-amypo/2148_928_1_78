package com.example.demo.service;

import com.example.demo.exception.BadRequestException;
import com.example.demo.model.TaskAssignmentRecord;
import com.example.demo.model.TaskRecord;
import com.example.demo.model.VolunteerProfile;
import com.example.demo.model.VolunteerSkillRecord;
import com.example.demo.repository.TaskAssignmentRecordRepository;
import com.example.demo.repository.TaskRecordRepository;
import com.example.demo.repository.VolunteerProfileRepository;
import com.example.demo.repository.VolunteerSkillRecordRepository;
import com.example.demo.util.SkillLevelUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskAssignmentService {

    private final TaskAssignmentRecordRepository assignmentRepository;
    private final TaskRecordRepository taskRepository;
    private final VolunteerProfileRepository volunteerRepository;
    private final VolunteerSkillRecordRepository skillRepository;

    public TaskAssignmentService(TaskAssignmentRecordRepository assignmentRepository,
                                 TaskRecordRepository taskRepository,
                                 VolunteerProfileRepository volunteerRepository,
                                 VolunteerSkillRecordRepository skillRepository) {
        this.assignmentRepository = assignmentRepository;
        this.taskRepository = taskRepository;
        this.volunteerRepository = volunteerRepository;
        this.skillRepository = skillRepository;
    }

    public TaskAssignmentRecord assignVolunteer(Long taskId) {

        TaskRecord task = taskRepository.findById(taskId)
                .orElseThrow(() -> new BadRequestException("Task not found"));

        List<VolunteerProfile> volunteers =
                volunteerRepository.findByAvailabilityStatus("AVAILABLE");

        for (VolunteerProfile volunteer : volunteers) {

            List<VolunteerSkillRecord> skills =
                    skillRepository.findByVolunteerId(volunteer.getId());

            for (VolunteerSkillRecord skill : skills) {

                if (skill.getSkillName().equalsIgnoreCase(task.getRequiredSkill())
                        && SkillLevelUtil.levelRank(skill.getSkillLevel())
                        >= SkillLevelUtil.levelRank(task.getRequiredSkillLevel())) {

                    TaskAssignmentRecord assignment = new TaskAssignmentRecord();
                    assignment.setTaskId(taskId);
                    assignment.setVolunteerId(volunteer.getId());
                    assignment.setStatus("ASSIGNED");

                    volunteer.setAvailabilityStatus("BUSY");
                    volunteerRepository.save(volunteer);

                    task.setStatus("ASSIGNED");
                    taskRepository.save(task);

                    return assignmentRepository.save(assignment);
                }
            }
        }

        throw new BadRequestException("No suitable volunteer found");
    }

    public TaskAssignmentRecord updateStatus(Long assignmentId, String status) {

        TaskAssignmentRecord assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new BadRequestException("Assignment not found"));

        assignment.setStatus(status);
        return assignmentRepository.save(assignment);
    }
}
