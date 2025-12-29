package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private SecretKey secretKey;
    private long jwtExpirationInMs;
    
    // Default constructor for Spring
    public JwtTokenProvider() {
        // Will be initialized by @PostConstruct or setters
    }
    
    // Constructor for tests (not used by Spring)
    public JwtTokenProvider(String secretKey, long jwtExpirationInMs) {
        initialize(secretKey, jwtExpirationInMs);
    }
    
    @Value("${jwt.secret}")
    public void setSecretKey(String secret) {
        if (this.secretKey == null) { // Only set if not already set by constructor
            this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        }
    }
    
    @Value("${jwt.expiration}")
    public void setJwtExpirationInMs(long jwtExpirationInMs) {
        if (this.jwtExpirationInMs == 0) { // Only set if not already set by constructor
            this.jwtExpirationInMs = jwtExpirationInMs;
        }
    }
    
    private void initialize(String secretKey, long jwtExpirationInMs) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtExpirationInMs = jwtExpirationInMs;
    }
    
    // Initialize with defaults if values not set
    public void init() {
        if (this.secretKey == null) {
            this.secretKey = Keys.hmacShaKeyFor("defaultSecretKey".getBytes());
        }
        if (this.jwtExpirationInMs == 0) {
            this.jwtExpirationInMs = 3600000L; // 1 hour default
        }
    }

    // Method that matches test expectations
    public String generateToken(Authentication authentication, Long expiration, String role) {
        init(); // Ensure initialization
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (expiration != null ? expiration : jwtExpirationInMs));
        
        String username = authentication.getName();
        
        Claims claims = Jwts.claims().setSubject(username);
        
        if (role != null && !role.isEmpty()) {
            claims.put("role", role);
        }
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            List<String> authorityNames = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            claims.put("authorities", authorityNames);
        }
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    
    public String generateToken(Authentication authentication, long expiration, String role) {
        return generateToken(authentication, Long.valueOf(expiration), role);
    }
    
    public String generateToken(String username) {
        init();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    
    public Claims getAllClaims(String token) {
        init();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaims(token).getSubject();
    }
    
    public String getRoleFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("role", String.class);
    }
    
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = getAllClaims(token);
        List<String> authorityNames = claims.get("authorities", List.class);
        
        if (authorityNames == null) {
            return new ArrayList<>();
        }
        
        return authorityNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        init();
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public long getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }
}