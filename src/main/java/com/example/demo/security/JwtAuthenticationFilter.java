package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, 
                                  UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        final String requestUri = request.getRequestURI();
        final String method = request.getMethod();
        
        logger.info("══════════════════════════════════════════════════════════");
        logger.info("JWT FILTER START - " + method + " " + requestUri);
        
        // Skip filter for public endpoints
        if (isPublicEndpoint(requestUri)) {
            logger.info("Skipping JWT filter for public endpoint: " + requestUri);
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // Extract token from Authorization header
            String authHeader = request.getHeader("Authorization");
            logger.info("Authorization Header: " + (authHeader != null ? authHeader.substring(0, Math.min(30, authHeader.length())) + "..." : "NULL"));
            
            String token = getTokenFromRequest(request);
            logger.info("Token extracted: " + (token != null ? "YES (length: " + token.length() + ")" : "NO"));
            
            if (token != null) {
                // Log first part of token for debugging
                if (token.length() > 50) {
                    logger.info("Token preview: " + token.substring(0, 50) + "...");
                }
                
                // Validate token
                logger.info("Validating token...");
                boolean isValid = jwtTokenProvider.validateToken(token);
                logger.info("Token validation result: " + (isValid ? "VALID" : "INVALID"));
                
                if (isValid) {
                    // Extract username from token
                    String username = jwtTokenProvider.getUsernameFromToken(token);
                    logger.info("Username from token: " + username);
                    
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        logger.info("Attempting to load user: " + username);
                        
                        try {
                            // Load user details
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            logger.info("✅ User loaded successfully: " + userDetails.getUsername());
                            logger.info("✅ User authorities: " + userDetails.getAuthorities());
                            
                            // Create authentication token
                            UsernamePasswordAuthenticationToken authToken = 
                                new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                                );
                            
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                            logger.info("✅ Authentication set in SecurityContext for user: " + username);
                            
                        } catch (UsernameNotFoundException e) {
                            logger.error("❌ User not found in database: " + username);
                            logger.error("❌ Error: " + e.getMessage());
                        } catch (Exception e) {
                            logger.error("❌ Error loading user: " + e.getMessage(), e);
                        }
                    } else {
                        if (username == null) {
                            logger.error("❌ Username is null from token");
                        } else {
                            logger.info("Authentication already exists in context");
                        }
                    }
                } else {
                    logger.error("❌ Token validation failed!");
                    logger.error("❌ Request will proceed without authentication");
                }
            } else {
                logger.info("No JWT token found in request");
                logger.info("Request will proceed without authentication");
            }
            
        } catch (Exception e) {
            logger.error("❌ Unhandled exception in JWT filter: " + e.getMessage(), e);
            // Don't block the request, just continue without authentication
        }
        
        logger.info("JWT FILTER END - Proceeding with filter chain");
        logger.info("══════════════════════════════════════════════════════════");
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            // Clean up token (remove extra spaces, newlines, etc.)
            token = token.trim();
            logger.info("Cleaned token length: " + token.length());
            return token;
        }
        
        return null;
    }
    
    private boolean isPublicEndpoint(String requestUri) {
        // List of public endpoints that don't require JWT authentication
        return requestUri.startsWith("/api/auth/") ||
               requestUri.startsWith("/swagger-ui/") ||
               requestUri.startsWith("/v3/api-docs") ||
               requestUri.startsWith("/swagger-ui.html") ||
               requestUri.equals("/") ||
               requestUri.startsWith("/actuator/health") ||
               requestUri.startsWith("/h2-console/") ||
               requestUri.equals("/error");
    }
}