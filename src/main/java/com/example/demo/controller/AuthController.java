package com.example.demo.controller;

import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "User login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        // In a real app, you would get userId and role from database
        String token = jwtTokenProvider.generateToken(authentication, 1L, "ADMIN");
        
        return Map.of("token", token, "email", request.getEmail());
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public Map<String, Object> register(@RequestBody RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        Map<String, Object> user = userDetailsService.registerUser(
            request.getFullName(),
            request.getEmail(),
            encodedPassword,
            request.getRole()
        );
        
        // Generate token for the new user
        Authentication auth = new UsernamePasswordAuthenticationToken(
            request.getEmail(), 
            request.getPassword()
        );
        
        String token = jwtTokenProvider.generateToken(auth, 
            (Long) user.get("userId"), 
            (String) user.get("role"));
        
        user.put("token", token);
        return user;
    }
    
    // DTO classes
    static class LoginRequest {
        private String email;
        private String password;
        
        // getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    static class RegisterRequest {
        private String fullName;
        private String email;
        private String password;
        private String role = "VOLUNTEER_VIEWER";
        
        // getters and setters
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}