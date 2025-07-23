package com.ftp.fundtransferservice.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * AccountRequest is a Data Transfer Object (DTO) used for creating a new account.
 * This DTO includes the user ID and balance that are required to create an account.
 */
public class AccountRequest {

    /**
     * The unique identifier of the user who owns the account.
     * This field is required to associate the account with a specific user.
     */
    @NotNull(message = "userId must not be null")
    @Schema(description = "Unique identifier of the user who owns the account", example = "b2f4d3e6-a789-1234-5678-90abcdef1234", required = true)
    private UUID userId;

    /**
     * The initial balance for the account.
     * This field is required to set up the account with a starting balance.
     */
    @NotNull(message = "balance must not be null")
    @DecimalMin(value = "0.00", inclusive = true, message = "balance must be non-negative")
    @Schema(description = "Initial balance of the account", example = "1000.00", required = true)
    private BigDecimal balance;

    /**
     * Gets the user ID associated with the account.
     *
     * @return the user ID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Sets the user ID for the account.
     *
     * @param userId the user ID to associate with the account
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Gets the balance of the account.
     *
     * @return the balance of the account
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets the balance for the account.
     *
     * @param balance the initial balance to set for the account
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
