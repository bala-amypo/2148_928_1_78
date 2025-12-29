package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;   // ✅ FIXED IMPORT
import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private SecretKey secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    /**
     * Initializes a secure HS512 key
     * (Fixes 392-bit error permanently)
     */
    @PostConstruct
    public void init() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    // ================= TOKEN GENERATION =================

    public String generateToken(Authentication authentication, Long expiration, String role) {

        Date now = new Date();
        Date expiryDate = new Date(
                now.getTime() + (expiration != null ? expiration : jwtExpirationInMs)
        );

        Claims claims = Jwts.claims().setSubject(authentication.getName());

        if (role != null && !role.isEmpty()) {
            claims.put("role", role);
        }

        if (authentication.getAuthorities() != null) {
            List<String> authorities = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            claims.put("authorities", authorities);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // ================= TOKEN VALIDATION =================

    public boolean validateToken(String token) {
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

    public Claims getAllClaims(String token) {
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
        return getAllClaims(token).get("role", String.class);
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        List<String> roles = getAllClaims(token).get("authorities", List.class);
        if (roles == null) return Collections.emptyList();

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public long getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }
}
