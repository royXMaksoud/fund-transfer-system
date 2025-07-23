package com.ftp.authservice.web.dto;

import com.ftp.authservice.domain.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for registering a new user")
public class RegisterRequestDTO {

    @NotBlank(message = "Username must not be empty")
    @Schema(description = "Unique username for the new user", example = "roy")
    private String username;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Password for the new user (min 6 characters)", example = "123roy123")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Valid email address", example = "maksoud@example.com")
    private String email;

    @NotBlank(message = "First name must not be empty")
    @Schema(description = "User's first name", example = "Mak")
    private String firstName;

    @NotBlank(message = "Last name must not be empty")
    @Schema(description = "User's last name", example = "John")
    private String lastName;

    @NotBlank(message = "Role must not be empty")
    @Schema(description = "User role (e.g., ADMIN, USER)", example = "USER")
    private String role;

    public RegisterRequestDTO() {}

    public RegisterRequestDTO(String username, String password, String email,
                              String firstName, String lastName, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    /**
     * Converts this DTO into a User domain model
     */
    public User toDomain() {
        return new User(
                null,
                this.username,
                this.password,
                this.email,
                this.firstName,
                this.lastName,
                this.role
        );
    }
}
