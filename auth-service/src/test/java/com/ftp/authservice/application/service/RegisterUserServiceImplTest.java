package com.ftp.authservice.application.service;

import com.ftp.authservice.domain.model.User;
import com.ftp.authservice.domain.ports.out.SaveUserPort;
import com.ftp.authservice.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class RegisterUserServiceImplTest {

    @Mock
    private SaveUserPort saveUserPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterServiceImpl registerService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User(
                UUID.randomUUID(),
                "newuser",
                "raw-password",
                "new@example.com",
                "New",
                "User",
                "USER"
        );
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        when(saveUserPort.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("raw-password")).thenReturn("encoded-password");

        // Use ArgumentCaptor to verify saved user
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        when(saveUserPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = registerService.register(user);

        // Assert
        assertEquals("newuser", result.getUsername());
        assertEquals("encoded-password", result.getPassword());

        verify(saveUserPort).save(userCaptor.capture());
        assertEquals("encoded-password", userCaptor.getValue().getPassword());
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists_ShouldThrowException() {
        // Arrange
        when(saveUserPort.existsByUsername("newuser")).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () ->
                registerService.register(user));

        assertEquals("Username already exists", exception.getMessage());

        verify(saveUserPort, never()).save(any());
    }

}
