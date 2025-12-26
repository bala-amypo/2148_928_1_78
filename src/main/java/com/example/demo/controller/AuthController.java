package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestParam String username,
            @RequestParam String password) {

        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("username", username);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestParam String username,
            @RequestParam String password) {

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("username", username);

        return ResponseEntity.ok(response);
    }
}
