package com.aejimenezdev.authService.service.auth;

import com.aejimenezdev.authService.dto.AuthResponse;
import com.aejimenezdev.authService.dto.LoginRequest;
import com.aejimenezdev.authService.dto.RegisterRequest;
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
            log.warn("[AuthService] Email ya registrado: {}", request.getEmail());
            throw new RuntimeException("Email ya registrado");
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
        log.info("[AuthService] Intentando login para usuario: {}", request.getEmail());

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("[AuthService] Login fallido - usuario no encontrado: {}", request.getEmail());
                    return new RuntimeException("Credenciales inválidas");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("[AuthService] Login fallido - contraseña incorrecta para: {}", request.getEmail());
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole().name());

        log.info("[AuthService] Login exitoso para usuario: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}