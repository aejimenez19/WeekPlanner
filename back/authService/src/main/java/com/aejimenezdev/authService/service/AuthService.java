package com.aejimenezdev.authService.service;

import com.aejimenezdev.authService.config.JwtConfig;
import com.aejimenezdev.authService.dto.AuthResponse;
import com.aejimenezdev.authService.dto.LoginRequest;
import com.aejimenezdev.authService.dto.RegisterRequest;
import com.aejimenezdev.authService.model.User;
import com.aejimenezdev.authService.model.UserRole;
import com.aejimenezdev.authService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;

    public AuthResponse register(RegisterRequest request) {
        log.info("[AuthService] Iniciando registro de usuario: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("[AuthService] Email ya registrado: {}", request.getEmail());
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
        
        log.info("[AuthService] Usuario registrado exitosamente: {}", savedUser.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        log.info("[AuthService] Intentando login para usuario: {}", request.getEmail());
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("[AuthService] Login fallido - usuario no encontrado: {}", request.getEmail());
                    return new RuntimeException("Credenciales inválidas");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("[AuthService] Login fallido - contraseña incorrecta para: {}", request.getEmail());
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtConfig.generateToken(user.getEmail(), user.getId(), user.getRole().name());
        
        log.info("[AuthService] Login exitoso para usuario: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}