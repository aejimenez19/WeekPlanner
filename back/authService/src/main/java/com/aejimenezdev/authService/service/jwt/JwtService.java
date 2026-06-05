package com.aejimenezdev.authService.service.jwt;

import java.util.Date;

public interface JwtService {
    String extractUsername(String token);
    Long extractUserId(String token);
    Date extractExpiration(String token);
    boolean isTokenExpired(String token);
    String generateToken(String email, Long userId, String role);
    boolean validateToken(String token, String username);
}