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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .fullName("Test User")
                .role(UserRole.USER)
                .build();

        registerRequest = RegisterRequest.builder()
                .email("test@example.com")
                .password("password123")
                .fullName("Test User")
                .build();

        loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();
    }

    @Nested
    @DisplayName("register()")
    class RegisterTests {

        @Test
        @DisplayName("should throw DuplicateEmailException when email already exists")
        void register_DuplicateEmail_ThrowsException() {
            when(userService.existsByEmail(anyString())).thenReturn(true);

            assertThrows(DuplicateEmailException.class, () -> authService.register(registerRequest));

            verify(userService).existsByEmail("test@example.com");
            verify(userService, never()).save(any(User.class));
        }

        @Test
        @DisplayName("should register successfully and return token")
        void register_Success_ReturnsToken() {
            when(userService.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userService.save(any(User.class))).thenReturn(testUser);
            when(jwtService.generateToken(anyString(), anyLong(), anyString())).thenReturn("jwt-token");

            AuthResponse response = authService.register(registerRequest);

            assertNotNull(response);
            assertEquals("jwt-token", response.getToken());
            assertEquals(1L, response.getUserId());
            assertEquals("test@example.com", response.getEmail());
            assertEquals("Test User", response.getFullName());

            verify(userService).existsByEmail("test@example.com");
            verify(userService).save(any(User.class));
            verify(jwtService).generateToken("test@example.com", 1L, "USER");
        }
    }

    @Nested
    @DisplayName("login()")
    class LoginTests {

        @Test
        @DisplayName("should throw AuthenticationException when user not found")
        void login_UserNotFound_ThrowsException() {
            when(userService.getByEmail(anyString())).thenThrow(new UserNotFoundException("User not found"));

            assertThrows(AuthenticationException.class, () -> authService.login(loginRequest));

            verify(userService).getByEmail("test@example.com");
            verify(passwordEncoder, never()).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("should throw AuthenticationException when password is wrong")
        void login_WrongPassword_ThrowsException() {
            when(userService.getByEmail(anyString())).thenReturn(testUser);
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

            assertThrows(AuthenticationException.class, () -> authService.login(loginRequest));

            verify(userService).getByEmail("test@example.com");
            verify(passwordEncoder).matches("password123", "encodedPassword");
        }

        @Test
        @DisplayName("should login successfully and return token")
        void login_Success_ReturnsToken() {
            when(userService.getByEmail(anyString())).thenReturn(testUser);
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
            when(jwtService.generateToken(anyString(), anyLong(), anyString())).thenReturn("jwt-token");

            AuthResponse response = authService.login(loginRequest);

            assertNotNull(response);
            assertEquals("jwt-token", response.getToken());
            assertEquals(1L, response.getUserId());
            assertEquals("test@example.com", response.getEmail());
            assertEquals("Test User", response.getFullName());

            verify(userService).getByEmail("test@example.com");
            verify(passwordEncoder).matches("password123", "encodedPassword");
            verify(jwtService).generateToken("test@example.com", 1L, "USER");
        }
    }
}