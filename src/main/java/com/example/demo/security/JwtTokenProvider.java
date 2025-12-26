package com.example.demo.security;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenProvider {

    private final String secret;
    private final long expirationMs;

    public JwtTokenProvider(String secret, long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    public String generateToken(Authentication auth, Long userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        claims.put("email", auth.getName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(auth.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ REQUIRED BY JwtAuthenticationFilter
    public String getEmailFromToken(String token) {
        return getAllClaims(token).get("email").toString();
    }

    public String getUsernameFromToken(String token) {
        return getEmailFromToken(token);
    }

    public Map<String, Object> getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
