package com.ftp.authservice.domain.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID id;

    @NotNull(message = "Username must not be null")
    private final String username;

    @NotNull(message = "Password must not be null")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters, contain a digit, an uppercase letter, and a special character")
    private final String password;  // The encrypted password

    @NotNull(message = "Email must not be null")
    private final String email;

    @NotNull(message = "First name must not be null")
    private final String firstName;

    @NotNull(message = "Last name must not be null")
    private final String lastName;

    @NotNull(message = "Role must not be null")
    private final String role;

    public User(UUID id, String username, String password,
                String email, String firstName, String lastName, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // Getters for all fields

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;  // Return the encrypted password
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
