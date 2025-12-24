package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class VolunteerSkillRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private VolunteerProfile volunteer;

    private String skillName;
    private int skillLevel;

    public VolunteerSkillRecord() {}

    public VolunteerSkillRecord(VolunteerProfile volunteer,
                                String skillName, int skillLevel) {
        this.volunteer = volunteer;
        this.skillName = skillName;
        this.skillLevel = skillLevel;
    }

    public Long getId() { return id; }

    public VolunteerProfile getVolunteer() { return volunteer; }
    public void setVolunteer(VolunteerProfile volunteer) {
        this.volunteer = volunteer;
    }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public int getSkillLevel() { return skillLevel; }
    public void setSkillLevel(int skillLevel) { this.skillLevel = skillLevel; }
}
