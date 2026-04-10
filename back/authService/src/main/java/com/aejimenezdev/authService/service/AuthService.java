package com.aejimenezdev.authService.service;

import com.aejimenezdev.authService.config.JwtConfig;
import com.aejimenezdev.authService.dto.AuthResponse;
import com.aejimenezdev.authService.dto.LoginRequest;
import com.aejimenezdev.authService.dto.RegisterRequest;
import com.aejimenezdev.authService.model.User;
import com.aejimenezdev.authService.model.UserRole;
import com.aejimenezdev.authService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtConfig.generateToken(savedUser.getEmail(), savedUser.getId(), savedUser.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtConfig.generateToken(user.getEmail(), user.getId(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}