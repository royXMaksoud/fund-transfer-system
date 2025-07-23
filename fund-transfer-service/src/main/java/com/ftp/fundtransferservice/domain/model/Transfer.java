package com.ftp.fundtransferservice.domain.model;

import com.ftp.fundtransferservice.shared.constants.Currency;
import com.ftp.fundtransferservice.shared.constants.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The Transfer class represents a money transfer between two accounts.
 * It contains details about the transfer such as sender, receiver, amount, currency, status, and creation timestamp.
 */
public class Transfer {

    // The unique identifier for the transfer
    private UUID id;

    // The unique identifier for the sender's account
    private UUID senderId;

    // The unique identifier for the receiver's account
    private UUID receiverId;

    // The amount of money being transferred
    private BigDecimal amount;

    // The currency of the transfer (e.g., USD, EUR, etc.)
    private Currency currency; // ✅ enum not String to ensure valid currencies

    // The current status of the transfer (e.g., PENDING, COMPLETED)
    private TransferStatus status;

    // The date and time when the transfer was created
    private LocalDateTime createdAt;

    /**
     * Constructor to create a new Transfer instance with the specified details.
     * This constructor initializes the transfer with the provided values.
     *
     * @param id the unique identifier for the transfer
     * @param senderId the UUID of the sender's account
     * @param receiverId the UUID of the receiver's account
     * @param amount the amount of money being transferred
     * @param currency the currency of the transfer
     * @param status the current status of the transfer
     * @param createdAt the timestamp when the transfer was created
     * @throws IllegalArgumentException if the amount is negative
     */
    public Transfer(UUID id,
                    UUID senderId,
                    UUID receiverId,
                    BigDecimal amount,
                    Currency currency,
                    TransferStatus status,
                    LocalDateTime createdAt) {
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

    /**
     * Gets the unique identifier of the transfer.
     *
     * @return the transfer ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the unique identifier of the sender's account.
     *
     * @return the sender's account ID
     */
    public UUID getSenderId() {
        return senderId;
    }

    /**
     * Gets the unique identifier of the receiver's account.
     *
     * @return the receiver's account ID
     */
    public UUID getReceiverId() {
        return receiverId;
    }

    /**
     * Gets the amount of money being transferred.
     *
     * @return the amount of the transfer
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Gets the currency of the transfer (e.g., USD, EUR).
     *
     * @return the currency of the transfer
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Gets the current status of the transfer (e.g., PENDING, COMPLETED).
     *
     * @return the status of the transfer
     */
    public TransferStatus getStatus() {
        return status;
    }

    /**
     * Gets the creation timestamp of the transfer.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
