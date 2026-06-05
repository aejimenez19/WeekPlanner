package com.aejimenezdev.authService.service.auth;

import com.aejimenezdev.authService.dto.AuthResponse;
import com.aejimenezdev.authService.dto.LoginRequest;
import com.aejimenezdev.authService.dto.RegisterRequest;
import com.aejimenezdev.authService.exception.AuthenticationException;
import com.aejimenezdev.authService.exception.DuplicateEmailException;
import com.aejimenezdev.authService.exception.UserNotFoundException;
import com.aejimenezdev.authService.model.User;
import com.aejimenezdev.authService.model.UserRole;
import com.aejimenezdev.authService.service.jwt.JwtService;
import com.aejimenezdev.authService.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        log.info("[AuthService] Iniciando registro de usuario: {}", request.getEmail());

        if (userService.existsByEmail(request.getEmail())) {
            log.warn("[AuthService] Email already registered: {}", request.getEmail());
            throw new DuplicateEmailException("Email already registered");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(UserRole.USER)
                .build();

        User savedUser = userService.save(user);

        String token = jwtService.generateToken(savedUser.getEmail(), savedUser.getId(), savedUser.getRole().name());

        log.info("[AuthService] Usuario registrado exitosamente: {}", savedUser.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("[AuthService] Attempting login for user: {}", request.getEmail());

        User user;
        try {
            user = userService.getByEmail(request.getEmail());
        } catch (UserNotFoundException e) {
            log.warn("[AuthService] Login failed - user not found: {}", request.getEmail());
            throw new AuthenticationException("Invalid credentials");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("[AuthService] Login failed - wrong password for: {}", request.getEmail());
            throw new AuthenticationException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole().name());

        log.info("[AuthService] Login successful for user: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}