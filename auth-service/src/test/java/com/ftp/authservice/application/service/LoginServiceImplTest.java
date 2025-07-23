package com.ftp.authservice.application.service;

import com.ftp.authservice.domain.model.User;
import com.ftp.authservice.domain.ports.out.LoadUserPort;
import com.ftp.authservice.infrastructure.db.entities.FailedLoginAttempt;
import com.ftp.authservice.infrastructure.repositories.FailedLoginAttemptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class LoginServiceImplTest {

    @Mock
    private LoadUserPort loadUserPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private FailedLoginAttemptRepository failedLoginAttemptRepository;

    @InjectMocks
    private LoginServiceImpl loginService;

    private User user;
    private FailedLoginAttempt attempt;

    @BeforeEach
    void setup() {
        user = new User(
                UUID.fromString("8f3d6e5c-4e6b-460f-a434-64d3d3658b1b"),
                "roy",
                "encoded-password",
                "roy@example.com",
                "Roy",
                "User",
                "USER"
        );

        attempt = new FailedLoginAttempt();
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        when(failedLoginAttemptRepository.findByUsername("roy")).thenReturn(Optional.of(attempt));
        when(loadUserPort.loadByUsername("roy")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123Roy123", "encoded-password")).thenReturn(true);

        // Act
        User result = loginService.login("roy", "123Roy123");

        // Assert
        assertNotNull(result);
        assertEquals("roy", result.getUsername());

        // Verify that failed attempt record was deleted
        verify(failedLoginAttemptRepository).delete(attempt);
        verifyNoMoreInteractions(failedLoginAttemptRepository);
    }

    @Test
    void testLogin_InvalidPassword_ShouldIncrementFailedAttempts() {
        // Arrange
        when(failedLoginAttemptRepository.findByUsername("roy")).thenReturn(Optional.of(attempt));
        when(loadUserPort.loadByUsername("roy")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encoded-password")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                loginService.login("roy", "wrongPassword"));

        assertEquals("Invalid username or password", exception.getMessage());

        // Verify failed attempt was incremented and saved
        verify(failedLoginAttemptRepository).save(attempt);
        verify(failedLoginAttemptRepository, never()).delete(any());
    }

    @Test
    void testLogin_UserNotFound_ShouldIncrementFailedAttempts() {
        // Arrange
        when(failedLoginAttemptRepository.findByUsername("unknownUser")).thenReturn(Optional.of(attempt));
        when(loadUserPort.loadByUsername("unknownUser")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                loginService.login("unknownUser", "anyPassword"));

        assertEquals("Invalid username or password", exception.getMessage());

        // Verify failed attempt was incremented and saved
        verify(failedLoginAttemptRepository).save(attempt);
        verify(failedLoginAttemptRepository, never()).delete(any());
    }

}
