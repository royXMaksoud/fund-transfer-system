package com.ftp.authservice.infrastructure.db.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class FailedLoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private int attemptCount;

    @Column(nullable = false)
    private Instant lastAttemptTime;

    public boolean isLocked() {
        return attemptCount >= 5 && Instant.now().isBefore(lastAttemptTime.plusMillis(30)); // Lock user for 30 minutes after 5 failed attempts
    }

    // Getters and setters

    public void incrementAttempts() {
        this.attemptCount++;
        this.lastAttemptTime = Instant.now();
    }

    // Constructor, Getters, and Setters
}
