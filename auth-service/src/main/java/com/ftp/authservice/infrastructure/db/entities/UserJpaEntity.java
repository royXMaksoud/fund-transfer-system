package com.ftp.authservice.infrastructure.db.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entity class representing a User in the database.
 * This class is used for storing and retrieving user data.
 */
@Entity
@Table(name = "users")  // Table name in the database
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Added strategy for UUID generation
    private UUID id;  // Unique identifier for the user

    @Column(nullable = false, unique = true)  // Ensuring the username is unique and not null
    private String username;  // Username, must be unique

    @Column(nullable = false)  // Password cannot be null, stores encrypted password
    private String password;  // This will store the encrypted password (hashed)

    @Column(nullable = false, unique = true)  // Email must be unique and not null
    private String email;  // User's email, should be unique

    @Column(nullable = false)  // First name cannot be null
    private String firstName;  // User's first name

    @Column(nullable = false)  // Last name cannot be null
    private String lastName;  // User's last name

    @Column(nullable = false)  // Role cannot be null
    private String role;  // User's role (e.g., ADMIN, USER)
}
