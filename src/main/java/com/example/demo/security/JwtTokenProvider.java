package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    
    private SecretKey secretKey;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    /**
     * Initializes a secure HS512 key from configured secret
     */
    @jakarta.annotation.PostConstruct
    public void init() {
        try {
            logger.info("🔑 Initializing JwtTokenProvider...");
            logger.info("🔑 JWT Secret length: " + jwtSecret.length());
            logger.info("🔑 JWT Expiration (ms): " + jwtExpirationInMs);
            logger.info("🔑 JWT Expiration (hours): " + (jwtExpirationInMs / 1000 / 60 / 60) + " hours");
            
            // Ensure the secret is at least 64 characters for HS512
            if (jwtSecret.length() < 64) {
                logger.warn("⚠️ JWT secret is less than 64 characters. Padding to 64...");
                String paddedSecret = String.format("%-64s", jwtSecret).substring(0, 64);
                this.secretKey = Keys.hmacShaKeyFor(paddedSecret.getBytes());
                logger.info("✅ Padded secret to 64 characters");
            } else {
                this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
                logger.info("✅ Using provided secret key (length: " + jwtSecret.length() + ")");
            }
            
            logger.info("✅ JwtTokenProvider initialized successfully");
            logger.info("✅ Secret key algorithm: " + secretKey.getAlgorithm());
            
        } catch (Exception e) {
            logger.error("❌ Failed to initialize JwtTokenProvider: " + e.getMessage(), e);
            throw new RuntimeException("Failed to initialize JwtTokenProvider", e);
        }
    }

    // ================= TOKEN GENERATION =================

    public String generateToken(Authentication authentication) {
        try {
            logger.info("🔐 Generating JWT token for user: " + authentication.getName());
            
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
            
            logger.info("   Current time: " + now);
            logger.info("   Expiry time: " + expiryDate);
            logger.info("   Expires in: " + (jwtExpirationInMs / 1000 / 60) + " minutes");
            
            Claims claims = Jwts.claims().setSubject(authentication.getName());

            // Add authorities/roles
            if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
                List<String> authorities = authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());
                claims.put("authorities", authorities);
                logger.info("   Authorities added: " + authorities);
            }

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .compact();
            
            logger.info("✅ Token generated successfully");
            logger.info("   Token length: " + token.length());
            logger.info("   Token preview: " + token.substring(0, Math.min(50, token.length())) + "...");
            
            // Verify the token can be parsed back
            try {
                Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
                logger.info("✅ Token self-verification successful");
            } catch (Exception e) {
                logger.error("❌ Token self-verification failed: " + e.getMessage());
            }
            
            return token;
            
        } catch (Exception e) {
            logger.error("❌ Error generating token: " + e.getMessage(), e);
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    public String generateToken(String username) {
        try {
            logger.info("🔐 Generating JWT token for username: " + username);
            
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
            
            String token = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .compact();
            
            logger.info("✅ Simple token generated for: " + username);
            return token;
            
        } catch (Exception e) {
            logger.error("❌ Error generating simple token: " + e.getMessage(), e);
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    // ================= TOKEN VALIDATION =================

    public boolean validateToken(String token) {
        try {
            logger.info("🔐 Validating JWT token...");
            logger.info("   Token length: " + token.length());
            logger.info("   Token preview: " + token.substring(0, Math.min(50, token.length())) + "...");
            
            // Check if token structure looks valid (should have 3 parts separated by dots)
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                logger.error("❌ Invalid token structure - expected 3 parts, got " + parts.length);
                return false;
            }
            
            logger.info("   Token structure: ✅ Header.Payload.Signature");
            
            // Parse and validate the token
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            
            Claims claims = claimsJws.getBody();
            String username = claims.getSubject();
            Date expiration = claims.getExpiration();
            Date issuedAt = claims.getIssuedAt();
            
            logger.info("✅ Token validation successful");
            logger.info("   Subject (username): " + username);
            logger.info("   Issued at: " + issuedAt);
            logger.info("   Expiration: " + expiration);
            logger.info("   Current time: " + new Date());
            
            // Check expiration
            if (expiration.before(new Date())) {
                logger.error("❌ Token has expired!");
                logger.error("   Expired at: " + expiration);
                logger.error("   Current time: " + new Date());
                return false;
            }
            
            // Check if token was issued in the future (clock skew)
            Date now = new Date();
            if (issuedAt.after(new Date(now.getTime() + 300000))) { // 5 minutes tolerance
                logger.error("❌ Token issued in the future!");
                return false;
            }
            
            // Check authorities
            List<String> authorities = claims.get("authorities", List.class);
            if (authorities != null && !authorities.isEmpty()) {
                logger.info("   Authorities in token: " + authorities);
            }
            
            logger.info("✅ Token is valid and not expired");
            return true;
            
        } catch (ExpiredJwtException e) {
            logger.error("❌ JWT Token expired: " + e.getMessage());
            logger.error("   Expired at: " + e.getClaims().getExpiration());
            return false;
        } catch (MalformedJwtException e) {
            logger.error("❌ Invalid JWT token format: " + e.getMessage());
            logger.error("   This usually means the token was tampered with or corrupted");
            return false;
        } catch (SignatureException e) {
            logger.error("❌ Invalid JWT signature: " + e.getMessage());
            logger.error("❌ Possible secret key mismatch!");
            logger.error("❌ The token was signed with a different secret key");
            return false;
        } catch (IllegalArgumentException e) {
            logger.error("❌ JWT claims string is empty or null: " + e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("❌ Unsupported JWT token: " + e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("❌ Unexpected error validating token: " + e.getMessage(), e);
            return false;
        }
    }

    public Claims getAllClaims(String token) {
        try {
            logger.info("📄 Getting all claims from token...");
            
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            logger.info("✅ Claims retrieved successfully");
            logger.info("   Subject: " + claims.getSubject());
            logger.info("   Expiration: " + claims.getExpiration());
            logger.info("   Issued at: " + claims.getIssuedAt());
            
            return claims;
            
        } catch (Exception e) {
            logger.error("❌ Error getting claims from token: " + e.getMessage(), e);
            throw new RuntimeException("Failed to parse JWT claims", e);
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            logger.info("👤 Extracting username from token...");
            String username = getAllClaims(token).getSubject();
            logger.info("✅ Username extracted: " + username);
            return username;
        } catch (Exception e) {
            logger.error("❌ Error extracting username: " + e.getMessage());
            return null;
        }
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        try {
            logger.info("⚖️ Extracting authorities from token...");
            Claims claims = getAllClaims(token);
            List<String> authorities = claims.get("authorities", List.class);
            
            if (authorities == null || authorities.isEmpty()) {
                logger.info("   No authorities found in token");
                return Collections.emptyList();
            }
            
            logger.info("✅ Authorities found: " + authorities);
            return authorities.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            logger.error("❌ Error extracting authorities: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public long getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }
    
    // Helper method to decode token without validation (for debugging)
    public void debugToken(String token) {
        try {
            logger.info("🔍 DEBUGGING TOKEN:");
            logger.info("   Full token: " + token);
            
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                // Decode header
                String header = new String(Base64.getUrlDecoder().decode(parts[0]));
                logger.info("   Header: " + header);
                
                // Decode payload
                String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
                logger.info("   Payload: " + payload);
                
                logger.info("   Signature length: " + parts[2].length() + " chars");
            }
        } catch (Exception e) {
            logger.error("❌ Error debugging token: " + e.getMessage());
        }
    }
}