package com.ftp.fundtransferservice.infrastructure.db.entities;

import com.ftp.fundtransferservice.shared.constants.Currency;
import com.ftp.fundtransferservice.shared.constants.TransferStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TransferEntity represents a transfer record in the database.
 * This entity is mapped to the "transfers" table and is used by JPA for persistence.
 */
@Entity
@Table(name = "transfers")
public class TransferEntity {

    @Id
    @GeneratedValue
    private UUID id; // The unique identifier for the transfer

    @Column(nullable = false)
    private UUID senderId; // The ID of the sender's account

    @Column(nullable = false)
    private UUID receiverId; // The ID of the receiver's account

    @Column(nullable = false)
    private BigDecimal amount; // The amount to be transferred

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency; // The currency of the transfer

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status; // The status of the transfer (e.g., completed, pending)

    private LocalDateTime createdAt; // The timestamp of when the transfer was created

    // Default constructor required by JPA
    public TransferEntity() {
    }

    // Constructor to initialize TransferEntity with provided values
    public TransferEntity(UUID id, UUID senderId, UUID receiverId, BigDecimal amount,
                          Currency currency, TransferStatus status, LocalDateTime createdAt) {
        // Ensure the amount is not negative
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters for all fields (no setters unless needed)
    public UUID getId() { return id; }
    public UUID getSenderId() { return senderId; }
    public UUID getReceiverId() { return receiverId; }
    public BigDecimal getAmount() { return amount; }
    public Currency getCurrency() { return currency; }
    public TransferStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
