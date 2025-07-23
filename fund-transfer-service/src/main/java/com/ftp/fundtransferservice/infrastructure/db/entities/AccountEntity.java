package com.ftp.fundtransferservice.infrastructure.db.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * AccountEntity represents an account record in the database.
 * This entity is mapped to the "accounts" table and is used by JPA for persistence.
 */
@Entity
@Table(name = "accounts")
public class AccountEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID userId; // The UUID of the user who owns the account

    @Column(nullable = false)
    private BigDecimal balance; // The current balance in the account

    // Getters and Setters

    public UUID getId() { return id; }

    public UUID getUserId() { return userId; }

    public BigDecimal getBalance() { return balance; }

    public void setId(UUID id) { this.id = id; }

    public void setUserId(UUID userId) { this.userId = userId; }

    public void setBalance(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }

    // Default constructor required by JPA
    public AccountEntity() {
        // Required by JPA
    }

    // Constructor for creating an account entity
    public AccountEntity(UUID id, UUID userId, BigDecimal balance) {
        this.id = id;
        this.userId = userId;
        setBalance(balance);  // Ensure balance is validated before being set
    }
}
