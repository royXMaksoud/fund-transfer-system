package com.ftp.authservice.web.dto;

import com.ftp.authservice.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Response containing user information")
public class UserResponseDTO {

    @Schema(description = "Unique identifier of the user", example = "550e8400-e29b-41d4-a716-446655440000")
    public UUID id;

    @Schema(description = "Username of the user", example = "john_doe")
    public String username;

    @Schema(description = "Email address of the user", example = "john@example.com")
    public String email;

    @Schema(description = "Role assigned to the user", example = "USER")
    public String role;

    public UserResponseDTO(UUID id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
