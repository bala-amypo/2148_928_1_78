package com.example.demo.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final Map<String, Map<String, Object>> users = new HashMap<>();
    private long nextUserId = 1;
    
    public Map<String, Object> registerUser(String fullName, String email, 
                                           String encryptedPassword, String role) {
        Map<String, Object> user = new HashMap<>();
        user.put("userId", nextUserId++);
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("password", encryptedPassword);
        user.put("role", role);
        user.put("enabled", true);
        
        users.put(email, user);
        return user;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Map<String, Object> user = users.get(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        
        return User.builder()
                .username(email)
                .password((String) user.get("password"))
                .roles(((String) user.get("role")).replace("ROLE_", ""))
                .disabled(!(Boolean) user.get("enabled"))
                .build();
    }
}