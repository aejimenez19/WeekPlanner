package com.aejimenezdev.authService.service.auth;

import com.aejimenezdev.authService.dto.AuthResponse;
import com.aejimenezdev.authService.dto.LoginRequest;
import com.aejimenezdev.authService.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}