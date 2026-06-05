package com.aejimenezdev.authService.service.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    private JwtServiceImpl jwtService;

    private static final String SECRET = "mySecretKeymySecretKeymySecretKeymySecretKeymySecretKey";
    private static final Long EXPIRATION = 1000L * 60 * 60 * 24;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", EXPIRATION);
    }

    @Nested
    @DisplayName("generateToken()")
    class GenerateTokenTests {

        @Test
        @DisplayName("should generate a valid token")
        void generateToken_ReturnsValidToken() {
            String token = jwtService.generateToken("test@example.com", 1L, "USER");

            assertNotNull(token);
            assertFalse(token.isEmpty());
        }
    }

    @Nested
    @DisplayName("extractUsername()")
    class ExtractUsernameTests {

        @Test
        @DisplayName("should extract email from token")
        void extractUsername_ReturnsEmail() {
            String token = jwtService.generateToken("test@example.com", 1L, "USER");

            String extractedEmail = jwtService.extractUsername(token);

            assertEquals("test@example.com", extractedEmail);
        }
    }

    @Nested
    @DisplayName("extractUserId()")
    class ExtractUserIdTests {

        @Test
        @DisplayName("should extract userId from token")
        void extractUserId_ReturnsUserId() {
            String token = jwtService.generateToken("test@example.com", 123L, "USER");

            Long extractedUserId = jwtService.extractUserId(token);

            assertEquals(123L, extractedUserId);
        }
    }

    @Nested
    @DisplayName("isTokenExpired()")
    class IsTokenExpiredTests {

        @Test
        @DisplayName("should return false for non-expired token")
        void isTokenExpired_NonExpiredToken_ReturnsFalse() {
            String token = jwtService.generateToken("test@example.com", 1L, "USER");

            boolean isExpired = jwtService.isTokenExpired(token);

            assertFalse(isExpired);
        }
    }

    @Nested
    @DisplayName("validateToken()")
    class ValidateTokenTests {

        @Test
        @DisplayName("should return true for valid token with correct username")
        void validateToken_ValidTokenAndUsername_ReturnsTrue() {
            String token = jwtService.generateToken("test@example.com", 1L, "USER");

            boolean isValid = jwtService.validateToken(token, "test@example.com");

            assertTrue(isValid);
        }

        @Test
        @DisplayName("should return false for valid token with wrong username")
        void validateToken_ValidTokenWrongUsername_ReturnsFalse() {
            String token = jwtService.generateToken("test@example.com", 1L, "USER");

            boolean isValid = jwtService.validateToken(token, "wrong@example.com");

            assertFalse(isValid);
        }

        @Test
        @DisplayName("should return false for invalid token")
        void validateToken_InvalidToken_ReturnsFalse() {
            boolean isValid = jwtService.validateToken("invalid.token.here", "test@example.com");

            assertFalse(isValid);
        }
    }
}