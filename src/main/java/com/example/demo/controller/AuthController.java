package com.example.demo.controller;

import com.example.demo.security.JwtTokenProvider;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AuthController(AuthenticationManager authenticationManager,
                         JwtTokenProvider jwtTokenProvider,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        // Check if user exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        
        userRepository.save(user);
        
        // DON'T try to authenticate immediately - instead, manually create authentication
        // Create authentication object manually
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            null,
            user.getAuthorities()  // Use the user's authorities directly
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        
        return new AuthResponse(token, "User registered successfully", user.getUsername());
    }
    
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        // This should work as-is
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        
        return new AuthResponse(token, "Login successful", request.getUsername());
    }
}