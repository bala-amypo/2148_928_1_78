package com.example.demo.service.impl;

import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl {

    public String createTask(String title) {
        return "Task created: " + title;
    }
}
