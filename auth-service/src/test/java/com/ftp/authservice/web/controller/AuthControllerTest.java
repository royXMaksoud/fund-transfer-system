package com.ftp.authservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftp.authservice.domain.model.User;
import com.ftp.authservice.domain.ports.in.LoginUseCase;
import com.ftp.authservice.domain.ports.in.RegisterUserUseCase;
import com.ftp.authservice.infrastructure.db.entities.RefreshTokenEntity;
import com.ftp.authservice.infrastructure.security.JwtTokenProvider;
import com.ftp.authservice.infrastructure.security.RefreshTokenService;
import com.ftp.authservice.web.dto.LoginRequestDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) //
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RegisterUserUseCase registerUserUseCase;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLogin_Successful() throws Exception {
        // Arrange
        String username = "roy";
        String rawPassword = "123Roy123";
        String jwtToken = "test-jwt-token";
        String refreshTokenValue = "refresh-token-123";

        User user = new User(
                UUID.randomUUID(),
                username,
                "encoded-password",
                "roy@example.com",
                "Roy",
                "User",
                "USER"
        );

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .id(UUID.randomUUID())
                .token(refreshTokenValue)
                .username(username)
                .role("USER")
                .expiryDate(Instant.now().plusSeconds(3600))
                .build();

        when(loginUseCase.login(username, rawPassword)).thenReturn(user);
        when(jwtTokenProvider.generateToken(username, "USER")).thenReturn(jwtToken);
        when(refreshTokenService.createRefreshToken(username, "USER")).thenReturn(refreshToken);

        LoginRequestDTO request = new LoginRequestDTO(username, rawPassword);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(jwtToken))
                .andExpect(jsonPath("$.refreshToken").value(refreshTokenValue))
                .andExpect(jsonPath("$.username").value(username));
    }
}
