package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email) {
        return userService.register(name, email);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email) {
        return userService.login(email);
    }
}
