package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

@Service
public class SkillServiceImpl {

    public String addSkill(String skillName) {
        return "Skill added: " + skillName;
    }
}
