package com.aejimenezdev.authService.service.user;

import com.aejimenezdev.authService.exception.UserNotFoundException;
import com.aejimenezdev.authService.model.User;
import com.aejimenezdev.authService.model.UserRole;
import com.aejimenezdev.authService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .fullName("Test User")
                .role(UserRole.USER)
                .build();
    }

    @Nested
    @DisplayName("getByEmail()")
    class GetByEmailTests {

        @Test
        @DisplayName("should return user when found")
        void findByEmail_UserExists_ReturnsUser() {
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

            User result = userService.getByEmail("test@example.com");

            assertNotNull(result);
            assertEquals(testUser, result);
            verify(userRepository).findByEmail("test@example.com");
        }

        @Test
        @DisplayName("should return empty optional when user not found")
        void findByEmail_UserNotFound_ReturnsEmpty() {
            when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

            UserNotFoundException result = assertThrows(UserNotFoundException.class,
                    () -> userService.getByEmail("notfound@example.com"));

            assertEquals("User not found with email: notfound@example.com", result.getMessage());
            verify(userRepository).findByEmail("notfound@example.com");
        }
    }


    @Nested
    @DisplayName("existsByEmail()")
    class ExistsByEmailTests {

        @Test
        @DisplayName("should return true when email exists")
        void existsByEmail_EmailExists_ReturnsTrue() {
            when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

            boolean result = userService.existsByEmail("test@example.com");

            assertTrue(result);
            verify(userRepository).existsByEmail("test@example.com");
        }

        @Test
        @DisplayName("should return false when email does not exist")
        void existsByEmail_EmailNotExists_ReturnsFalse() {
            when(userRepository.existsByEmail("notfound@example.com")).thenReturn(false);

            boolean result = userService.existsByEmail("notfound@example.com");

            assertFalse(result);
            verify(userRepository).existsByEmail("notfound@example.com");
        }
    }

    @Nested
    @DisplayName("save()")
    class SaveTests {

        @Test
        @DisplayName("should delegate to repository and return saved user")
        void save_DelegatesToRepository() {
            when(userRepository.save(testUser)).thenReturn(testUser);

            User result = userService.save(testUser);

            assertEquals(testUser, result);
            verify(userRepository).save(testUser);
        }
    }
}