package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public String register(String name, String email) {
        return "User registered: " + name;
    }

    @Override
    public String login(String email) {
        return "User logged in: " + email;
    }
}
