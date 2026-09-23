package com.taskflow.taskflow_api.service;

import com.taskflow.taskflow_api.dto.AuthResponse;
import com.taskflow.taskflow_api.dto.LoginRequest;
import com.taskflow.taskflow_api.dto.RegisterRequest;
import com.taskflow.taskflow_api.entity.User;
import com.taskflow.taskflow_api.repository.UserRepository;
import com.taskflow.taskflow_api.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, jwtUtil);
    }

    // ---------- register ----------

    @Test
    void register_shouldCreateUserAndReturnToken_whenEmailIsNew() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashed-password");
        when(jwtUtil.generateToken("test@test.com")).thenReturn("fake-jwt-token");

        // Act
        AuthResponse result = authService.register(request);

        // Assert
        assertEquals("fake-jwt-token", result.getToken());
        verify(userRepository).save(any(User.class)); // confirm a user was actually saved
    }

    // THIS is the duplicate-email test you asked for
    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {
        // Arrange - pretend this email is already registered
        User existingUser = new User();
        existingUser.setEmail("test@test.com");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(existingUser));

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@test.com");
        request.setPassword("password123");

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> {
            authService.register(request);
        });

        // Confirm it never tried to save a duplicate user
        verify(userRepository, never()).save(any());
    }

    // ---------- login ----------

    @Test
    void login_shouldReturnToken_whenPasswordMatches() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("hashed-password");

        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed-password")).thenReturn(true);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("fake-jwt-token");

        // Act
        AuthResponse result = authService.login(request);

        // Assert
        assertEquals("fake-jwt-token", result.getToken());
    }

    @Test
    void login_shouldThrowException_whenPasswordIsWrong() {
        // Arrange
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("hashed-password");

        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("wrong-password");

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "hashed-password")).thenReturn(false);

        // Act + Assert
        assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    void login_shouldThrowException_whenEmailDoesNotExist() {
        when(userRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setEmail("ghost@test.com");
        request.setPassword("password123");

        assertThrows(BadCredentialsException.class, () -> {
            authService.login(request);
        });
    }
}
