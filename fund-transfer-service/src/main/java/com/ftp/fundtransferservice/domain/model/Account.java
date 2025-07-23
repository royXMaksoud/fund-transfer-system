package com.ftp.fundtransferservice.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * The Account class represents a financial account for a user in the system.
 * Each account has a unique ID, a user ID to associate it with a specific user,
 * and a balance that represents the amount of money in the account.
 */
public class Account {

    // The unique identifier for the account
    private UUID id;

    // The unique identifier of the user that owns the account
    private UUID userId;

    // The balance of the account, represented as a BigDecimal
    private BigDecimal balance;

    /**
     * Constructor to create a new account with the specified ID, user ID, and balance.
     *
     * @param id the unique identifier for the account
     * @param userId the user ID to associate the account with a specific user
     * @param balance the initial balance of the account
     * @throws IllegalArgumentException if the balance is negative
     */
    public Account(UUID id, UUID userId, BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.id = id;
        this.userId = userId;
        this.balance = balance;
    }

    /**
     * Gets the unique identifier for the account.
     *
     * @return the account ID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the unique identifier for the user that owns the account.
     *
     * @return the user ID associated with the account
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets the balance of the account.
     *
     * @return the current balance of the account
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account.
     *
     * @param balance the new balance to set for the account
     * @throws IllegalArgumentException if the balance is negative
     */
    public void setBalance(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }

    /**
     * Returns the amount of money in the account. This is an alias for the balance.
     *
     * @return the amount (balance) in the account
     */
    public BigDecimal getAmount() {
        return balance;
    }
}
